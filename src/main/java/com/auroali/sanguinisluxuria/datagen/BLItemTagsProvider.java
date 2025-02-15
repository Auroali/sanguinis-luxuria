package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.BLItems;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class BLItemTagsProvider extends FabricTagProvider<Item> {
    public BLItemTagsProvider(FabricDataOutput dataGenerator, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataGenerator, RegistryKeys.ITEM, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(BLTags.Items.FACE_TRINKETS)
          .add(
            BLItems.MASK_1,
            BLItems.MASK_2,
            BLItems.MASK_3
          );
        this.getOrCreateTagBuilder(BLTags.Items.NECKLACE_TRINKETS)
          .add(BLItems.PENDANT_OF_PIERCING);
        this.getOrCreateTagBuilder(BLTags.Items.SUN_BLOCKING_HELMETS)
          .add(
            Items.LEATHER_HELMET,
            Items.CARVED_PUMPKIN
          );
        this.getOrCreateTagBuilder(BLTags.Items.VAMPIRE_MASKS)
          .add(
            BLItems.MASK_1,
            BLItems.MASK_2,
            BLItems.MASK_3
          );
        this.getOrCreateTagBuilder(BLTags.Items.SILVER_INGOTS)
          .add(BLItems.SILVER_INGOT);
        this.getOrCreateTagBuilder(ItemTags.PICKAXES)
          .add(BLItems.SILVER_PICKAXE);
        this.getOrCreateTagBuilder(ItemTags.AXES)
          .add(BLItems.SILVER_AXE);
        this.getOrCreateTagBuilder(ItemTags.SWORDS)
          .add(BLItems.SILVER_SWORD);
        this.getOrCreateTagBuilder(ItemTags.HOES)
          .add(BLItems.SILVER_HOE);
        this.getOrCreateTagBuilder(ItemTags.SHOVELS)
          .add(BLItems.SILVER_SHOVEL);
        this.getOrCreateTagBuilder(ConventionalItemTags.ORES)
          .add(
            BLItems.SILVER_ORE,
            BLItems.DEEPSLATE_SILVER_ORE
          );
        this.getOrCreateTagBuilder(BLTags.Items.DECAYED_LOGS)
          .add(
            BLItems.DECAYED_WOOD,
            BLItems.DECAYED_LOG,
            BLItems.STRIPPED_DECAYED_LOG,
            BLItems.STRIPPED_DECAYED_WOOD,
            BLItems.HUNGRY_DECAYED_LOG,
            BLItems.STRIPPED_HUNGRY_DECAYED_LOG
          );
        this.getOrCreateTagBuilder(ItemTags.LOGS)
          .add(
            BLItems.DECAYED_WOOD,
            BLItems.DECAYED_LOG,
            BLItems.STRIPPED_DECAYED_LOG,
            BLItems.STRIPPED_DECAYED_WOOD,
            BLItems.HUNGRY_DECAYED_LOG,
            BLItems.STRIPPED_HUNGRY_DECAYED_LOG)
        ;
        this.getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
          .add(
            BLItems.DECAYED_WOOD,
            BLItems.DECAYED_LOG,
            BLItems.STRIPPED_DECAYED_LOG,
            BLItems.STRIPPED_DECAYED_WOOD,
            BLItems.HUNGRY_DECAYED_LOG,
            BLItems.STRIPPED_HUNGRY_DECAYED_LOG
          );
        this.getOrCreateTagBuilder(BLTags.Items.HUNGRY_DECAYED_LOGS)
          .add(
            BLItems.HUNGRY_DECAYED_LOG,
            BLItems.STRIPPED_HUNGRY_DECAYED_LOG
          );
        this.getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES)
          .add(BLItems.DECAYED_PRESSURE_PLATE);
        this.getOrCreateTagBuilder(BLTags.Items.SILVER_BLOCKS)
          .add(BLItems.SILVER_BLOCK);
        this.getOrCreateTagBuilder(BLTags.Items.RAW_SILVER_BLOCKS)
          .add(BLItems.RAW_SILVER_BLOCK);
        this.getOrCreateTagBuilder(BLTags.Items.SILVER_ORES)
          .add(
            BLItems.SILVER_ORE,
            BLItems.DEEPSLATE_SILVER_ORE
          );
        this.getOrCreateTagBuilder(ConventionalItemTags.INGOTS)
          .add(BLItems.SILVER_INGOT);
        this.getOrCreateTagBuilder(ItemTags.SAPLINGS)
          .add(BLItems.GRAFTED_SAPLING);

        this.getOrCreateTagBuilder(ItemTags.PLANKS)
          .add(BLItems.DECAYED_PLANKS);
        this.getOrCreateTagBuilder(ItemTags.WOODEN_FENCES)
          .add(BLItems.DECAYED_FENCE);
        this.getOrCreateTagBuilder(ItemTags.FENCE_GATES)
          .add(BLItems.DECAYED_FENCE_GATE);
        this.getOrCreateTagBuilder(ItemTags.WOODEN_DOORS)
          .add(BLItems.DECAYED_DOOR);
        this.getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS)
          .add(BLItems.DECAYED_BUTTON);
        this.getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS)
          .add(BLItems.DECAYED_TRAPDOOR);
        this.getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS)
          .add(BLItems.DECAYED_STAIRS);
        this.getOrCreateTagBuilder(ItemTags.WOODEN_SLABS)
          .add(BLItems.DECAYED_SLAB);

        this.getOrCreateTagBuilder(BLTags.Items.BLOOD_STORING_BOTTLES)
          .add(Items.GLASS_BOTTLE);
    }
}
