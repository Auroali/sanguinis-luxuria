package com.auroali.sanguinisluxuria.common.conversions;

import com.auroali.sanguinisluxuria.common.registry.SLRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Function;

public interface EntityConversionTransformer {
    Codec<EntityConversionTransformer> CODEC = SLRegistries.CONVERSION_TRANSFORMERS
      .getCodec()
      .dispatch("type", EntityConversionTransformer::getCodec, Function.identity());
    Codec<List<EntityConversionTransformer>> LIST_CODEC = Codec.list(CODEC);

    void apply(ConversionContext context, NbtCompound nbtIn, NbtCompound nbtOut, List<ConversionContext.ConversionCallback> callbacks);

    Codec<? extends EntityConversionTransformer> getCodec();

    int hashCode();

    boolean equals(Object other);

    static <T extends EntityConversionTransformer> Identifier getId(EntityConversionTransformer transformer) {
        return SLRegistries.CONVERSION_TRANSFORMERS.getId(transformer.getCodec());
    }
}
