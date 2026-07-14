package com.auroali.sanguinisluxuria.common.conversions.transformers;

import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionTransformer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.*;

import java.util.List;

/**
 * Sets the NBT field at the specified path to the provided value for the new entity
 */
public record SetTransformer(NbtPathArgumentType.NbtPath destination,
                             NbtElement element) implements EntityConversionTransformer {
    private static final Codec<NbtPathArgumentType.NbtPath> PATH_CODEC = Codec.STRING.comapFlatMap(
      str -> {
          try {
              NbtPathArgumentType.NbtPath path = NbtPathArgumentType.nbtPath().parse(new StringReader(str));
              return DataResult.success(path);
          } catch (CommandSyntaxException e) {
              return DataResult.error(e::getMessage);
          }
      },
      NbtPathArgumentType.NbtPath::toString
    );
    public static Codec<SetTransformer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      PATH_CODEC.fieldOf("destination").forGetter(SetTransformer::destination),
      // snbt codec, to allow specifying types
      Codec.STRING.comapFlatMap(
        str -> {
            try {
                return DataResult.success(new StringNbtReader(new StringReader(str)).parseElement());
            } catch (CommandSyntaxException e) {
                return DataResult.error(e::getMessage);
            }
        },
        NbtElement::toString
      ).fieldOf("nbt").forGetter(SetTransformer::element)
    ).apply(instance, SetTransformer::new));

    @Override
    public void apply(ConversionContext context, NbtCompound nbtIn, NbtCompound nbtOut, List<ConversionContext.ConversionCallback> callbacks) {
        try {
            this.destination.put(nbtOut, this.element);
        } catch (CommandSyntaxException ignored) {
        }
    }

    @Override
    public Codec<SetTransformer> getCodec() {
        return CODEC;
    }

    public static SetTransformer create(String dst, NbtElement element) {
        return new SetTransformer(
          NbtPathCodec.fromString(dst).getOrThrow(false, SanguinisLuxuria.LOGGER::error),
          element
        );
    }

    public static SetTransformer create(String dst, int element) {
        return new SetTransformer(
          NbtPathCodec.fromString(dst).getOrThrow(false, SanguinisLuxuria.LOGGER::error),
          NbtInt.of(element)
        );
    }

    public static SetTransformer create(String dst, long element) {
        return new SetTransformer(
          NbtPathCodec.fromString(dst).getOrThrow(false, SanguinisLuxuria.LOGGER::error),
          NbtLong.of(element)
        );
    }

    public static SetTransformer create(String dst, byte element) {
        return new SetTransformer(
          NbtPathCodec.fromString(dst).getOrThrow(false, SanguinisLuxuria.LOGGER::error),
          NbtByte.of(element)
        );
    }

    public static SetTransformer create(String dst, short element) {
        return new SetTransformer(
          NbtPathCodec.fromString(dst).getOrThrow(false, SanguinisLuxuria.LOGGER::error),
          NbtShort.of(element)
        );
    }

    public static SetTransformer create(String dst, String element) {
        return new SetTransformer(
          NbtPathCodec.fromString(dst).getOrThrow(false, SanguinisLuxuria.LOGGER::error),
          NbtString.of(element)
        );
    }

    public static EntityConversionTransformer create(String dst, float element) {
        return new SetTransformer(
          NbtPathCodec.fromString(dst).getOrThrow(false, SanguinisLuxuria.LOGGER::error),
          NbtFloat.of(element)
        );
    }

    public static EntityConversionTransformer create(String dst, double element) {
        return new SetTransformer(
          NbtPathCodec.fromString(dst).getOrThrow(false, SanguinisLuxuria.LOGGER::error),
          NbtDouble.of(element)
        );
    }
}
