package com.auroali.sanguinisluxuria.common.rituals;

import com.auroali.sanguinisluxuria.common.registry.BLRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public interface RitualType<T extends Ritual> {
    default String getTranslationKey() {
        return Util.createTranslationKey("altar_ritual", getId(this));
    }

    Codec<T> getCodec();

    static <T extends Ritual> RitualType<T> fromCodec(Codec<T> codec) {
        return () -> codec;
    }

    static <T extends Ritual> Identifier getId(RitualType<T> type) {
        return BLRegistries.RITUAL_TYPES.getId(type);
    }
}
