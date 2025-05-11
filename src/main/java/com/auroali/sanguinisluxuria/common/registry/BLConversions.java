package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.Bloodlust;
import com.auroali.sanguinisluxuria.common.conversions.*;
import com.auroali.sanguinisluxuria.common.conversions.conditions.*;
import com.auroali.sanguinisluxuria.common.conversions.transformers.ConditionalTransformer;
import com.auroali.sanguinisluxuria.common.conversions.transformers.CopyConversionTransformer;
import com.auroali.sanguinisluxuria.common.conversions.transformers.SetTransformer;
import com.auroali.sanguinisluxuria.common.events.VampireConversionEvents;
import com.google.common.collect.HashMultimap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registry;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;
import java.util.Set;

public class BLConversions extends JsonDataLoader implements IdentifiableResourceReloadListener {
    private static final HashMultimap<EntityType<?>, EntityConversionData> CONVERSIONS = HashMultimap.create();
    private static final Gson GSON = new Gson();

    public static final ConversionType SET_VAMPIRE_TYPE = new VampireSettingConversionType(true);
    public static final ConversionType REVERT_VAMPIRE_TYPE = new VampireSettingConversionType(false);
    public static final ConversionType SPAWN_TYPE = new CreateEntityConversionType();

    public static final EntityConversionTransformer.Serializer<?> COPY_TRANSFORMER = new EntityConversionTransformer.Serializer<>(CopyConversionTransformer::fromJson);
    public static final EntityConversionTransformer.Serializer<?> SET_TRANSFORMER = new EntityConversionTransformer.Serializer<>(SetTransformer::fromJson);
    public static final EntityConversionTransformer.Serializer<?> CONDITIONAL_TRANSFORMER = new EntityConversionTransformer.Serializer<>(ConditionalTransformer::fromJson);

    public static final EntityConversionCondition.Serializer<?> CONVERSION_CONTEXT_CONDITION = new EntityConversionCondition.Serializer<>(ConversionContextCondition::fromJson);
    public static final EntityConversionCondition.Serializer<?> OR_CONDITION = new EntityConversionCondition.Serializer<>(json -> CompositeConversionCondition.fromJson(json, OrConversionCondition::new));
    public static final EntityConversionCondition.Serializer<?> AND_CONDITION = new EntityConversionCondition.Serializer<EntityConversionCondition>(json -> CompositeConversionCondition.fromJson(json, AndConversionCondition::new));
    public static final EntityConversionCondition.Serializer<?> VAMPIRE_CONDITION = new EntityConversionCondition.Serializer<>(VampireConversionCondition::fromJson);

    public static void register() {
        Registry.register(BLRegistries.CONVERSION_TYPES, BLResources.SET_VAMPIRE_TYPE, SET_VAMPIRE_TYPE);
        Registry.register(BLRegistries.CONVERSION_TYPES, BLResources.REVERT_VAMPIRE_TYPE, REVERT_VAMPIRE_TYPE);
        Registry.register(BLRegistries.CONVERSION_TYPES, BLResources.SPAWN_TYPE, SPAWN_TYPE);
        Registry.register(BLRegistries.CONVERSION_TRANSFORMERS, BLResources.COPY_TRANSFORMER_ID, COPY_TRANSFORMER);
        Registry.register(BLRegistries.CONVERSION_TRANSFORMERS, BLResources.SET_TRANSFORMER_ID, SET_TRANSFORMER);
        Registry.register(BLRegistries.CONVERSION_TRANSFORMERS, BLResources.CONDITIONAL_TRANSFORMER_ID, CONDITIONAL_TRANSFORMER);
        Registry.register(BLRegistries.CONVERSION_CONDITIONS, BLResources.CONVERSION_CONTEXT_CONDITION_ID, CONVERSION_CONTEXT_CONDITION);
        Registry.register(BLRegistries.CONVERSION_CONDITIONS, BLResources.OR_CONDITION_ID, OR_CONDITION);
        Registry.register(BLRegistries.CONVERSION_CONDITIONS, BLResources.AND_CONDITION_ID, AND_CONDITION);
        Registry.register(BLRegistries.CONVERSION_CONDITIONS, BLResources.VAMPIRE_CONDITION_ID, VAMPIRE_CONDITION);

        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
          .registerReloadListener(new BLConversions());
    }

    public static boolean convertEntity(ConversionContext context) {
        Set<EntityConversionData> conversions = CONVERSIONS.get(context.entity().getType());
        if (conversions.isEmpty())
            return false;

        if (!VampireConversionEvents.ALLOW_CONVERSION.invoker().allowConversion(context))
            return false;

        for (EntityConversionData conversion : conversions) {
            if (!conversion.testConditions(context))
                continue;

            conversion.performConversion(context);
            return true;
        }
        return false;
    }

    public BLConversions() {
        super(GSON, "vampire_conversions");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        CONVERSIONS.clear();
        prepared.forEach((id, element) -> {
            try {
                EntityConversionData conversionData = EntityConversionData.fromJson(JsonHelper.asObject(element, "top object"));
                CONVERSIONS.put(conversionData.getEntity(), conversionData);
            } catch (IllegalArgumentException | JsonParseException e) {
                Bloodlust.LOGGER.error("Failed to read conversion {}", id, e);
            }
        });
    }

    @Override
    public Identifier getFabricId() {
        return BLResources.CONVERSION_DATA;
    }
}
