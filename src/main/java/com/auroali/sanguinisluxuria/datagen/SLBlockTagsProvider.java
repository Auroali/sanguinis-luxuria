package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.SLBlocks;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class SLBlockTagsProvider extends FabricTagProvider.BlockTagProvider {
    public SLBlockTagsProvider(FabricDataOutput dataGenerator, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataGenerator, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup args) {
        this.getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
          .add(
            SLBlocks.ALTAR,
            SLBlocks.SILVER_ORE,
            SLBlocks.DEEPSLATE_SILVER_ORE,
            SLBlocks.SILVER_BLOCK,
            SLBlocks.RAW_SILVER_BLOCK,
            SLBlocks.SILVER_BARS,
            SLBlocks.SILVER_PRESSURE_PLATE
          );
        this.getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
          .add(
            SLBlocks.SILVER_ORE,
            SLBlocks.DEEPSLATE_SILVER_ORE,
            SLBlocks.SILVER_BLOCK,
            SLBlocks.RAW_SILVER_BLOCK
          );
        this.getOrCreateTagBuilder(BlockTags.CAULDRONS)
          .add(SLBlocks.BLOOD_CAULDRON);
        this.getOrCreateTagBuilder(ConventionalBlockTags.ORES)
          .add(
            SLBlocks.SILVER_ORE,
            SLBlocks.DEEPSLATE_SILVER_ORE
          );
        this.getOrCreateTagBuilder(BlockTags.ALL_SIGNS)
          .add(
            SLBlocks.DECAYED_SIGN,
            SLBlocks.DECAYED_WALL_SIGN
          );
        this.getOrCreateTagBuilder(BlockTags.ALL_HANGING_SIGNS)
          .add(
            SLBlocks.DECAYED_HANGING_SIGN,
            SLBlocks.DECAYED_WALL_HANGING_SIGN
          );
        this.getOrCreateTagBuilder(BlockTags.PLANKS)
          .add(SLBlocks.DECAYED_PLANKS);
        this.getOrCreateTagBuilder(BlockTags.WOODEN_SLABS)
          .add(SLBlocks.DECAYED_SLAB);
        this.getOrCreateTagBuilder(BlockTags.WOODEN_DOORS)
          .add(SLBlocks.DECAYED_DOOR);
        this.getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS)
          .add(SLBlocks.DECAYED_TRAPDOOR);
        this.getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS)
          .add(SLBlocks.DECAYED_BUTTON);
        this.getOrCreateTagBuilder(BlockTags.WOODEN_FENCES)
          .add(SLBlocks.DECAYED_FENCE);
        this.getOrCreateTagBuilder(BlockTags.FENCE_GATES)
          .add(SLBlocks.DECAYED_FENCE_GATE);
        this.getOrCreateTagBuilder(SLTags.Blocks.DECAYED_LOGS)
          .add(
            SLBlocks.DECAYED_WOOD,
            SLBlocks.DECAYED_LOG,
            SLBlocks.STRIPPED_DECAYED_LOG,
            SLBlocks.STRIPPED_DECAYED_WOOD,
            SLBlocks.HUNGRY_DECAYED_LOG,
            SLBlocks.STRIPPED_HUNGRY_DECAYED_LOG
          );
        this.getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES)
          .add(SLBlocks.DECAYED_PRESSURE_PLATE);
        this.getOrCreateTagBuilder(BlockTags.LOGS)
          .add(
            SLBlocks.DECAYED_WOOD,
            SLBlocks.DECAYED_LOG,
            SLBlocks.STRIPPED_DECAYED_LOG,
            SLBlocks.STRIPPED_DECAYED_WOOD,
            SLBlocks.HUNGRY_DECAYED_LOG,
            SLBlocks.STRIPPED_HUNGRY_DECAYED_LOG
          );
        this.getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
          .add(
            SLBlocks.DECAYED_WOOD,
            SLBlocks.DECAYED_LOG,
            SLBlocks.STRIPPED_DECAYED_LOG,
            SLBlocks.STRIPPED_DECAYED_WOOD,
            SLBlocks.HUNGRY_DECAYED_LOG,
            SLBlocks.STRIPPED_HUNGRY_DECAYED_LOG
          );
        this.getOrCreateTagBuilder(SLTags.Blocks.HUNGRY_DECAYED_LOGS)
          .add(
            SLBlocks.HUNGRY_DECAYED_LOG,
            SLBlocks.STRIPPED_HUNGRY_DECAYED_LOG
          );
        this.getOrCreateTagBuilder(BlockTags.PRESSURE_PLATES)
          .add(
            SLBlocks.SILVER_PRESSURE_PLATE,
            SLBlocks.DECAYED_PRESSURE_PLATE
          );
        this.getOrCreateTagBuilder(SLTags.Blocks.SILVER_BLOCKS)
          .add(SLBlocks.SILVER_BLOCK);
        this.getOrCreateTagBuilder(SLTags.Blocks.RAW_SILVER_BLOCKS)
          .add(SLBlocks.RAW_SILVER_BLOCK);
        this.getOrCreateTagBuilder(SLTags.Blocks.SILVER_ORES)
          .add(
            SLBlocks.SILVER_ORE,
            SLBlocks.DEEPSLATE_SILVER_ORE
          );
        this.getOrCreateTagBuilder(BlockTags.SAPLINGS)
          .add(SLBlocks.GRAFTED_SAPLING);
        this.getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS)
          .add(SLBlocks.DECAYED_STAIRS);
        this.getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
          .add(SLBlocks.PEDESTAL);
        this.getOrCreateTagBuilder(SLTags.Blocks.NO_MIST_COLLISION)
          .add(
            Blocks.IRON_BARS,
            Blocks.BAMBOO
          )
          .forceAddTag(BlockTags.DOORS)
          .forceAddTag(BlockTags.TRAPDOORS)
          .forceAddTag(BlockTags.FENCE_GATES)
          .forceAddTag(BlockTags.FENCES)
          .forceAddTag(BlockTags.ALL_HANGING_SIGNS)
          .forceAddTag(BlockTags.BEDS)
          .forceAddTag(BlockTags.ANVIL);
    }
}
