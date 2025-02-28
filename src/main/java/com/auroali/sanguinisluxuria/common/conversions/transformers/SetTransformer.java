package com.auroali.sanguinisluxuria.common.conversions.transformers;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionTransformer;
import com.auroali.sanguinisluxuria.common.registry.BLConversions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.*;
import net.minecraft.util.JsonHelper;

/**
 * Sets the NBT field at the specified path to the provided value for the new entity
 */
public class SetTransformer implements EntityConversionTransformer {
    final NbtTreeLocation dst;
    final NbtElement element;

    public SetTransformer(NbtTreeLocation dst, NbtElement element) {
        this.dst = dst;
        this.element = element;
    }

    @Override
    public void apply(ConversionContext context, NbtCompound nbtIn, NbtCompound nbtOut) {
        this.dst.insertInto(nbtOut, this.element);
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        nbtToJson(this.element, object);
        object.addProperty("dst", this.dst.toString());
        return object;
    }

    @Override
    public Serializer<?> getSerializer() {
        return BLConversions.SET_TRANSFORMER;
    }

    public static SetTransformer fromJson(JsonObject object) {
        NbtElement element = nbtFromJson(object);
        NbtTreeLocation dst = NbtTreeLocation.fromString(object.get("dst").getAsString());
        if (dst == null)
            throw new JsonParseException("Failed to parse nbt tree location");
        return new SetTransformer(dst, element);
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


    protected static NbtElement nbtFromJson(JsonObject object) {
        try {
            return new StringNbtReader(new StringReader(JsonHelper.getString(object, "nbt"))).parseElement();
        } catch (CommandSyntaxException e) {
            throw new JsonParseException("Failed to parse nbt field", e);
        }
    }

    protected static void nbtToJson(NbtElement element, JsonObject object) {
        object.addProperty("nbt", element.toString());
    }
}
