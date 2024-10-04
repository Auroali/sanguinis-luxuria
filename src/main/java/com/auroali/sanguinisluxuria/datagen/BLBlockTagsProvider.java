package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.BLBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BLBlockTagsProvider extends FabricTagProvider<Block> {
    public BLBlockTagsProvider(FabricDataOutput dataGenerator, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataGenerator, RegistryKeys.BLOCK, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup args) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
          .add(BLBlocks.ALTAR)
          .add(BLBlocks.PEDESTAL)
          .add(BLBlocks.SILVER_ORE)
          .add(BLBlocks.DEEPSLATE_SILVER_ORE)
          .add(BLBlocks.SILVER_BLOCK)
          .add(BLBlocks.RAW_SILVER_BLOCK);
        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
          .add(BLBlocks.SILVER_ORE)
          .add(BLBlocks.DEEPSLATE_SILVER_ORE)
          .add(BLBlocks.SILVER_BLOCK)
          .add(BLBlocks.RAW_SILVER_BLOCK);
        getOrCreateTagBuilder(BlockTags.CAULDRONS)
          .add(BLBlocks.BLOOD_CAULDRON);
        getOrCreateTagBuilder(ConventionalBlockTags.ORES)
          .add(BLBlocks.SILVER_ORE)
          .add(BLBlocks.DEEPSLATE_SILVER_ORE);
    }
}
