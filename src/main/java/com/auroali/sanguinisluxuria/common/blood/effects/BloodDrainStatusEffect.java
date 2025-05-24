package com.auroali.sanguinisluxuria.common.blood.effects;

import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.auroali.sanguinisluxuria.common.blood.BloodDrainEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.dynamic.Codecs;

public record BloodDrainStatusEffect(StatusEffect effect, int duration, int amplifier,
                                     float chance) implements BloodDrainEffect {
    public static final Codec<BloodDrainStatusEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Registries.STATUS_EFFECT.getCodec().fieldOf("effect").forGetter(BloodDrainStatusEffect::effect),
      Codecs.NONNEGATIVE_INT.optionalFieldOf("duration", 300).forGetter(BloodDrainStatusEffect::duration),
      Codecs.NONNEGATIVE_INT.optionalFieldOf("amplifier", 0).forGetter(BloodDrainStatusEffect::amplifier),
      Codec.floatRange(0.f, 1.f).optionalFieldOf("chance", 1.f).forGetter(BloodDrainStatusEffect::chance)
    ).apply(instance, BloodDrainStatusEffect::new));

    @Override
    public void apply(LivingEntity entity) {
        if (entity.getRandom().nextFloat() < this.chance())
            entity.addStatusEffect(new StatusEffectInstance(this.effect(), this.duration(), this.amplifier()));
    }

    @Override
    public boolean canMerge(BloodDrainEffect other) {
        return other instanceof BloodDrainStatusEffect otherEffect && this.effect() == otherEffect.effect();
    }

    @Override
    public BloodDrainEffect merge(BloodDrainEffect other) {
        BloodDrainStatusEffect otherEffect = (BloodDrainStatusEffect) other;
        if (this.effect() != otherEffect.effect()) {
            SanguinisLuxuria.LOGGER.warn("Cannot merge two BloodDrainEffectInstances with different effects {} and {}", Registries.STATUS_EFFECT.getId(this.effect()), Registries.STATUS_EFFECT.getId(otherEffect.effect()));
            return this;
        }
        if (otherEffect.amplifier() > this.amplifier()) {
            return otherEffect;
        }
        if (this.amplifier() == otherEffect.amplifier()) {
            return new BloodDrainStatusEffect(this.effect(), Math.max(this.duration(), otherEffect.duration()), this.amplifier(), Math.max(this.chance(), otherEffect.chance()));
        }
        return this;
    }

    @Override
    public Codec<BloodDrainStatusEffect> getCodec() {
        return CODEC;
    }
}
