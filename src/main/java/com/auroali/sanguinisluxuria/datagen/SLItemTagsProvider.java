package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.SLItems;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class SLItemTagsProvider extends FabricTagProvider.ItemTagProvider {
    public SLItemTagsProvider(FabricDataOutput dataGenerator, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, FabricTagProvider.BlockTagProvider blockTagProvider) {
        super(dataGenerator, registriesFuture, blockTagProvider);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(SLTags.Items.FACE_TRINKETS)
          .add(
            SLItems.MASK_1,
            SLItems.MASK_2,
            SLItems.MASK_3
          );
        this.getOrCreateTagBuilder(SLTags.Items.NECKLACE_TRINKETS)
          .add(SLItems.PENDANT_OF_PIERCING);
        this.getOrCreateTagBuilder(SLTags.Items.SUN_BLOCKING_HELMETS)
          .add(
            Items.LEATHER_HELMET,
            Items.CARVED_PUMPKIN
          );
        this.getOrCreateTagBuilder(SLTags.Items.VAMPIRE_MASKS)
          .add(
            SLItems.MASK_1,
            SLItems.MASK_2,
            SLItems.MASK_3
          );
        this.getOrCreateTagBuilder(SLTags.Items.SILVER_INGOTS)
          .add(SLItems.SILVER_INGOT);
        this.getOrCreateTagBuilder(ItemTags.PICKAXES)
          .add(SLItems.SILVER_PICKAXE);
        this.getOrCreateTagBuilder(ItemTags.AXES)
          .add(SLItems.SILVER_AXE);
        this.getOrCreateTagBuilder(ItemTags.SWORDS)
          .add(SLItems.SILVER_SWORD);
        this.getOrCreateTagBuilder(ItemTags.HOES)
          .add(SLItems.SILVER_HOE);
        this.getOrCreateTagBuilder(ItemTags.SHOVELS)
          .add(SLItems.SILVER_SHOVEL);

        this.copy(SLTags.Blocks.DECAYED_LOGS, SLTags.Items.DECAYED_LOGS);
        this.copy(BlockTags.LOGS, ItemTags.LOGS);
        this.copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
        this.copy(SLTags.Blocks.HUNGRY_DECAYED_LOGS, SLTags.Items.HUNGRY_DECAYED_LOGS);

        this.copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        this.copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
        this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        this.copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        this.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
        this.copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
        this.copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
        this.copy(BlockTags.PLANKS, ItemTags.PLANKS);
        this.copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
        this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);

        this.copy(SLTags.Blocks.SILVER_BLOCKS, SLTags.Items.SILVER_BLOCKS);
        this.copy(SLTags.Blocks.SILVER_ORES, SLTags.Items.SILVER_ORES);
        this.copy(SLTags.Blocks.RAW_SILVER_BLOCKS, SLTags.Items.RAW_SILVER_BLOCKS);
        this.copy(ConventionalBlockTags.ORES, ConventionalItemTags.ORES);

        this.getOrCreateTagBuilder(ConventionalItemTags.INGOTS)
          .add(SLItems.SILVER_INGOT);

        this.getOrCreateTagBuilder(SLTags.Items.BLOOD_STORING_BOTTLES)
          .add(Items.GLASS_BOTTLE);
    }
}
