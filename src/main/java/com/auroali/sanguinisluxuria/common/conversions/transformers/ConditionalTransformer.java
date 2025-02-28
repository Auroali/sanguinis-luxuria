package com.auroali.sanguinisluxuria.common.conversions.transformers;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionCondition;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionTransformer;
import com.auroali.sanguinisluxuria.common.conversions.conditions.ConversionContextCondition;
import com.auroali.sanguinisluxuria.common.registry.BLConversions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

/**
 * Transformer that only runs the provided transformer if some condition succeeds
 */
public class ConditionalTransformer implements EntityConversionTransformer {
    private final EntityConversionTransformer transformer;
    private final EntityConversionCondition condition;

    public ConditionalTransformer(EntityConversionTransformer transformer, EntityConversionCondition condition) {
        this.transformer = transformer;
        this.condition = condition;
    }

    @Override
    public void apply(ConversionContext context, NbtCompound nbtIn, NbtCompound nbtOut) {
        if (this.condition.test(context))
            this.transformer.apply(context, nbtIn, nbtOut);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.add(
          "transformer",
          ((EntityConversionTransformer.Serializer) this.transformer.getSerializer()).toJson(this.transformer)
        );
        object.add(
          "condition",
          ((EntityConversionCondition.Serializer) this.condition.getSerializer()).toJson(this.condition)
        );
        return object;
    }

    public static ConditionalTransformer fromJson(JsonObject object) {
        if (!object.has("condition"))
            throw new JsonParseException("Missing condition field");
        if (!object.has("transformer"))
            throw new JsonParseException("Missing transformer field");
        EntityConversionTransformer transformer = EntityConversionTransformer.fromJson(object.getAsJsonObject("transformer"));
        EntityConversionCondition condition = EntityConversionCondition.fromJson(object.getAsJsonObject("condition"));

        return new ConditionalTransformer(transformer, condition);
    }

    @Override
    public Serializer<?> getSerializer() {
        return BLConversions.CONDITIONAL_TRANSFORMER;
    }

    public static ConditionalTransformer biome(EntityConversionTransformer transformer, RegistryKey<Biome> biome) {
        return new ConditionalTransformer(
          transformer,
          ConversionContextCondition.predicate(EntityPredicate.Builder
            .create()
            .location(LocationPredicate.biome(biome))
            .build()
          )
        );
    }
}
