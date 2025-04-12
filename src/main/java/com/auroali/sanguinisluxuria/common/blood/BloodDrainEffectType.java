package com.auroali.sanguinisluxuria.common.blood;

import com.mojang.serialization.Codec;

public interface BloodDrainEffectType<T extends BloodDrainEffect> {
    Codec<T> getCodec();

    static <T extends BloodDrainEffect> BloodDrainEffectType<T> fromCodec(Codec<T> codec) {
        return () -> codec;
    }
}
