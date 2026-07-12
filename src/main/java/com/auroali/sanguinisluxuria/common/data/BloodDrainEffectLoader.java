package com.auroali.sanguinisluxuria.common.data;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.auroali.sanguinisluxuria.common.blood.BloodDrainEffect;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.List;
import java.util.Map;

public class BloodDrainEffectLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    private static BloodDrainEffectManager MANAGER;

    public static void init() {
        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
            if (!client && MANAGER instanceof BloodDrainEffectManager.Unresolved unresolved) {
                MANAGER = unresolved.resolve(registries);
            }
        });
    }

    public static BloodDrainEffectManager getManager() {
        return MANAGER;
    }

    private Either<TagKey<EntityType<?>>, EntityType<?>> parseTagOrType(String value) {
        if (value.startsWith("#")) {
            Identifier id = new Identifier(value.substring(1));
            return Either.left(TagKey.of(RegistryKeys.ENTITY_TYPE, id));
        }

        Identifier entityId = new Identifier(value);
        if (!Registries.ENTITY_TYPE.containsId(entityId))
            throw new JsonParseException("Unknown entity '" + entityId + "'");

        return Either.right(Registries.ENTITY_TYPE.get(entityId));
    }

    public BloodDrainEffectLoader(Gson gson) {
        super(gson, "blood_drain_effects");
    }

    @Override
    public Identifier getFabricId() {
        return SLResources.BLOOD_DRAIN_EFFECTS;
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        BloodDrainEffectManager.Unresolved effectManager = new BloodDrainEffectManager.Unresolved();
        prepared.forEach((id, element) -> {
            try {
                JsonObject object = JsonHelper.asObject(element, "top object");
                Either<TagKey<EntityType<?>>, EntityType<?>> target = this.parseTagOrType(JsonHelper.getString(object, "entity"));
                List<BloodDrainEffect> effects = BloodDrainEffect.LIST_CODEC.decode(JsonOps.INSTANCE, JsonHelper.getArray(object, "effects"))
                  .getOrThrow(true, SanguinisLuxuria.LOGGER::error)
                  .getFirst();

                effects.forEach(effect ->
                  effectManager.add(target, effect)
                );
            } catch (RuntimeException e) {
                SanguinisLuxuria.LOGGER.error("Error loading blood drain effect {}", id, e);
            }
        });
        MANAGER = effectManager;
    }
}
