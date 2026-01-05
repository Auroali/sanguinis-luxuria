package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.SLItems;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class SLItemTagsProvider extends FabricTagProvider.ItemTagProvider {
    public SLItemTagsProvider(FabricDataOutput dataGenerator, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataGenerator, registriesFuture);
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
        this.getOrCreateTagBuilder(ConventionalItemTags.ORES)
          .add(
            SLItems.SILVER_ORE,
            SLItems.DEEPSLATE_SILVER_ORE
          );
        this.getOrCreateTagBuilder(SLTags.Items.DECAYED_LOGS)
          .add(
            SLItems.DECAYED_WOOD,
            SLItems.DECAYED_LOG,
            SLItems.STRIPPED_DECAYED_LOG,
            SLItems.STRIPPED_DECAYED_WOOD,
            SLItems.HUNGRY_DECAYED_LOG,
            SLItems.STRIPPED_HUNGRY_DECAYED_LOG
          );
        this.getOrCreateTagBuilder(ItemTags.LOGS)
          .add(
            SLItems.DECAYED_WOOD,
            SLItems.DECAYED_LOG,
            SLItems.STRIPPED_DECAYED_LOG,
            SLItems.STRIPPED_DECAYED_WOOD,
            SLItems.HUNGRY_DECAYED_LOG,
            SLItems.STRIPPED_HUNGRY_DECAYED_LOG
          );
        this.getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
          .add(
            SLItems.DECAYED_WOOD,
            SLItems.DECAYED_LOG,
            SLItems.STRIPPED_DECAYED_LOG,
            SLItems.STRIPPED_DECAYED_WOOD,
            SLItems.HUNGRY_DECAYED_LOG,
            SLItems.STRIPPED_HUNGRY_DECAYED_LOG
          );
        this.getOrCreateTagBuilder(SLTags.Items.HUNGRY_DECAYED_LOGS)
          .add(
            SLItems.HUNGRY_DECAYED_LOG,
            SLItems.STRIPPED_HUNGRY_DECAYED_LOG
          );
        this.getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES)
          .add(SLItems.DECAYED_PRESSURE_PLATE);
        this.getOrCreateTagBuilder(SLTags.Items.SILVER_BLOCKS)
          .add(SLItems.SILVER_BLOCK);
        this.getOrCreateTagBuilder(SLTags.Items.RAW_SILVER_BLOCKS)
          .add(SLItems.RAW_SILVER_BLOCK);
        this.getOrCreateTagBuilder(SLTags.Items.SILVER_ORES)
          .add(
            SLItems.SILVER_ORE,
            SLItems.DEEPSLATE_SILVER_ORE
          );
        this.getOrCreateTagBuilder(ConventionalItemTags.INGOTS)
          .add(SLItems.SILVER_INGOT);
        this.getOrCreateTagBuilder(ItemTags.SAPLINGS)
          .add(SLItems.GRAFTED_SAPLING);

        this.getOrCreateTagBuilder(ItemTags.PLANKS)
          .add(SLItems.DECAYED_PLANKS);
        this.getOrCreateTagBuilder(ItemTags.WOODEN_FENCES)
          .add(SLItems.DECAYED_FENCE);
        this.getOrCreateTagBuilder(ItemTags.FENCE_GATES)
          .add(SLItems.DECAYED_FENCE_GATE);
        this.getOrCreateTagBuilder(ItemTags.WOODEN_DOORS)
          .add(SLItems.DECAYED_DOOR);
        this.getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS)
          .add(SLItems.DECAYED_BUTTON);
        this.getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS)
          .add(SLItems.DECAYED_TRAPDOOR);
        this.getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS)
          .add(SLItems.DECAYED_STAIRS);
        this.getOrCreateTagBuilder(ItemTags.WOODEN_SLABS)
          .add(SLItems.DECAYED_SLAB);

        this.getOrCreateTagBuilder(SLTags.Items.BLOOD_STORING_BOTTLES)
          .add(Items.GLASS_BOTTLE);
    }
}
