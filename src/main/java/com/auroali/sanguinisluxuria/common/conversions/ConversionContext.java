package com.auroali.sanguinisluxuria.common.conversions;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

public record ConversionContext(
  ServerWorld world,
  Entity entity,
  Entity source,
  Conversion conversion,
  ConversionCallback convertedCallback
) {
    public static Builder builder(Entity entity) {
        if (!(entity.getWorld() instanceof ServerWorld world))
            throw new IllegalArgumentException("Cannot create a non-server conversion context");
        return new Builder(world, entity);
    }

    public enum Conversion implements StringIdentifiable {
        // represents converting to a vampire
        CONVERTING("converting"),
        // represents deconverting from being a vampire
        DECONVERTING("deconverting");

        public static final com.mojang.serialization.Codec<Conversion> CODEC = StringIdentifiable.createCodec(Conversion::values);

        private final String name;

        Conversion(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        public static Conversion fromJson(JsonElement element) {
            if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString())
                throw new JsonParseException("Expected a string for the conversion field");

            return switch (element.getAsString()) {
                case "converting" -> CONVERTING;
                case "deconverting" -> DECONVERTING;
                default -> throw new JsonParseException("Unknown conversion " + element.getAsString());
            };
        }
    }

    public static class Builder {
        private final ServerWorld world;
        private final Entity entity;
        private Entity source;
        private Conversion conversion;
        private ConversionCallback callback;

        protected Builder(ServerWorld world, Entity entity) {
            this.world = world;
            this.entity = entity;
            this.conversion = Conversion.CONVERTING;
        }

        public Builder withConversion(Conversion conversion) {
            this.conversion = conversion;
            return this;
        }

        public Builder withSource(Entity entity) {
            this.source = entity;
            return this;
        }

        public Builder withCallback(ConversionCallback callback) {
            this.callback = callback;
            return this;
        }

        public ConversionContext build() {
            return new ConversionContext(this.world, this.entity, this.source, this.conversion, this.callback);
        }
    }

    @FunctionalInterface
    public interface ConversionCallback {
        void onConverted(Entity convertedEntity, @Nullable Entity conversionSource);
    }
}
