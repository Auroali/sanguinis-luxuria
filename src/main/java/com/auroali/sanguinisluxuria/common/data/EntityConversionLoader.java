package com.auroali.sanguinisluxuria.common.data;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionCondition;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionData;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionTransformer;
import com.auroali.sanguinisluxuria.common.events.VampireConversionEvents;
import com.auroali.sanguinisluxuria.util.CachedCodec;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.Collection;
import java.util.Map;

public class EntityConversionLoader extends JsonDataLoader implements IdentifiableResourceReloadListener {
    private static Multimap<EntityType<?>, EntityConversionData> CONVERSIONS;

    public static boolean convertEntity(ConversionContext context) {
        if (CONVERSIONS == null)
            throw new IllegalStateException("Cannot convert an entity before conversions are loaded");

        Collection<EntityConversionData> conversions = CONVERSIONS.get(context.entity().getType());
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

    public EntityConversionLoader(Gson gson) {
        super(gson, "vampire_conversions");
    }

    @Override
    public Identifier getFabricId() {
        return SLResources.CONVERSION_DATA;
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        CONVERSIONS = HashMultimap.create();
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
}
