package com.auroali.sanguinisluxuria.common.advancements;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ConvertCriterion extends AbstractCriterion<ConvertCriterion.Conditions> {
    @Override
    protected ConvertCriterion.Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        ConversionContext.Conversion conversion = ConversionContext.Conversion.fromJson(obj.get("conversion"));
        return new Conditions(conversion, playerPredicate);
    }

    @Override
    public Identifier getId() {
        return SLResources.CONVERT_CRITERION_ID;
    }

    public void trigger(ServerPlayerEntity entity, ConversionContext.Conversion conversion) {
        this.trigger(entity, conditions -> conditions.conversion == conversion);
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final ConversionContext.Conversion conversion;

        public Conditions(ConversionContext.Conversion conversion, LootContextPredicate entity) {
            super(SLResources.CONVERT_CRITERION_ID, entity);
            this.conversion = conversion;
        }

        public static Conditions create(ConversionContext.Conversion conversion) {
            return new Conditions(conversion, LootContextPredicate.EMPTY);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject obj = super.toJson(predicateSerializer);
            obj.addProperty("conversion", this.conversion.asString());
            return obj;
        }
    }
}
