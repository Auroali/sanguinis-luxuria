package com.auroali.sanguinisluxuria.common.conversions;

import com.auroali.sanguinisluxuria.common.registry.SLRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Function;

public interface EntityConversionCondition {
    Codec<EntityConversionCondition> CODEC = SLRegistries.CONVERSION_CONDITIONS
      .getCodec()
      .dispatch("type", EntityConversionCondition::getCodec, Function.identity());
    Codec<List<EntityConversionCondition>> LIST_CODEC = Codec.list(CODEC);

    boolean test(ConversionContext context);

    Codec<? extends EntityConversionCondition> getCodec();

    int hashCode();

    boolean equals(Object other);

    static <T extends EntityConversionCondition> Identifier getId(EntityConversionCondition transformer) {
        return SLRegistries.CONVERSION_CONDITIONS.getId(transformer.getCodec());
    }
}
