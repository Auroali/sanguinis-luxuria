package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.blood.BloodDrainEffect;
import com.auroali.sanguinisluxuria.common.conversions.ConversionType;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionCondition;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionTransformer;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.mojang.serialization.Codec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class SLRegistryKeys {
    public static final RegistryKey<Registry<VampireAbility>> VAMPIRE_ABILITIES = RegistryKey.ofRegistry(SLResources.VAMPIRE_ABILITY_REGISTRY_ID);
    public static final RegistryKey<Registry<RitualType<?>>> RITUAL_TYPES = RegistryKey.ofRegistry(SLResources.RITUAL_TYPE_REGISTRY_ID);
    public static final RegistryKey<Registry<Codec<? extends EntityConversionTransformer>>> CONVERSION_TRANSFORMERS = RegistryKey.ofRegistry(SLResources.CONVERSION_TRANSFORMERS);
    public static final RegistryKey<Registry<Codec<? extends EntityConversionCondition>>> CONVERSION_CONDITIONS = RegistryKey.ofRegistry(SLResources.CONVERSION_CONDITIONS);
    public static final RegistryKey<Registry<ConversionType>> CONVERSION_TYPES = RegistryKey.ofRegistry(SLResources.CONVERSION_TYPES);
    public static final RegistryKey<Registry<Codec<? extends BloodDrainEffect>>> BLOOD_DRAIN_EFFECTS = RegistryKey.ofRegistry(SLResources.BLOOD_DRAIN_EFFECTS);
}
