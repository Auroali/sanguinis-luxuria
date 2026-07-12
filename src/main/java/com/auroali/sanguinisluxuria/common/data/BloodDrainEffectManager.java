package com.auroali.sanguinisluxuria.common.data;

import com.auroali.sanguinisluxuria.common.blood.BloodDrainEffect;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Either;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public sealed interface BloodDrainEffectManager permits BloodDrainEffectManager.Unresolved, BloodDrainEffectManager.Resolved {
    void apply(LivingEntity target, LivingEntity source);

    final class Unresolved implements BloodDrainEffectManager {
        private final Map<EntityType<?>, List<BloodDrainEffect>> effectsByType;
        private final Map<TagKey<EntityType<?>>, List<BloodDrainEffect>> effectsByTag;

        public Unresolved() {
            this.effectsByType = new HashMap<>();
            this.effectsByTag = new HashMap<>();
        }

        @Override
        public void apply(LivingEntity target, LivingEntity source) {
            throw new IllegalStateException("Cannot apply effects while unresolved");
        }

        public void add(Either<TagKey<EntityType<?>>, EntityType<?>> key, BloodDrainEffect effect) {
            key
              .ifLeft(tag -> this.effectsByTag
                .computeIfAbsent(tag, k -> new ArrayList<>())
                .add(effect)
              )
              .ifRight(type -> this.effectsByType
                .computeIfAbsent(type, k -> new ArrayList<>())
                .add(effect)
              );
        }

        public BloodDrainEffectManager resolve(DynamicRegistryManager manager) {
            Registry<EntityType<?>> entities = manager.get(RegistryKeys.ENTITY_TYPE);
            this.effectsByTag.forEach((tag, effects) -> {
                SLTags.getAllEntriesInTag(tag, entities)
                  .forEach(type -> this.effectsByType
                    .computeIfAbsent(type, k -> new ArrayList<>())
                    .addAll(effects)
                  );
            });

            Multimap<EntityType<?>, BloodDrainEffect> resolvedEffects = HashMultimap.create();
            this.effectsByType.forEach((type, effects) -> {
                List<BloodDrainEffect> mergedEffects = new ArrayList<>(effects.size());
                for (BloodDrainEffect effect : effects) {
                    boolean merged = false;
                    for (int i = 0; i < mergedEffects.size(); i++) {
                        if (effect.canMerge(mergedEffects.get(i))) {
                            mergedEffects.set(i, effect.merge(mergedEffects.get(i)));
                            break;
                        }
                    }
                    if (!merged)
                        mergedEffects.add(effect);
                }
                resolvedEffects.putAll(type, mergedEffects);
            });

            return new Resolved(resolvedEffects);
        }
    }

    record Resolved(Multimap<EntityType<?>, BloodDrainEffect> effects) implements BloodDrainEffectManager {
        @Override
        public void apply(LivingEntity target, LivingEntity source) {
            if (this.effects.containsKey(source.getType())) {
                this.effects.get(source.getType())
                  .forEach(effect -> effect.apply(target));
            }
        }
    }
}
