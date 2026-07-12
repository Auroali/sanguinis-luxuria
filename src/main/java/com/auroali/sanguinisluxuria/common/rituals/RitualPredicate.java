package com.auroali.sanguinisluxuria.common.rituals;

import com.auroali.sanguinisluxuria.common.rituals.predicate.RitualEntityPredicate;
import com.auroali.sanguinisluxuria.common.rituals.predicate.RitualFieldsPredicate;
import com.auroali.sanguinisluxuria.common.rituals.predicate.RitualTypePredicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.loot.condition.LootConditionTypes;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.JsonHelper;

import java.util.Objects;
import java.util.Optional;

public record RitualPredicate(
  Optional<LootContextPredicate> target,
  Optional<LootContextPredicate> initiator,
  Optional<RitualTypePredicate> type,
  Optional<RitualFieldsPredicate> fields
) {
    public static final RitualPredicate EMPTY = new RitualPredicate(
      Optional.empty(),
      Optional.empty(),
      Optional.empty(),
      Optional.empty()
    );

    public boolean test(ServerWorld world, Ritual ritual, RitualParameters parameters) {
        if (this.initiator.isPresent()) {
            if (!parameters.hasInitiator())
                return false;
            LootContext initiatorContext = new LootContext.Builder(
              new LootContextParameterSet.Builder(world)
                .add(LootContextParameters.ORIGIN, parameters.pos().toCenterPos())
                .add(LootContextParameters.THIS_ENTITY, parameters.initiator())
                .build(LootContextTypes.ADVANCEMENT_ENTITY)
            ).build(null);
            return this.initiator.get().test(initiatorContext);
        }
        if (this.target.isPresent()) {
            if (!parameters.hasTarget())
                return false;
            LootContext targetContext = new LootContext.Builder(
              new LootContextParameterSet.Builder(world)
                .add(LootContextParameters.ORIGIN, parameters.pos().toCenterPos())
                .add(LootContextParameters.THIS_ENTITY, parameters.target())
                .build(LootContextTypes.ADVANCEMENT_ENTITY)
            ).build(null);
            return this.target.get().test(targetContext);
        }

        return this.type.map(predicate -> predicate.test(ritual.getType())).orElse(true)
          && this.fields.map(predicate -> predicate.test(ritual)).orElse(true);
    }

    public static RitualPredicate fromJson(AdvancementEntityPredicateDeserializer deserializer, JsonObject object) {
        Optional<LootContextPredicate> target = Optional.empty();
        Optional<LootContextPredicate> initiator = Optional.empty();
        Optional<RitualTypePredicate> type = Optional.empty();
        Optional<RitualFieldsPredicate> fields = Optional.empty();
        if (object.has("target")) {
            LootContextPredicate predicate = LootContextPredicate.fromJson(
              "target",
              deserializer,
              object.get("target"),
              LootContextTypes.ADVANCEMENT_ENTITY
            );
            if (predicate == null)
                throw new JsonParseException("Failed to parse target predicate");
            target = Optional.of(predicate);
        }
        if (object.has("initiator")) {
            LootContextPredicate predicate = LootContextPredicate.fromJson(
              "initiator",
              deserializer,
              object.get("initiator"),
              LootContextTypes.ADVANCEMENT_ENTITY
            );
            if (predicate == null)
                throw new JsonParseException("Failed to parse initiator predicate");
            initiator = Optional.of(predicate);
        }
        if (JsonHelper.hasString(object, "type")) {
            type = Optional.of(RitualTypePredicate.fromJson(object.get("type")));
        }
        if (JsonHelper.hasJsonObject(object, "fields")) {
            fields = Optional.of(RitualFieldsPredicate.fromJson(object.get("fields")));
        }

        RitualPredicate predicate = new RitualPredicate(
          target, initiator, type, fields
        );
        return predicate.equals(EMPTY) ? EMPTY : predicate;
    }

    public JsonElement toJson(AdvancementEntityPredicateSerializer serializer) {
        if (this == EMPTY)
            return JsonNull.INSTANCE;

        JsonObject json = new JsonObject();
        this.initiator.ifPresent(predicate ->
          json.add("initiator", predicate.toJson(serializer))
        );
        this.target.ifPresent(predicate ->
          json.add("target", predicate.toJson(serializer))
        );
        this.type.ifPresent(predicate ->
          json.add("type", predicate.toJson())
        );
        this.fields.ifPresent(predicate ->
          json.add("fields", predicate.toJson())
        );
        return json;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LootContextPredicate initiator;
        private LootContextPredicate target;
        private RitualTypePredicate type;
        private RitualFieldsPredicate fields;

        protected Builder() {
        }

        public Builder initiator(EntityPredicate predicate) {
            return this.initiator(EntityPredicate.asLootContextPredicate(predicate));
        }

        public Builder target(EntityPredicate predicate) {
            return this.target(EntityPredicate.asLootContextPredicate(predicate));
        }

        public Builder initiator(LootContextPredicate predicate) {
            this.initiator = predicate;
            return this;
        }

        public Builder target(LootContextPredicate predicate) {
            this.target = predicate;
            return this;
        }

        public Builder type(RitualTypePredicate type) {
            this.type = type;
            return this;
        }

        public Builder fields(RitualFieldsPredicate fields) {
            this.fields = fields;
            return this;
        }

        public RitualPredicate build() {
            RitualPredicate predicate = new RitualPredicate(
              Optional.ofNullable(this.target),
              Optional.ofNullable(this.initiator),
              Optional.ofNullable(this.type),
              Optional.ofNullable(this.fields)
            );
            return predicate.equals(EMPTY) ? EMPTY : predicate;
        }
    }


}
