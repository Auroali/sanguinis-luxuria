package com.auroali.sanguinisluxuria.common.conversions.transformers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.command.argument.NbtPathArgumentType;

public class NbtPathCodec {
    private static final NbtPathArgumentType PATH_ARGUMENT_TYPE = NbtPathArgumentType.nbtPath();
    public static final Codec<NbtPathArgumentType.NbtPath> CODEC = Codec.STRING.comapFlatMap(
      NbtPathCodec::fromString,
      NbtPathArgumentType.NbtPath::toString
    );

    public static DataResult<NbtPathArgumentType.NbtPath> fromString(String str) {
        try {
            return DataResult.success(PATH_ARGUMENT_TYPE.parse(new StringReader(str)));
        } catch (CommandSyntaxException e) {
            return DataResult.error(e::getMessage);
        }
    }
}
