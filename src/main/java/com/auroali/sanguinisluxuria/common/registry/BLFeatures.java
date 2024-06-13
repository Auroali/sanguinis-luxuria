package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.config.BLConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

import java.util.Arrays;

public class BLFeatures {
    public static final RegistryKey<PlacedFeature> SILVER_ORE = RegistryKey.of(Registry.PLACED_FEATURE_KEY, BLResources.SILVER_ORE_ID);

    public static final ConfiguredFeature<?, ?> SILVER_ORE_CONFIGURED_FEATURE = new ConfiguredFeature<>(
            Feature.ORE, new OreFeatureConfig(
                    Arrays.asList(
                            OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, BLBlocks.SILVER_ORE.getDefaultState()),
                            OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, BLBlocks.DEEPSLATE_SILVER_ORE.getDefaultState())
                    ),
                    6
            )
    );

    public static final PlacedFeature SILVER_ORE_PLACED_FEATURE = new PlacedFeature(RegistryEntry.of(SILVER_ORE_CONFIGURED_FEATURE),
            Arrays.asList(
                    CountPlacementModifier.of(16),
                    HeightRangePlacementModifier.trapezoid(YOffset.aboveBottom(20), YOffset.fixed(40))
            )
    );
    public static void register() {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, BLResources.SILVER_ORE_ID, SILVER_ORE_CONFIGURED_FEATURE);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, BLResources.SILVER_ORE_ID, SILVER_ORE_PLACED_FEATURE);
        if(BLConfig.INSTANCE.generateSilverOre)
            BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, BLFeatures.SILVER_ORE);
    }
}
