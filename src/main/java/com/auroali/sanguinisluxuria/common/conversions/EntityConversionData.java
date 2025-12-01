package com.auroali.sanguinisluxuria.common.conversions;

import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.InitializableBloodComponent;
import com.auroali.sanguinisluxuria.common.events.VampireConversionEvents;
import com.auroali.sanguinisluxuria.common.registry.SLRegistries;
import com.auroali.sanguinisluxuria.util.CachedCodec;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityConversionData {
    protected final ConversionType type;
    protected final EntityType<?> entity;
    protected final EntityType<?> target;
    protected final List<EntityConversionTransformer> transformers;
    protected final List<EntityConversionCondition> conditions;

    public EntityConversionData(ConversionType type, EntityType<?> entity, EntityType<?> target, List<EntityConversionTransformer> transformers, List<EntityConversionCondition> conditions) {
        this.type = type;
        this.entity = entity;
        this.target = target;
        this.transformers = transformers;
        this.conditions = conditions;
    }

    public EntityType<?> getEntity() {
        return this.entity;
    }

    public boolean testConditions(ConversionContext context) {
        for (EntityConversionCondition conditions : this.conditions) {
            if (!conditions.test(context))
                return false;
        }
        return true;
    }

    public void performConversion(ConversionContext context) {
        Entity entity = context.entity();
        World world = context.world();

        NbtCompound entityNbt = new NbtCompound();
        NbtCompound newNbt = new NbtCompound();
        entity.writeNbt(entityNbt);

        this.transformers.forEach(transformer -> transformer.apply(context, entityNbt, newNbt));
        Entity newEntity = this.type.apply(world, entity, this.target, newNbt);
        if (newEntity == entity) {
            VampireConversionEvents.AFTER_CONVERSION.invoker().afterConversion(context, newEntity);
            context.onConverted(newEntity);
            return;
        }

        if (VampireHelper.hasBlood(newEntity) && VampireHelper.hasBlood(entity)) {
            BloodComponent oldBlood = BloodComponent.KEY.get(entity);
            BloodComponent newBlood = BloodComponent.KEY.get(newEntity);
            if (newBlood instanceof InitializableBloodComponent initializable && !initializable.hasInitialized())
                initializable.initializeBloodValues();

            newBlood.setBlood(Math.min(oldBlood.getBlood(), newBlood.getMaxBlood()));
        }

        world.spawnEntity(newEntity);
        entity.remove(Entity.RemovalReason.DISCARDED);

        VampireConversionEvents.AFTER_CONVERSION.invoker().afterConversion(context, newEntity);
        context.onConverted(entity);
    }

    public static EntityConversionData fromJson(JsonObject object, CachedCodec<EntityConversionTransformer> transformerCache, CachedCodec<EntityConversionCondition> conditionCache) {
        if (!object.has("type"))
            throw new JsonParseException("Missing type field");
        if (!object.has("entity"))
            throw new JsonParseException("Missing entity field");
        if (!object.has("target"))
            throw new JsonParseException("Missing target field");
        if (!object.get("target").isJsonPrimitive() || !object.get("target").getAsJsonPrimitive().isString())
            throw new JsonParseException("Expected string for target, got " + object.get("target"));
        if (!object.get("entity").isJsonPrimitive() || !object.get("entity").getAsJsonPrimitive().isString())
            throw new JsonParseException("Expected string for entity, got " + object.get("entity"));

        ConversionType type = SLRegistries.CONVERSION_TYPES.get(Identifier.tryParse(object.get("type").getAsString()));
        if (type == null)
            throw new JsonParseException("Could not get type " + object.get("type"));

        Identifier entityId = Identifier.tryParse(object.get("entity").getAsString());
        if (entityId == null)
            throw new JsonParseException("Failed to parse id " + object.get("entity"));
        if (!Registries.ENTITY_TYPE.containsId(entityId))
            throw new JsonParseException(entityId + " is not a valid entity");

        Identifier targetId = Identifier.tryParse(object.get("target").getAsString());
        if (targetId == null)
            throw new JsonParseException("Failed to parse id " + object.get("target"));
        if (!Registries.ENTITY_TYPE.containsId(targetId))
            throw new JsonParseException(targetId + " is not a valid entity");

        EntityType<?> entity = Registries.ENTITY_TYPE.get(entityId);
        EntityType<?> target = Registries.ENTITY_TYPE.get(targetId);
        // if the transformers field is present, parse it
        List<EntityConversionTransformer> transformers = object.has("transformers") && object.get("transformers").isJsonArray()
          ? parseWithCache(object.getAsJsonArray("transformers"), transformerCache)
          : Collections.emptyList();

        // if the conditions field is present, parse it
        List<EntityConversionCondition> conditions = object.has("conditions") && object.get("conditions").isJsonArray()
          ? parseWithCache(object.getAsJsonArray("conditions"), conditionCache)
          : Collections.emptyList();

        return new EntityConversionData(type, entity, target, transformers, conditions);
    }

    private static <T> List<T> parseWithCache(JsonArray array, CachedCodec<T> cache) {
        List<T> result = new ArrayList<>(array.size());
        for (JsonElement element : array) {
            cache.parse(JsonOps.INSTANCE, element)
              .resultOrPartial(SanguinisLuxuria.LOGGER::error)
              .ifPresent(result::add);
        }
        return result;
    }
}
