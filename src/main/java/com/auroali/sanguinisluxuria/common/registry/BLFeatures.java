package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.config.BLConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;

import java.util.Arrays;

public class BLFeatures {
    public static final RegistryKey<PlacedFeature> SILVER_ORE = RegistryKey.of(RegistryKeys.PLACED_FEATURE, BLResources.SILVER_ORE_ID);

    public static void register() {
        if(BLConfig.INSTANCE.generateSilverOre)
            BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, BLFeatures.SILVER_ORE);
    }
}
