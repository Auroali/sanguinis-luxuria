package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.Bloodlust;
import com.auroali.sanguinisluxuria.common.blood.BloodDrainEffectInstance;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class BLBloodDrainEffects extends JsonDataLoader implements IdentifiableResourceReloadListener {
    private static Map<EntityType<?>, List<BloodDrainEffectInstance>> EFFECT_MAP = Collections.emptyMap();
    private static final TagResolvingMapBuilder<EntityType<?>, List<BloodDrainEffectInstance>> RESOLVER = new TagResolvingMapBuilder<>(
      HashMap::new,
      BLEntityBloodDrainEffects::mergeEffects
    );
    private static final Gson GSON = new Gson();

    public BLBloodDrainEffects() {
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
            effect.apply(entity);
        });
    }

    public static void init() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
          .registerReloadListener(new BLBloodDrainEffects());

        Registry.register(BLRegistries.BLOOD_DRAIN_EFFECTS, BLResources.STATUS_EFFECT_ID, BloodDrainStatusEffect.CODEC);
        Registry.register(BLRegistries.BLOOD_DRAIN_EFFECTS, BLResources.TELEPORT_ID, BloodDrainTeleportEffect.CODEC);
        Registry.register(BLRegistries.BLOOD_DRAIN_EFFECTS, BLResources.IGNITE_EFFECT_ID, BloodDrainIgniteEffect.CODEC);
        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
            if (!client)
                resolveReferences();
        });
    }

    @Override
    public Identifier getFabricId() {
        return BLResources.BLOOD_DRAIN_EFFECTS;
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        prepared.forEach((id, element) -> {
            try {
                JsonObject object = JsonHelper.asObject(element, "top object");
                UNRESOLVED_EFFECTS.add(LoadedEffects.fromJson(object));
            } catch (IllegalArgumentException | JsonParseException e) {
                Bloodlust.LOGGER.error("Error loading blood drain effect {}", id, e);
            }
        });

    }

    private static List<BloodDrainEffectInstance> mergeEffects(List<BloodDrainEffectInstance> from, List<BloodDrainEffectInstance> to) {
        List<BloodDrainEffectInstance> result = new ArrayList<>(to);

        for (BloodDrainEffectInstance effect : from) {
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

    /**
     * Intermediate representation of a list of blood drain effects
     * <br> Required because tags aren't loaded until after effects are, so the tag references can't be resolved until after
     * datapack reload
     *
     * @param targets either a direct entity type reference, or a tag
     * @param effects the list of effects
     */
    private record LoadedEffects(Either<TagKey<EntityType<?>>, EntityType<?>> targets,
                                 List<BloodDrainEffect> effects) {
        List<EntityType<?>> resolveTargets() {
            return this.targets().map(
              tag -> BLTags.getAllEntriesInTag(tag, Registries.ENTITY_TYPE),
              Collections::singletonList
            );
        }

        public static LoadedEffects fromJson(JsonObject object) {
            // handle effects
            List<BloodDrainEffect> effects = new ArrayList<>();
            for (JsonElement element : object.getAsJsonArray("effects")) {
                if (!element.isJsonObject())
                    throw new JsonParseException("Expected json object but got " + element);

                BloodDrainEffect.CODEC.parse(JsonOps.INSTANCE, element)
                  .resultOrPartial(Bloodlust.LOGGER::error)
                  .ifPresent(effects::add);
            }

            Either<TagKey<EntityType<?>>, EntityType<?>> targets;

            // handle parsing target
            String targetString = object.get("entity").getAsString();
            if (targetString.startsWith("#")) {
                Identifier id = Identifier.tryParse(targetString.substring(1));
                if (id == null)
                    throw new JsonParseException("Failed to parse id " + targetString + " for tag");
                TagKey<EntityType<?>> tag = TagKey.of(RegistryKeys.ENTITY_TYPE, id);
                targets = Either.left(tag);
            } else {
                Identifier id = Identifier.tryParse(targetString);
                if (id == null)
                    throw new JsonParseException("Failed to parse id " + targetString + " for entity");
                if (!Registries.ENTITY_TYPE.containsId(id))
                    throw new JsonParseException(id + " is not a valid entity");

                EntityType<?> type = Registries.ENTITY_TYPE.get(id);
                targets = Either.right(type);
            }

            return new LoadedEffects(targets, effects);
        }
    }
}
