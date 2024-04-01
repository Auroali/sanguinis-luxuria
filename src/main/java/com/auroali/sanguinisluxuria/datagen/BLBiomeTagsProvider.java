package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.BLTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class BLBiomeTagsProvider extends FabricTagProvider.DynamicRegistryTagProvider<Biome> {
    public BLBiomeTagsProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator, Registry.BIOME_KEY);
    }

    @Override
    protected void generateTags() {
        getOrCreateTagBuilder(BLTags.Biomes.VAMPIRE_VILLAGER_SPAWN)
                .add(BiomeKeys.DARK_FOREST);
    }
}
