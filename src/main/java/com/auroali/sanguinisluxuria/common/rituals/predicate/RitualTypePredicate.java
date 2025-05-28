package com.auroali.sanguinisluxuria.common.rituals.predicate;

import com.auroali.sanguinisluxuria.common.registry.SLRegistries;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.Objects;

public class RitualTypePredicate {
    public static final RitualTypePredicate ANY = new RitualTypePredicate(null, false);

    private final RitualType<?> type;
    private final boolean invert;

    protected RitualTypePredicate(RitualType<?> type, boolean invert) {
        this.type = type;
        this.invert = invert;
    }

    public boolean test(RitualType<?> type) {
        if (this == ANY)
            return true;

        return this.invert != Objects.equals(this.type, type);
    }

    public static RitualTypePredicate fromJson(JsonElement element) {
        JsonObject object = element.getAsJsonObject();
        RitualType<?> type = SLRegistries.RITUAL_TYPES.get(new Identifier(JsonHelper.getString(object, "id")));
        boolean invert = JsonHelper.getBoolean(object, "inverted", false);
        if (type == null)
            return ANY;

        return new RitualTypePredicate(type, invert);
    }

    public JsonElement toJson() {
        if (this == ANY)
            return JsonNull.INSTANCE;

        JsonObject object = new JsonObject();
        object.addProperty("id", RitualType.getId(this.type).toString());
        if (this.invert)
            object.addProperty("inverted", true);
        return object;
    }

    public RitualTypePredicate inverted() {
        return this == ANY ? ANY : new RitualTypePredicate(this.type, !this.invert);
    }

    public static RitualTypePredicate create(RitualType<?> type) {
        return new RitualTypePredicate(Objects.requireNonNull(type), false);
    }
}
