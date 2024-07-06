package com.auroali.sanguinisluxuria.common.advancements;

import com.auroali.sanguinisluxuria.BLResources;
import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class UnbecomeVampireCriterion extends AbstractCriterion<UnbecomeVampireCriterion.Conditions> {
    @Override
    protected UnbecomeVampireCriterion.Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(LootContextPredicate.EMPTY);
    }

    @Override
    public Identifier getId() {
        return BLResources.UNBECOME_VAMPIRE_CRITERION_ID;
    }

    public void trigger(ServerPlayerEntity entity) {
        this.trigger(entity, e -> true);
    }

    public static class Conditions extends AbstractCriterionConditions {

        public Conditions(LootContextPredicate entity) {
            super(BLResources.UNBECOME_VAMPIRE_CRITERION_ID, entity);
        }

        public static Conditions create() {
            return new Conditions(LootContextPredicate.EMPTY);
        }
    }
}
