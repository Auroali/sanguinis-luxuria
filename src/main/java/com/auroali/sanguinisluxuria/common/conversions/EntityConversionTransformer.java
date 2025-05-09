package com.auroali.sanguinisluxuria.common.conversions;

import com.auroali.sanguinisluxuria.common.registry.BLRegistries;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.Function;

public interface EntityConversionTransformer {
    void apply(ConversionContext context, NbtCompound nbtIn, NbtCompound nbtOut);

    JsonObject toJson();

    Serializer<?> getSerializer();

    static <T extends EntityConversionTransformer> Identifier getId(Serializer<T> serializer) {
        return BLRegistries.CONVERSION_TRANSFORMERS.getId(serializer);
    }

    static EntityConversionTransformer fromJson(JsonObject object) {
        if (Serializer.CACHE != null && Serializer.CACHE.containsKey(object)) {
            return Serializer.CACHE.get(object);
        }
        if (!object.has("type"))
            throw new JsonParseException("Missing type field");

        Identifier id = Identifier.tryParse(object.get("type").getAsString());
        if (id == null)
            throw new JsonParseException("Failed to parse id " + object.get("type"));

        Serializer<?> transformerSerializer = BLRegistries.CONVERSION_TRANSFORMERS.get(id);
        if (transformerSerializer == null)
            throw new JsonParseException("Unknown transformer " + id);
        EntityConversionTransformer transformer = transformerSerializer.fromJson(object);
        if (Serializer.CACHE != null)
            Serializer.CACHE.put(object, transformer);

        return transformer;
    }

    class Serializer<T extends EntityConversionTransformer> {
        private static HashMap<JsonObject, EntityConversionTransformer> CACHE;
        private final Function<JsonObject, T> fromJson;
        private final Function<T, JsonObject> toJson;

        public Serializer(Function<JsonObject, T> fromJson) {
            this.fromJson = fromJson;
            this.toJson = T::toJson;
        }

        public JsonObject toJson(T object) {
            JsonObject json = this.toJson.apply(object);
            Identifier id = EntityConversionTransformer.getId(this);
            if (id == null)
                throw new IllegalStateException("Attempted to save transformer using unregistered serializer!");
            json.addProperty("type", id.toString());
            return json;
        }

        public T fromJson(JsonObject object) {
            return this.fromJson.apply(object);
        }

        public static void initCache() {
            CACHE = new HashMap<>();
        }

        public static void dropCache() {
            CACHE = null;
        }
    }
}
