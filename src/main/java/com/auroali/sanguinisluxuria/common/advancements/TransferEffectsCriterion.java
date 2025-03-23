package com.auroali.sanguinisluxuria.common.advancements;

import com.auroali.sanguinisluxuria.BLResources;
import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityEffectPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransferEffectsCriterion extends AbstractCriterion<TransferEffectsCriterion.Conditions> {
    @Override
    protected TransferEffectsCriterion.Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        if (obj.has("effects")) {
            return new Conditions(playerPredicate, EntityEffectPredicate.fromJson(obj.get("effects")), -1);
        }
        return new Conditions(playerPredicate, null, obj.has("min_effects") ? obj.get("min_effects").getAsInt() : 1);
    }

    @Override
    public Identifier getId() {
        return BLResources.TRANSFER_EFFECTS_ID;
    }

    public void trigger(ServerPlayerEntity entity, Collection<StatusEffectInstance> effects) {
        this.trigger(entity, e -> e.test(effects.stream().collect(Collectors.toMap(StatusEffectInstance::getEffectType, Function.identity()))));
    }

    public static class Conditions extends AbstractCriterionConditions {
        final int minCount;
        final EntityEffectPredicate statusEffects;

        public Conditions(LootContextPredicate entity, EntityEffectPredicate effects, int minCount) {
            super(BLResources.TRANSFER_EFFECTS_ID, entity);
            this.minCount = minCount;
            this.statusEffects = effects;
        }

        public boolean test(Map<StatusEffect, StatusEffectInstance> effects) {
            if (this.statusEffects != null)
                return this.statusEffects.test(effects);

            return effects.size() >= this.minCount;
        }

        public static Conditions create() {
            return new Conditions(LootContextPredicate.EMPTY, null, 1);
        }


        public static Conditions create(int minCount) {
            return new Conditions(LootContextPredicate.EMPTY, null, minCount);
        }

        public static Conditions create(EntityEffectPredicate effects) {
            return new Conditions(LootContextPredicate.EMPTY, effects, -1);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject obj = super.toJson(predicateSerializer);
            if (this.statusEffects != null) {
                obj.add("effects", this.statusEffects.toJson());
                return obj;
            }
            obj.addProperty("min_effects", this.minCount);
            return obj;
        }
    }
}
