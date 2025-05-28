package com.auroali.sanguinisluxuria.common.advancements;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.rituals.Ritual;
import com.auroali.sanguinisluxuria.common.rituals.RitualParameters;
import com.auroali.sanguinisluxuria.common.rituals.RitualPredicate;
import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class PerformRitualCriterion extends AbstractCriterion<PerformRitualCriterion.Conditions> {
    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(
          RitualPredicate.fromJson(JsonHelper.getObject(obj, "ritual")),
          playerPredicate
        );
    }

    public void trigger(ServerPlayerEntity player, Ritual ritual, RitualParameters parameters) {
        if (parameters.world() instanceof ServerWorld world)
            this.trigger(player, conditions -> conditions.test(world, ritual, parameters));
    }

    @Override
    public Identifier getId() {
        return SLResources.PERFORM_RITUAL_ID;
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final RitualPredicate predicate;

        public Conditions(RitualPredicate predicate, LootContextPredicate entity) {
            super(SLResources.PERFORM_RITUAL_ID, entity);
            this.predicate = predicate;
        }

        public boolean test(ServerWorld world, Ritual ritual, RitualParameters parameters) {
            if (this.predicate == RitualPredicate.ANY)
                return true;

            return this.predicate.test(world, ritual, parameters);
        }

        public static Conditions create() {
            return new Conditions(RitualPredicate.ANY, LootContextPredicate.EMPTY);
        }

        public static Conditions create(RitualPredicate predicate) {
            return new Conditions(predicate, LootContextPredicate.EMPTY);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject object = super.toJson(predicateSerializer);
            object.add("ritual", this.predicate.toJson());
            return object;
        }
    }
}
