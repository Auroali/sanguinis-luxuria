package com.auroali.sanguinisluxuria.common.conversions.transformers;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionTransformer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.*;

/**
 * Sets the NBT field at the specified path to the provided value for the new entity
 */
public record SetTransformer(NbtTreeLocation destination, NbtElement element) implements EntityConversionTransformer {
    public static Codec<SetTransformer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      NbtTreeLocation.CODEC.fieldOf("destination").forGetter(SetTransformer::destination),
      // snbt codec, to allow specifying types
      Codec.STRING.flatXmap(
        str -> {
            try {
                return DataResult.success(new StringNbtReader(new StringReader(str)).parseElement());
            } catch (CommandSyntaxException e) {
                return DataResult.error(e::getMessage);
            }
        },
        nbt -> DataResult.success(nbt.toString())
      ).fieldOf("nbt").forGetter(SetTransformer::element)
    ).apply(instance, SetTransformer::new));

    @Override
    public void apply(ConversionContext context, NbtCompound nbtIn, NbtCompound nbtOut) {
        this.destination.insertInto(nbtOut, this.element);
    }

    @Override
    public Codec<SetTransformer> getCodec() {
        return CODEC;
    }

    @Override
    public int hashCode() {
        return 31 * this.destination.hashCode() + 7 * this.element.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        return o instanceof SetTransformer other
          && other.destination.equals(this.destination)
          && other.element.equals(this.element);
    }

    public static SetTransformer create(String dst, NbtElement element) {
        return new SetTransformer(NbtTreeLocation.fromString(dst), element);
    }

    public static SetTransformer create(String dst, int element) {
        return new SetTransformer(NbtTreeLocation.fromString(dst), NbtInt.of(element));
    }

    public static SetTransformer create(String dst, long element) {
        return new SetTransformer(NbtTreeLocation.fromString(dst), NbtLong.of(element));
    }

    public static SetTransformer create(String dst, byte element) {
        return new SetTransformer(NbtTreeLocation.fromString(dst), NbtByte.of(element));
    }

    public static SetTransformer create(String dst, short element) {
        return new SetTransformer(NbtTreeLocation.fromString(dst), NbtShort.of(element));
    }

    public static SetTransformer create(String dst, String element) {
        return new SetTransformer(NbtTreeLocation.fromString(dst), NbtString.of(element));
    }

    public static EntityConversionTransformer create(String dst, float element) {
        return new SetTransformer(NbtTreeLocation.fromString(dst), NbtFloat.of(element));
    }

    public static EntityConversionTransformer create(String dst, double element) {
        return new SetTransformer(NbtTreeLocation.fromString(dst), NbtDouble.of(element));
    }
}
