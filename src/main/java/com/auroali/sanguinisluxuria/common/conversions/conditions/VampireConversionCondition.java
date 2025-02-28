package com.auroali.sanguinisluxuria.common.conversions.conditions;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionCondition;
import com.auroali.sanguinisluxuria.common.registry.BLConversions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Condition that succeeds if the target entity's current vampire status matches the specified vampire status
 * <br> i.e. if the target entity is a vampire and the isVampire field is true, this condition succeeds
 */
public class VampireConversionCondition implements EntityConversionCondition {
    private final boolean isVampire;

    public VampireConversionCondition(boolean isVampire) {
        this.isVampire = isVampire;
    }

    @Override
    public boolean test(ConversionContext context) {
        return this.isVampire == VampireHelper.isVampire(context.entity());
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("isVampire", this.isVampire);
        return object;
    }

    public static VampireConversionCondition fromJson(JsonObject object) {
        if (!object.has("isVampire"))
            throw new JsonParseException("Missing isVampire field");

        return new VampireConversionCondition(object.get("isVampire").getAsBoolean());
    }

    @Override
    public Serializer<?> getSerializer() {
        return BLConversions.VAMPIRE_CONDITION;
    }

    public static VampireConversionCondition vampire() {
        return new VampireConversionCondition(true);
    }

    public static VampireConversionCondition nonVampire() {
        return new VampireConversionCondition(false);
    }
}
