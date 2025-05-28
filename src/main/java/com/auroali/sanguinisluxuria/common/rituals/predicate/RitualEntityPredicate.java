package com.auroali.sanguinisluxuria.common.rituals.predicate;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Vec3d;

public class RitualEntityPredicate {
    public static final RitualEntityPredicate ANY = new RitualEntityPredicate(EntityPredicate.ANY, false);
    private final EntityPredicate predicate;
    private final boolean invert;

    public RitualEntityPredicate(EntityPredicate predicate, boolean invert) {
        this.predicate = predicate;
        this.invert = invert;
    }

    public boolean test(ServerWorld world, Vec3d pos, Entity entity) {
        if (this == ANY)
            return true;

        return this.invert != this.predicate.test(world, pos, entity);
    }

    public static RitualEntityPredicate fromJson(JsonElement element) {
        JsonObject object = element.getAsJsonObject();
        EntityPredicate predicate = EntityPredicate.fromJson(object.get("predicate"));
        boolean invert = JsonHelper.getBoolean(object, "inverted", false);
        if (predicate == EntityPredicate.ANY)
            return ANY;

        return new RitualEntityPredicate(predicate, invert);
    }

    public JsonElement toJson() {
        if (this == ANY)
            return JsonNull.INSTANCE;

        JsonObject object = new JsonObject();
        object.add("predicate", this.predicate.toJson());
        if (this.invert)
            object.addProperty("inverted", true);
        return object;
    }

    public RitualEntityPredicate inverted() {
        return new RitualEntityPredicate(this.predicate, !this.invert);
    }

    public static RitualEntityPredicate create(EntityPredicate predicate) {
        if (predicate == EntityPredicate.ANY)
            return ANY;
        return new RitualEntityPredicate(predicate, false);
    }
}
