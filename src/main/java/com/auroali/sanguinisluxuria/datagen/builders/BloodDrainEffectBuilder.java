package com.auroali.sanguinisluxuria.datagen.builders;

import com.auroali.sanguinisluxuria.common.blood.BloodDrainEffect;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BloodDrainEffectBuilder {
    private final Either<Identifier, TagKey<EntityType<?>>> target;
    private final List<BloodDrainEffect> effects;

    private BloodDrainEffectBuilder(Either<Identifier, TagKey<EntityType<?>>> target) {
        this.target = target;
        this.effects = new ArrayList<>();
    }

    public static BloodDrainEffectBuilder builder(Identifier entityId) {
        return new BloodDrainEffectBuilder(Either.left(entityId));
    }

    public static BloodDrainEffectBuilder builder(EntityType<?> type) {
        return new BloodDrainEffectBuilder(Either.left(Registries.ENTITY_TYPE.getId(type)));
    }

    public static BloodDrainEffectBuilder builder(TagKey<EntityType<?>> tag) {
        return new BloodDrainEffectBuilder(Either.right(tag));
    }

    public BloodDrainEffectBuilder effect(BloodDrainEffect... effects) {
        for (BloodDrainEffect effect : effects) {
            boolean merged = false;
            for (int i = 0; i < this.effects.size(); i++) {
                if (effect.canMerge(this.effects.get(i))) {
                    this.effects.set(i, effect.merge(this.effects.get(i)));
                    merged = true;
                    break;
                }
            }
            if (!merged)
                this.effects.add(effect);
        }
        return this;
    }

    public Identifier getId() {
        return this.target
          .left()
          .orElseThrow(() -> new IllegalArgumentException("Cannot determine id automatically for tag-based blood drain effects"));
    }

    public void validate() {
        if (this.effects.isEmpty())
            throw new IllegalArgumentException("Cannot build empty effects");
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("entity", this.target.<String>map(
            Objects::toString,
            tag -> "#" + tag.id().toString()
          )
        );

        obj.add(
          "effects",
          BloodDrainEffect.LIST_CODEC.encodeStart(JsonOps.INSTANCE, this.effects)
            .getOrThrow(true, str -> {
            })
        );

        return obj;
    }
}
