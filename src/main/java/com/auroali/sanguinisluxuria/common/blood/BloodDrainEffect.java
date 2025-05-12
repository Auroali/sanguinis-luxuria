package com.auroali.sanguinisluxuria.common.blood;

import com.auroali.sanguinisluxuria.common.registry.BLRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.entity.LivingEntity;

import java.util.List;
import java.util.function.Function;

public interface BloodDrainEffect {
    Codec<BloodDrainEffect> CODEC = BLRegistries.BLOOD_DRAIN_EFFECTS
      .getCodec()
      .dispatch("type", BloodDrainEffect::getCodec, Function.identity());
    Codec<List<BloodDrainEffect>> LIST_CODEC = Codec.list(CODEC);

    void apply(LivingEntity entity);

    boolean canMerge(BloodDrainEffect other);

    BloodDrainEffect merge(BloodDrainEffect other);

    Codec<? extends BloodDrainEffect> getCodec();
}
