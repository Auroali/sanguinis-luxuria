package com.auroali.sanguinisluxuria.common.conversions.transformers;

import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionTransformer;
import com.google.common.collect.Iterables;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.List;
import java.util.Optional;

/**
 * Transformer that copies an NBT field from the source entity to the target entity,
 * with the specified NBT paths
 */
public record CopyConversionTransformer(NbtPathArgumentType.NbtPath source,
                                        Optional<NbtPathArgumentType.NbtPath> destination) implements EntityConversionTransformer {
    public static final Codec<CopyConversionTransformer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      NbtPathCodec.CODEC.fieldOf("source").forGetter(CopyConversionTransformer::source),
      NbtPathCodec.CODEC.optionalFieldOf("destination").forGetter(CopyConversionTransformer::destination)
    ).apply(instance, CopyConversionTransformer::new));

    public static CopyConversionTransformer create(String src) {
        return new CopyConversionTransformer(
          NbtPathCodec.fromString(src).getOrThrow(false, SanguinisLuxuria.LOGGER::error),
          Optional.empty()
        );
    }

    public static CopyConversionTransformer create(String src, String dst) {
        return new CopyConversionTransformer(
          NbtPathCodec.fromString(src).getOrThrow(false, SanguinisLuxuria.LOGGER::error),
          Optional.of(NbtPathCodec.fromString(dst).getOrThrow(false, SanguinisLuxuria.LOGGER::error))
        );
    }

    @Override
    public void apply(ConversionContext context, NbtCompound nbtIn, NbtCompound nbtOut, List<ConversionContext.ConversionCallback> callbacks) {
        try {
            List<NbtElement> element = this.source.get(nbtIn);
            this.destination.orElse(this.source)
              .put(nbtOut, Iterables.getLast(element));
        } catch (CommandSyntaxException ignored) {
        }
    }

    @Override
    public Codec<CopyConversionTransformer> getCodec() {
        return CODEC;
    }
}
