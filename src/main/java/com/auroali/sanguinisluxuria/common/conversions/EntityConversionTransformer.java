package com.auroali.sanguinisluxuria.common.conversions;

import com.auroali.sanguinisluxuria.common.registry.BLRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Function;

public interface EntityConversionTransformer {
    Codec<EntityConversionTransformer> CODEC = BLRegistries.CONVERSION_TRANSFORMERS
      .getCodec()
      .dispatch("type", EntityConversionTransformer::getCodec, Function.identity());
    Codec<List<EntityConversionTransformer>> LIST_CODEC = Codec.list(CODEC);

    void apply(ConversionContext context, NbtCompound nbtIn, NbtCompound nbtOut);

    Codec<? extends EntityConversionTransformer> getCodec();

    int hashCode();

    boolean equals(Object other);

    static <T extends EntityConversionTransformer> Identifier getId(EntityConversionTransformer transformer) {
        return BLRegistries.CONVERSION_TRANSFORMERS.getId(transformer.getCodec());
    }
}
