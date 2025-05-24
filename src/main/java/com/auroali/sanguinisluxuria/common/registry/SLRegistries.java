package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.blood.BloodDrainEffect;
import com.auroali.sanguinisluxuria.common.conversions.ConversionType;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionCondition;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionTransformer;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleRegistry;

public class SLRegistries {
    public static final Registry<VampireAbility> VAMPIRE_ABILITIES = FabricRegistryBuilder.from(
        new SimpleRegistry<>(
          SLRegistryKeys.VAMPIRE_ABILITIES,
          Lifecycle.stable(),
          true
        )
      )
      .attribute(RegistryAttribute.SYNCED)
      .buildAndRegister();
    public static final Registry<RitualType<?>> RITUAL_TYPES = FabricRegistryBuilder
      .createSimple(SLRegistryKeys.RITUAL_TYPES)
      .buildAndRegister();
    public static final Registry<ConversionType> CONVERSION_TYPES = FabricRegistryBuilder
      .createSimple(SLRegistryKeys.CONVERSION_TYPES)
      .buildAndRegister();
    public static final Registry<Codec<? extends EntityConversionTransformer>> CONVERSION_TRANSFORMERS = FabricRegistryBuilder
      .createSimple(SLRegistryKeys.CONVERSION_TRANSFORMERS)
      .buildAndRegister();
    public static final Registry<Codec<? extends EntityConversionCondition>> CONVERSION_CONDITIONS = FabricRegistryBuilder
      .createSimple(SLRegistryKeys.CONVERSION_CONDITIONS)
      .buildAndRegister();
    public static final Registry<Codec<? extends BloodDrainEffect>> BLOOD_DRAIN_EFFECTS = FabricRegistryBuilder
      .createSimple(SLRegistryKeys.BLOOD_DRAIN_EFFECTS)
      .buildAndRegister();

    // called to cause the class the load
    @SuppressWarnings({ "unused", "EmptyMethod" })
    public static void init() {
    }
}
