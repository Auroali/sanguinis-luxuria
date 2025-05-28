package com.auroali.sanguinisluxuria.common.rituals;

import com.auroali.sanguinisluxuria.common.rituals.predicate.RitualEntityPredicate;
import com.auroali.sanguinisluxuria.common.rituals.predicate.RitualFieldsPredicate;
import com.auroali.sanguinisluxuria.common.rituals.predicate.RitualTypePredicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.JsonHelper;

import java.util.Objects;

public class RitualPredicate {
    public static final RitualPredicate ANY = new RitualPredicate(
      RitualEntityPredicate.ANY,
      RitualEntityPredicate.ANY,
      RitualTypePredicate.ANY,
      RitualFieldsPredicate.ANY
    );
    private final RitualEntityPredicate initiator;
    private final RitualEntityPredicate target;
    private final RitualTypePredicate type;
    private final RitualFieldsPredicate fields;

    protected RitualPredicate(RitualEntityPredicate target, RitualEntityPredicate initiator, RitualTypePredicate type, RitualFieldsPredicate fields) {
        this.target = target;
        this.initiator = initiator;
        this.type = type;
        this.fields = fields;
    }

    public boolean test(ServerWorld world, Ritual ritual, RitualParameters parameters) {
        if (this.initiator.test(world, parameters.pos().toCenterPos(), parameters.initiator()))
            return false;
        if (this.target.test(world, parameters.pos().toCenterPos(), parameters.target()))
            return false;
        if (this.type.test(ritual.getType()))
            return false;
        return this.fields.test(ritual);
    }

    public static RitualPredicate fromJson(JsonObject object) {
        RitualEntityPredicate target = RitualEntityPredicate.ANY;
        RitualEntityPredicate initator = RitualEntityPredicate.ANY;
        RitualTypePredicate type = RitualTypePredicate.ANY;
        RitualFieldsPredicate fields = RitualFieldsPredicate.ANY;
        if (object.has("target")) {
            target = RitualEntityPredicate.fromJson(object.get("target"));
        }
        if (object.has("initiator")) {
            initator = RitualEntityPredicate.fromJson(object.get("initiator"));
        }
        if (JsonHelper.hasString(object, "type")) {
            type = RitualTypePredicate.fromJson(object.get("type"));
        }
        if (JsonHelper.hasJsonObject(object, "fields")) {
            fields = RitualFieldsPredicate.fromJson(object.get("fields"));
        }

        RitualPredicate predicate = new RitualPredicate(
          target, initator, type, fields
        );
        return predicate.equals(ANY) ? ANY : predicate;
    }

    public JsonElement toJson() {
        if (this == ANY)
            return JsonNull.INSTANCE;

        JsonObject json = new JsonObject();
        if (this.initiator != null) {
            json.add("initiator", this.initiator.toJson());
        }
        if (this.target != null) {
            json.add("target", this.target.toJson());
        }
        if (this.type != null) {
            json.add("type", this.type.toJson());
        }
        if (this.fields != null) {
            json.add("fields", this.fields.toJson());
        }
        return json;
    }

    @Override
    public int hashCode() {
        int code = 31;
        if (this.target != RitualEntityPredicate.ANY)
            code = this.target.hashCode() + 31 * code;
        if (this.initiator != RitualEntityPredicate.ANY)
            code = this.initiator.hashCode() + 31 * code + 3;
        if (this.type != RitualTypePredicate.ANY)
            code = this.type.hashCode() + 7 * code + 41;
        if (this.fields != RitualFieldsPredicate.ANY)
            code = this.fields.hashCode() + 17 * code;

        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof RitualPredicate predicate) {
            return Objects.equals(this.initiator, predicate.initiator)
              && Objects.equals(this.target, predicate.target)
              && Objects.equals(this.type, predicate.type)
              && Objects.equals(this.fields, predicate.fields);
        }

        return false;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RitualEntityPredicate initiator;
        private RitualEntityPredicate target;
        private RitualTypePredicate type;
        private RitualFieldsPredicate fields;

        protected Builder() {

        }

        public Builder initiator(EntityPredicate predicate) {
            this.initiator = RitualEntityPredicate.create(predicate);
            return this;
        }

        public Builder target(EntityPredicate predicate) {
            this.target = RitualEntityPredicate.create(predicate);
            return this;
        }

        public Builder initiatorExcluding(EntityPredicate predicate) {
            this.initiator = RitualEntityPredicate.create(predicate).inverted();
            return this;
        }

        public Builder targetExcluding(EntityPredicate predicate) {
            this.target = RitualEntityPredicate.create(predicate).inverted();
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
              this.target, this.initiator, this.type, this.fields
            );
            return predicate.equals(ANY) ? ANY : predicate;
        }
    }


}
