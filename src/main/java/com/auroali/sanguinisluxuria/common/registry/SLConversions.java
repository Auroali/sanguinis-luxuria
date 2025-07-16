package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.auroali.sanguinisluxuria.common.conversions.*;
import com.auroali.sanguinisluxuria.common.conversions.conditions.AndConversionCondition;
import com.auroali.sanguinisluxuria.common.conversions.conditions.ConversionContextCondition;
import com.auroali.sanguinisluxuria.common.conversions.conditions.OrConversionCondition;
import com.auroali.sanguinisluxuria.common.conversions.conditions.VampireConversionCondition;
import com.auroali.sanguinisluxuria.common.conversions.transformers.ConditionalTransformer;
import com.auroali.sanguinisluxuria.common.conversions.transformers.CopyConversionTransformer;
import com.auroali.sanguinisluxuria.common.conversions.transformers.SetTransformer;
import com.auroali.sanguinisluxuria.common.events.VampireConversionEvents;
import com.auroali.sanguinisluxuria.util.CachedCodec;
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

public class SLConversions extends JsonDataLoader implements IdentifiableResourceReloadListener {
    private static final HashMultimap<EntityType<?>, EntityConversionData> CONVERSIONS = HashMultimap.create();
    private static final Gson GSON = new Gson();

    public static final ConversionType SET_VAMPIRE_TYPE = new VampireSettingConversionType(true);
    public static final ConversionType REVERT_VAMPIRE_TYPE = new VampireSettingConversionType(false);
    public static final ConversionType SPAWN_TYPE = new CreateEntityConversionType();

    public static void register() {
        Registry.register(SLRegistries.CONVERSION_TYPES, SLResources.SET_VAMPIRE_TYPE, SET_VAMPIRE_TYPE);
        Registry.register(SLRegistries.CONVERSION_TYPES, SLResources.REVERT_VAMPIRE_TYPE, REVERT_VAMPIRE_TYPE);
        Registry.register(SLRegistries.CONVERSION_TYPES, SLResources.SPAWN_TYPE, SPAWN_TYPE);
        Registry.register(SLRegistries.CONVERSION_TRANSFORMERS, SLResources.COPY_TRANSFORMER_ID, CopyConversionTransformer.CODEC);
        Registry.register(SLRegistries.CONVERSION_TRANSFORMERS, SLResources.SET_TRANSFORMER_ID, SetTransformer.CODEC);
        Registry.register(SLRegistries.CONVERSION_TRANSFORMERS, SLResources.CONDITIONAL_TRANSFORMER_ID, ConditionalTransformer.CODEC);
        Registry.register(SLRegistries.CONVERSION_CONDITIONS, SLResources.CONVERSION_CONTEXT_CONDITION_ID, ConversionContextCondition.CODEC);
        Registry.register(SLRegistries.CONVERSION_CONDITIONS, SLResources.OR_CONDITION_ID, OrConversionCondition.CODEC);
        Registry.register(SLRegistries.CONVERSION_CONDITIONS, SLResources.AND_CONDITION_ID, AndConversionCondition.CODEC);
        Registry.register(SLRegistries.CONVERSION_CONDITIONS, SLResources.VAMPIRE_CONDITION_ID, VampireConversionCondition.CODEC);

        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
          .registerReloadListener(new SLConversions());
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

    public SLConversions() {
        super(GSON, "vampire_conversions");
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        CONVERSIONS.clear();
        CachedCodec<EntityConversionTransformer> transformerCache = CachedCodec.wrap(EntityConversionTransformer.CODEC);
        CachedCodec<EntityConversionCondition> conditionCache = CachedCodec.wrap(EntityConversionCondition.CODEC);
        prepared.forEach((id, element) -> {
            try {
                EntityConversionData conversionData = EntityConversionData.fromJson(
                  JsonHelper.asObject(element, "top object"),
                  transformerCache,
                  conditionCache
                );
                CONVERSIONS.put(conversionData.getEntity(), conversionData);
            } catch (IllegalArgumentException | JsonParseException e) {
                SanguinisLuxuria.LOGGER.error("Failed to read conversion {}", id, e);
            }
        });
    }

    @Override
    public Identifier getFabricId() {
        return SLResources.CONVERSION_DATA;
    }
}
