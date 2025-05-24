package com.auroali.sanguinisluxuria.common.rituals.types;

import com.auroali.sanguinisluxuria.common.registry.SLRitualTypes;
import com.auroali.sanguinisluxuria.common.rituals.Ritual;
import com.auroali.sanguinisluxuria.common.rituals.RitualParameters;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public record StatusEffectRitual(List<StatusEffect> effects, int duration, int amplifier,
                                 Target target) implements Ritual {
    public static final Codec<StatusEffectRitual> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.either(Registries.STATUS_EFFECT.getCodec(), Registries.STATUS_EFFECT.getCodec().listOf())
        .xmap(either -> either.map(List::of, list -> list), Either::right)
        .fieldOf("effect").forGetter(StatusEffectRitual::effects),
      Codec.INT.optionalFieldOf("duration", 3600).forGetter(StatusEffectRitual::duration),
      Codec.INT.optionalFieldOf("amplifier", 0).forGetter(StatusEffectRitual::duration),
      Target.CODEC.optionalFieldOf("target", Target.RITUAL_TARGET).forGetter(StatusEffectRitual::target)
    ).apply(instance, StatusEffectRitual::new));

    @Override
    public void onCompleted(RitualParameters parameters) {
        switch (this.target()) {
            case ALL -> {
                this.applyToEntity(parameters.target(), parameters.initiator());
                this.applyToOthers(parameters.world(), parameters.initiator(), parameters.pos());
            }
            case OTHER -> this.applyToOthers(parameters.world(), parameters.initiator(), parameters.pos());
            case RITUAL_TARGET -> this.applyToEntity(parameters.target(), parameters.initiator());
        }
    }

    public void applyToEntity(LivingEntity entity, LivingEntity initiator) {
        this.effects().forEach(effect ->
          entity.addStatusEffect(new StatusEffectInstance(effect, this.duration(), 0), initiator)
        );
    }

    public void applyToOthers(World world, LivingEntity initiator, BlockPos pos) {
        world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), new Box(pos).expand(16.d), entity -> entity != initiator && entity.isAlive())
          .forEach(entity ->
            this.applyToEntity(entity, initiator)
          );
    }

    @Override
    public void appendTooltips(List<Text> tooltips) {
        Ritual.super.appendTooltips(tooltips);
        tooltips.add(Text.translatable("altar_ritual.sanguinisluxuria.effects", this.target.asString(), this.duration / 20.f));
        for (StatusEffect effect : this.effects) {
            tooltips.add(Text.translatable(effect.getTranslationKey()));
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
        private final List<StatusEffect> effects;
        private int duration;
        private int amplifier;
        private Target target;

        protected Builder() {
            this.effects = new ArrayList<>();
            this.duration = 3600;
            this.target = Target.RITUAL_TARGET;
        }

        public Builder addEffect(StatusEffect effect) {
            this.effects.add(effect);
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder target(Target target) {
            this.target = target;
            return this;
        }

        public Builder amplifier(int amplifier) {
            this.amplifier = amplifier;
            return this;
        }

        public StatusEffectRitual build() {
            return new StatusEffectRitual(this.effects, this.duration, this.amplifier, this.target);
        }
    }

    public enum Target implements StringIdentifiable {
        RITUAL_TARGET("target"),
        OTHER("others"),
        ALL("all");

        public static final com.mojang.serialization.Codec<Target> CODEC = StringIdentifiable
          .createCodec(Target::values);

        private final String name;

        Target(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}
