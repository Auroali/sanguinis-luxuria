package com.auroali.sanguinisluxuria.common.rituals.types;

import com.auroali.sanguinisluxuria.common.registry.SLRitualTypes;
import com.auroali.sanguinisluxuria.common.rituals.Ritual;
import com.auroali.sanguinisluxuria.common.rituals.RitualParameters;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.auroali.sanguinisluxuria.common.rituals.RitualUtil;
import com.google.common.base.Predicates;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

// todo: rework?
public record StatusEffectRitual(List<StatusEffectInstance> effects,
                                 Target target) implements Ritual {
    // evil infinite codec
    private static final Codec<Integer> DURATION_CODEC = Codec.either(
      Codec.intRange(1, 1000000).xmap(seconds -> seconds * 20, ticks -> ticks / 20),
      Codec.STRING.flatXmap(
        str -> str.equals("infinite") ? DataResult.success(StatusEffectInstance.INFINITE) : DataResult.error(() -> "Expected either an integer value or 'infinite'"),
        i -> i == StatusEffectInstance.INFINITE ? DataResult.success("infinite") : DataResult.error(() -> "Non -1 value passed into infinite codec")
      )
    ).xmap(
      either -> either.map(Function.identity(), Function.identity()),
      i -> i < 0 ? Either.right(i) : Either.left(i)
    );
    private static final Codec<StatusEffectInstance> STATUS_EFFECT_INSTANCE = RecordCodecBuilder.create(instance -> instance.group(
      Registries.STATUS_EFFECT.getCodec().fieldOf("effect").forGetter(StatusEffectInstance::getEffectType),
      DURATION_CODEC.optionalFieldOf("duration", 3600).forGetter(StatusEffectInstance::getDuration),
      Codec.intRange(0, 255).optionalFieldOf("amplifier", 0).forGetter(StatusEffectInstance::getAmplifier)
    ).apply(instance, StatusEffectInstance::new));
    public static final Codec<StatusEffectRitual> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.either(STATUS_EFFECT_INSTANCE, STATUS_EFFECT_INSTANCE.listOf())
        .xmap(
          either -> either.map(List::of, Function.identity()),
          list -> list.size() == 1 ? Either.left(list.get(0)) : Either.right(list)
        ).fieldOf("effects").forGetter(StatusEffectRitual::effects),
      Target.CODEC.optionalFieldOf("target", Target.RITUAL_TARGET).forGetter(StatusEffectRitual::target)
    ).apply(instance, StatusEffectRitual::new));

    @Override
    public void onCompleted(RitualParameters parameters) {
        switch (this.target) {
            case RITUAL_TARGET -> {
                parameters.target().or(parameters::initiator).ifPresent(target -> {
                    RitualUtil.spawnSuccessParticles(parameters);
                    this.applyTo(parameters, target);
                });
            }
            case RADIUS_TARGET -> {
                parameters.target().or(parameters::initiator).ifPresent(target -> {
                    RitualUtil.spawnSuccessParticles(parameters);
                    this.applyToAllAround(parameters, target.getPos(), Predicates.alwaysTrue());
                });
            }
            case RADIUS_TARGET_EXCLUDING -> {
                parameters.target().or(parameters::initiator).ifPresent(target -> {
                    RitualUtil.spawnSuccessParticles(parameters);
                    this.applyToAllAround(parameters, target.getPos(), entity -> entity != target);
                });
            }
            case RADIUS_ALTAR -> {
                this.applyToAllAround(parameters, parameters.pos().toCenterPos(), Predicates.alwaysTrue());
            }
        }
    }

    private void applyToAllAround(RitualParameters parameters, Vec3d origin, Predicate<LivingEntity> predicate) {
        parameters.world().getNonSpectatingEntities(LivingEntity.class, Box.from(origin).expand(16.d))
          .forEach(entity -> {
              if (predicate.test(entity))
                  this.applyTo(parameters, entity);
          });
    }

    private void applyTo(RitualParameters parameters, LivingEntity entity) {
        this.effects.forEach(effect -> {
            entity.addStatusEffect(
              new StatusEffectInstance(effect),
              parameters.initiator().orElse(null)
            );
        });
        RitualUtil.spawnSuccessParticlesAt(parameters, entity.getPos());
    }

    @Override
    public void appendTooltips(List<Text> tooltips) {
        Ritual.super.appendTooltips(tooltips);
        tooltips.add(Text.translatable(this.target.getTranslationKey()));
        for (StatusEffectInstance effect : this.effects) {
            MutableText effectText = Text.translatable(effect.getTranslationKey());
            if (effect.getAmplifier() > 0) {
                effectText = Text.translatable(
                  "potion.withAmplifier",
                  effectText,
                  Text.translatable("potion.potency." + effect.getAmplifier())
                );
            }
            if (!effect.isDurationBelow(20)) {
                effectText = Text.translatable(
                  "potion.withDuration",
                  effectText,
                  StatusEffectUtil.getDurationText(effect, 1.f)
                );
            }
            tooltips.add(Text.translatable(
              this.getType().getTranslationKey() + ".effect_entry",
              effectText
            ).formatted(Formatting.GRAY));
        }
    }

    @Override
    public RitualType<?> getType() {
        return SLRitualTypes.STATUS_EFFECT_RITUAL_TYPE;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<StatusEffectInstance> effects;
        private Target target;

        protected Builder() {
            this.effects = new ArrayList<>();
            this.target = Target.RITUAL_TARGET;
        }

        public Builder addEffect(StatusEffect effect) {
            return this.addEffect(new StatusEffectInstance(effect, 3600));
        }

        public Builder addEffect(StatusEffect effect, int duration) {
            return this.addEffect(new StatusEffectInstance(effect, duration));
        }

        public Builder addEffect(StatusEffect effect, int duration, int amplifier) {
            return this.addEffect(new StatusEffectInstance(effect, duration, amplifier));
        }

        private Builder addEffect(StatusEffectInstance effect) {
            this.effects.add(effect);
            return this;
        }

        public Builder target(Target target) {
            this.target = target;
            return this;
        }

        public StatusEffectRitual build() {
            return new StatusEffectRitual(this.effects, this.target);
        }
    }

    public enum Target implements StringIdentifiable {
        RITUAL_TARGET("target"),
        RADIUS_TARGET("around_target_inclusive"),
        RADIUS_TARGET_EXCLUDING("around_target_exclusive"),
        RADIUS_ALTAR("around_altar");

        public static final com.mojang.serialization.Codec<Target> CODEC = StringIdentifiable
          .createCodec(Target::values);

        private final String name;
        private final String translationKey;

        Target(String name) {
            this.name = name;
            this.translationKey = "ritual_type.effect.target." + this.name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public String getTranslationKey() {
            return this.translationKey;
        }
    }
}
