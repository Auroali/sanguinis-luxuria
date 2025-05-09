package com.auroali.sanguinisluxuria.common.conversions;

import com.auroali.sanguinisluxuria.common.registry.BLRegistries;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.Function;

public interface EntityConversionCondition {
    boolean test(ConversionContext context);

    JsonObject toJson();

    Serializer<?> getSerializer();

    static <T extends EntityConversionCondition> Identifier getId(Serializer<T> serializer) {
        return BLRegistries.CONVERSION_CONDITIONS.getId(serializer);
    }

    static EntityConversionCondition fromJson(JsonObject object) {
        if (Serializer.CACHE != null && Serializer.CACHE.containsKey(object)) {
            return Serializer.CACHE.get(object);
        }
        if (!object.has("type"))
            throw new JsonParseException("Missing type field");

        Identifier id = Identifier.tryParse(object.get("type").getAsString());
        if (id == null)
            throw new JsonParseException("Failed to parse id " + object.get("type"));

        Serializer<?> transformerSerializer = BLRegistries.CONVERSION_CONDITIONS.get(id);
        if (transformerSerializer == null)
            throw new JsonParseException("Unknown condition " + id);
        EntityConversionCondition transformer = transformerSerializer.fromJson(object);
        if (Serializer.CACHE != null)
            Serializer.CACHE.put(object, transformer);

        return transformer;
    }

    class Serializer<T extends EntityConversionCondition> {
        private static HashMap<JsonObject, EntityConversionCondition> CACHE;
        private final Function<JsonObject, T> fromJson;
        private final Function<T, JsonObject> toJson;

        public Serializer(Function<JsonObject, T> fromJson) {
            this.fromJson = fromJson;
            this.toJson = T::toJson;
        }

        public JsonObject toJson(T object) {
            JsonObject json = this.toJson.apply(object);
            Identifier id = EntityConversionCondition.getId(this);
            if (id == null)
                throw new IllegalStateException("Attempted to save condition using unregistered serializer!");
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
