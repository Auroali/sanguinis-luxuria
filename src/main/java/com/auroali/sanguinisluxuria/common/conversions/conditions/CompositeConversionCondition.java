package com.auroali.sanguinisluxuria.common.conversions.conditions;

import com.auroali.sanguinisluxuria.common.conversions.EntityConversionCondition;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionData;
import com.auroali.sanguinisluxuria.common.registry.BLRegistries;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;

/**
 * Abstract class that represents a composite condition (one made up of multiple other conditions)
 *
 * @see AndConversionCondition
 * @see OrConversionCondition
 */
public abstract class CompositeConversionCondition implements EntityConversionCondition {
    protected final List<EntityConversionCondition> conditions;

    public CompositeConversionCondition(List<EntityConversionCondition> conditions) {
        this.conditions = conditions;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public JsonObject toJson() {
        if (this.conditions.isEmpty()) {
            throw new IllegalStateException("conditions must not be empty.");
        }

        JsonObject object = new JsonObject();
        JsonArray conditionsArray = new JsonArray();
        for (EntityConversionCondition condition : this.conditions) {
            Identifier typeId = BLRegistries.CONVERSION_CONDITIONS.getId(condition.getSerializer());
            if (typeId == null)
                continue;

            JsonObject conditionJson = ((Serializer) condition.getSerializer()).toJson(condition);
            conditionsArray.add(conditionJson);
        }

        object.add("conditions", conditionsArray);
        return object;
    }

    public static <T extends CompositeConversionCondition> T fromJson(JsonObject object, Factory<T> factory) {
        if (!object.has("conditions"))
            throw new JsonParseException("Missing conditions field");
        if (!object.get("conditions").isJsonArray())
            throw new JsonParseException("Expected conditions to be a json array, got " + object.get("conditions"));

        List<EntityConversionCondition> conditions = EntityConversionData.parseConditions(object.getAsJsonArray("conditions"));
        if (conditions.isEmpty()) {
            throw new JsonParseException("conditions must not be empty.");
        }
        return factory.create(conditions);
    }

    public static CompositeConversionCondition or(EntityConversionCondition... conditions) {
        return new OrConversionCondition(Arrays.asList(conditions));
    }

    public static CompositeConversionCondition and(EntityConversionCondition... conditions) {
        return new AndConversionCondition(Arrays.asList(conditions));
    }

    @FunctionalInterface
    public interface Factory<T extends CompositeConversionCondition> {
        T create(List<EntityConversionCondition> conditions);
    }
}
