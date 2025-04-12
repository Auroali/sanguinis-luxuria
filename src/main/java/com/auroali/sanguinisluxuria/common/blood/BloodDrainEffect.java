package com.auroali.sanguinisluxuria.common.blood;

import com.auroali.sanguinisluxuria.common.registry.BLRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.entity.LivingEntity;

public interface BloodDrainEffect {
    Codec<BloodDrainEffect> CODEC = BLRegistries.BLOOD_DRAIN_EFFECTS
      .getCodec()
      .dispatch("type", BloodDrainEffect::getType, BloodDrainEffectType::getCodec);

    void apply(LivingEntity entity);

    boolean canMerge(BloodDrainEffect other);

    BloodDrainEffect merge(BloodDrainEffect other);

    BloodDrainEffectType<?> getType();
}
