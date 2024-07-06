package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.BLBlocks;
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
        getOrCreateTagBuilder(BLTags.Items.FACE_TRINKETS)
                .add(BLItems.MASK_1)
                .add(BLItems.MASK_2)
                .add(BLItems.MASK_3);
        getOrCreateTagBuilder(BLTags.Items.NECKLACE_TRINKETS)
                .add(BLItems.PENDANT_OF_TRANSFUSION)
                .add(BLItems.PENDANT_OF_PIERCING);
//        getOrCreateTagBuilder(BLTags.Items.VAMPIRE_EDIBLE)
//                .add(Items.GOLDEN_APPLE)
//                .add(Items.ENCHANTED_GOLDEN_APPLE)
//                .add(Items.HONEY_BOTTLE)
//                .add(Items.SUSPICIOUS_STEW);
        getOrCreateTagBuilder(BLTags.Items.SUN_BLOCKING_HELMETS)
                .add(Items.LEATHER_HELMET)
                .add(Items.CARVED_PUMPKIN);
        getOrCreateTagBuilder(BLTags.Items.VAMPIRE_MASKS)
                .add(BLItems.MASK_1)
                .add(BLItems.MASK_2)
                .add(BLItems.MASK_3);
        getOrCreateTagBuilder(BLTags.Items.SILVER_INGOTS)
                .add(BLItems.SILVER_INGOT);
        getOrCreateTagBuilder(ItemTags.PICKAXES)
                .add(BLItems.SILVER_PICKAXE);
        getOrCreateTagBuilder(ItemTags.AXES)
                .add(BLItems.SILVER_AXE);
        getOrCreateTagBuilder(ItemTags.SWORDS)
                .add(BLItems.SILVER_SWORD);
        getOrCreateTagBuilder(ItemTags.HOES)
                .add(BLItems.SILVER_HOE);
        getOrCreateTagBuilder(ItemTags.SHOVELS)
                .add(BLItems.SILVER_SHOVEL);
        getOrCreateTagBuilder(ConventionalItemTags.ORES)
                .add(BLBlocks.SILVER_ORE.asItem())
                .add(BLBlocks.DEEPSLATE_SILVER_ORE.asItem());
    }
}
