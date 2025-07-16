package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.auroali.sanguinisluxuria.common.blood.BloodDrainEffect;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainIgniteEffect;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainStatusEffect;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainTeleportEffect;
import com.auroali.sanguinisluxuria.util.TagResolvingMapBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
                EFFECT_MAP = RESOLVER.resolveAndBuild(Registries.ENTITY_TYPE);
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
        prepared.forEach((id, element) -> {
            try {
                JsonObject object = JsonHelper.asObject(element, "top object");
                TAG_READER.parse(JsonOps.INSTANCE, object.get("entity"))
                  .resultOrPartial(SanguinisLuxuria.LOGGER::error)
                  .ifPresent(target -> {
                      List<BloodDrainEffect> effects = BloodDrainEffect.LIST_CODEC
                        .parse(JsonOps.INSTANCE, object.get("effects"))
                        .getOrThrow(true, SanguinisLuxuria.LOGGER::error);

                      target.ifLeft(tag -> RESOLVER.add(tag, effects))
                        .ifRight(entity -> RESOLVER.add(entity, effects));
                  });
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
}
