package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.conversions.ConversionType;
import com.auroali.sanguinisluxuria.common.conversions.CreateEntityConversionType;
import com.auroali.sanguinisluxuria.common.conversions.VampireSettingConversionType;
import com.auroali.sanguinisluxuria.common.conversions.conditions.AndConversionCondition;
import com.auroali.sanguinisluxuria.common.conversions.conditions.ConversionContextCondition;
import com.auroali.sanguinisluxuria.common.conversions.conditions.OrConversionCondition;
import com.auroali.sanguinisluxuria.common.conversions.conditions.VampireConversionCondition;
import com.auroali.sanguinisluxuria.common.conversions.transformers.ConditionalTransformer;
import com.auroali.sanguinisluxuria.common.conversions.transformers.CopyConversionTransformer;
import com.auroali.sanguinisluxuria.common.conversions.transformers.SetTransformer;
import net.minecraft.registry.Registry;

public class SLConversions {
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
    }
}
