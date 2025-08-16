package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.worldgen.DecayedTwigsDecorator;
import com.auroali.sanguinisluxuria.config.SLConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class SLWorldgen {
    public static final RegistryKey<PlacedFeature> SILVER_ORE = RegistryKey.of(RegistryKeys.PLACED_FEATURE, SLResources.SILVER_ORE_ID);
    public static final RegistryKey<ConfiguredFeature<?, ?>> DECAYED_TREE = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, SLResources.DECAYED_TREE);
    public static final TreeDecoratorType<?> DECAYED_TWIGS_DECORATOR = new TreeDecoratorType<>(DecayedTwigsDecorator.CODEC);

    public static void register() {
        Registry.register(Registries.TREE_DECORATOR_TYPE, SLResources.DECAYED_TWIGS_ID, DECAYED_TWIGS_DECORATOR);

        if (SLConfig.INSTANCE.generateSilverOre)
            BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, SLWorldgen.SILVER_ORE);
    }
}
