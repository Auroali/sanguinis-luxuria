package com.auroali.sanguinisluxuria.common.conversions.transformers;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionTransformer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

/**
 * Transformer that copies an NBT field from the source entity to the target entity,
 * with the specified NBT paths
 */
public record CopyConversionTransformer(NbtTreeLocation source,
                                        NbtTreeLocation destination) implements EntityConversionTransformer {
    public static final Codec<CopyConversionTransformer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      NbtTreeLocation.CODEC.fieldOf("source").forGetter(CopyConversionTransformer::source),
      NbtTreeLocation.CODEC.optionalFieldOf("destination", NbtTreeLocation.empty()).forGetter(
        condition -> condition.destination.equals(condition.source) ? NbtTreeLocation.empty() : condition.destination
      )
    ).apply(instance, (src, dst) -> new CopyConversionTransformer(
      src,
      dst == NbtTreeLocation.empty() ? src : dst
    )));

    public static CopyConversionTransformer create(String src) {
        return create(src, src);
    }

    public static CopyConversionTransformer create(String src, String dst) {
        return new CopyConversionTransformer(NbtTreeLocation.fromString(src), NbtTreeLocation.fromString(dst));
    }

    @Override
    public void apply(ConversionContext context, NbtCompound nbtIn, NbtCompound nbtOut) {
        NbtElement element = this.source.get(nbtIn);
        if (element != null)
            this.destination.insertInto(nbtOut, element);
    }

    @Override
    public Codec<CopyConversionTransformer> getCodec() {
        return CODEC;
    }

    @Override
    public int hashCode() {
        return 31 * this.source.hashCode() + 7 * this.destination.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        return o instanceof CopyConversionTransformer other
          && other.source.equals(this.source)
          && other.destination.equals(this.destination);
    }
}
