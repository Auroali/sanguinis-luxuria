package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.auroali.sanguinisluxuria.common.blood.BloodDrainEffect;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainIgniteEffect;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainStatusEffect;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainTeleportEffect;
import com.auroali.sanguinisluxuria.util.TagResolvingMapBuilder;
import com.google.gson.*;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.*;

public class SLBloodDrainEffects extends JsonDataLoader implements IdentifiableResourceReloadListener {
    private static Map<EntityType<?>, List<BloodDrainEffect>> EFFECT_MAP = Collections.emptyMap();
    private static final TagResolvingMapBuilder<EntityType<?>, List<BloodDrainEffect>> RESOLVER = new TagResolvingMapBuilder<>(
      HashMap::new,
      SLBloodDrainEffects::mergeEffects
    );
    private static final Gson GSON = new Gson();
    private static final Codec<Either<TagKey<EntityType<?>>, EntityType<?>>> TAG_READER = Codec.either(
      TagKey.codec(RegistryKeys.ENTITY_TYPE),
      Registries.ENTITY_TYPE.getCodec()
    );

    public SLBloodDrainEffects() {
        super(GSON, "blood_drain_effects");
    }

    public static List<BloodDrainEffect> getFor(EntityType<?> type) {
        return EFFECT_MAP.get(type);
    }

    public static void applyTo(LivingEntity drainer, LivingEntity entity) {
        List<BloodDrainEffect> effects = getFor(entity.getType());
        if (effects == null)
            return;

        effects.forEach(effect -> {
            effect.apply(drainer);
        });
    }

    public static void init() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
          .registerReloadListener(new SLBloodDrainEffects());
        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
            if (!client)
                EFFECT_MAP = RESOLVER.resolveAndBuild(registries.get(RegistryKeys.ENTITY_TYPE));
        });

        Registry.register(SLRegistries.BLOOD_DRAIN_EFFECTS, SLResources.STATUS_EFFECT_ID, BloodDrainStatusEffect.CODEC);
        Registry.register(SLRegistries.BLOOD_DRAIN_EFFECTS, SLResources.TELEPORT_ID, BloodDrainTeleportEffect.CODEC);
        Registry.register(SLRegistries.BLOOD_DRAIN_EFFECTS, SLResources.IGNITE_EFFECT_ID, BloodDrainIgniteEffect.CODEC);
    }

    @Override
    public Identifier getFabricId() {
        return SLResources.BLOOD_DRAIN_EFFECTS;
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        RESOLVER.clear();
        prepared.forEach((id, element) -> {
            try {
                JsonObject object = JsonHelper.asObject(element, "top object");
                Either<TagKey<EntityType<?>>, EntityType<?>> target = this.parseTagOrType(JsonHelper.getString(object, "entity"));
                List<BloodDrainEffect> effects = BloodDrainEffect.LIST_CODEC.decode(JsonOps.INSTANCE, JsonHelper.getArray(object, "effects"))
                  .getOrThrow(true, SanguinisLuxuria.LOGGER::error)
                  .getFirst();

                target
                  .ifLeft(tag -> RESOLVER.add(tag, effects))
                  .ifRight(type -> RESOLVER.add(type, effects));
            } catch (RuntimeException e) {
                SanguinisLuxuria.LOGGER.error("Error loading blood drain effect {}", id, e);
            }
        });

    }

    private static List<BloodDrainEffect> mergeEffects(List<BloodDrainEffect> from, List<BloodDrainEffect> to) {
        List<BloodDrainEffect> result = new ArrayList<>(to);

        for (BloodDrainEffect effect : from) {
            boolean hasMerged = false;
            for (int i = 0; i < to.size(); i++) {
                BloodDrainEffect existing = to.get(i);
                if (!effect.canMerge(existing))
                    continue;
                to.set(i, effect.merge(existing));
                hasMerged = true;
            }
            if (hasMerged)
                continue;

            to.add(effect);
        }

        return result;
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
}
