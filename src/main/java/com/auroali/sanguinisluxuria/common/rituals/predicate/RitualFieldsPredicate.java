package com.auroali.sanguinisluxuria.common.rituals.predicate;

import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.auroali.sanguinisluxuria.common.rituals.Ritual;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.JsonHelper;

import java.util.HashMap;
import java.util.Map;

public class RitualFieldsPredicate {
    public static final RitualFieldsPredicate ANY = new RitualFieldsPredicate(null, false);
    private final Map<String, JsonElement> fields;
    private final boolean invert;

    protected RitualFieldsPredicate(Map<String, JsonElement> fields, boolean invert) {
        this.fields = fields;
        this.invert = invert;
    }

    public boolean test(Ritual ritual) {
        if (this == ANY)
            return true;

        return Ritual.RITUAL_CODEC.encodeStart(JsonOps.INSTANCE, ritual)
          .resultOrPartial(SanguinisLuxuria.LOGGER::error)
          .map(json -> {
              if (!json.isJsonObject())
                  return false;

              JsonObject object = json.getAsJsonObject();
              for (Map.Entry<String, JsonElement> entry : this.fields.entrySet()) {
                  if (this.invert == object.has(entry.getKey()) || this.invert == object.get(entry.getKey()).equals(entry.getValue()))
                      return false;
              }
              return true;
          })
          .orElse(false);
    }

    public static RitualFieldsPredicate fromJson(JsonElement element) {
        JsonObject object = element.getAsJsonObject();
        Map<String, JsonElement> fields = JsonHelper.getObject(object, "fields").asMap();
        boolean invert = JsonHelper.getBoolean(object, "inverted", false);
        if (fields.isEmpty())
            return ANY;

        return new RitualFieldsPredicate(fields, invert);
    }

    public JsonElement toJson() {
        if (this == ANY)
            return JsonNull.INSTANCE;

        JsonObject object = new JsonObject();
        JsonObject fieldsJson = new JsonObject();
        for (Map.Entry<String, JsonElement> field : this.fields.entrySet()) {
            fieldsJson.add(field.getKey(), field.getValue());
        }
        object.add("fields", fieldsJson);
        if (this.invert)
            object.addProperty("inverted", true);
        return object;
    }

    public RitualFieldsPredicate inverted() {
        return this == ANY ? ANY : new RitualFieldsPredicate(this.fields, !this.invert);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<String, JsonElement> fields;

        protected Builder() {
            this.fields = new HashMap<>();
        }

        public Builder field(String name, JsonElement value) {
            this.fields.put(name, value);
            return this;
        }

        public Builder field(String name, byte value) {
            return this.field(name, new JsonPrimitive(value));
        }

        public Builder field(String name, short value) {
            return this.field(name, new JsonPrimitive(value));
        }

        public Builder field(String name, int value) {
            return this.field(name, new JsonPrimitive(value));
        }

        public Builder field(String name, long value) {
            return this.field(name, new JsonPrimitive(value));
        }

        public Builder field(String name, float value) {
            return this.field(name, new JsonPrimitive(value));
        }

        public Builder field(String name, double value) {
            return this.field(name, new JsonPrimitive(value));
        }

        public Builder field(String name, boolean value) {
            return this.field(name, new JsonPrimitive(value));
        }

        public Builder field(String name, String value) {
            return this.field(name, new JsonPrimitive(value));
        }

        public RitualFieldsPredicate build() {
            if (this.fields.isEmpty())
                return ANY;
            return new RitualFieldsPredicate(this.fields, false);
        }
    }
}
