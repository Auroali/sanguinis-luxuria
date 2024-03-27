package com.auroali.sanguinisluxuria.common.advancements;

import com.auroali.sanguinisluxuria.BLResources;
import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TransferEffectsCriterion extends AbstractCriterion<TransferEffectsCriterion.Conditions> {
    @Override
    protected TransferEffectsCriterion.Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(EntityPredicate.Extended.EMPTY, obj.has("min_effects") ? obj.get("min_effects").getAsInt() : 1);
    }

    @Override
    public Identifier getId() {
        return BLResources.TRANSFER_EFFECTS_ID;
    }

    public void trigger(ServerPlayerEntity entity, int numEffects) {
        this.trigger(entity, e -> numEffects >= e.minCount);
    }

    public static class Conditions extends AbstractCriterionConditions {
        final int minCount;
        public Conditions(EntityPredicate.Extended entity, int minCount) {
            super(BLResources.TRANSFER_EFFECTS_ID, entity);
            this.minCount = minCount;
        }

        public static Conditions create() {
            return new Conditions(EntityPredicate.Extended.EMPTY, 1);
        }


        public static Conditions create(int minCount) {
            return new Conditions(EntityPredicate.Extended.EMPTY, minCount);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject obj = super.toJson(predicateSerializer);
            obj.addProperty("min_effects", minCount);
            return obj;
        }
    }
}
