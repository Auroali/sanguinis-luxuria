package com.auroali.bloodlust.common.advancements;

import com.auroali.bloodlust.BLResources;
import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class BecomeVampireCriterion extends AbstractCriterion<BecomeVampireCriterion.Conditions> {
    @Override
    protected BecomeVampireCriterion.Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(EntityPredicate.Extended.EMPTY);
    }

    @Override
    public Identifier getId() {
        return BLResources.BECOME_VAMPIRE_CRITERION_ID;
    }

    public void trigger(ServerPlayerEntity entity) {
        this.trigger(entity, e -> true);
    }

    public static class Conditions extends AbstractCriterionConditions {

        public Conditions(EntityPredicate.Extended entity) {
            super(BLResources.BECOME_VAMPIRE_CRITERION_ID, entity);
        }

        public static Conditions create() {
            return new Conditions(EntityPredicate.Extended.EMPTY);
        }
    }
}
