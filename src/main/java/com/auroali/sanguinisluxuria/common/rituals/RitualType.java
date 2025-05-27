package com.auroali.sanguinisluxuria.common.rituals;

import com.auroali.sanguinisluxuria.common.registry.SLRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public interface RitualType<T extends Ritual> {
    default String getTranslationKey() {
        return Util.createTranslationKey("ritual_type", getId(this));
    }

    Codec<T> getCodec();

    static <T extends Ritual> RitualType<T> fromCodec(Codec<T> codec) {
        return () -> codec;
    }

    static <T extends Ritual> Identifier getId(RitualType<T> type) {
        return SLRegistries.RITUAL_TYPES.getId(type);
    }
}
