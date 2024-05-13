package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.BLItems;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

public class BLItemTagsProvider extends FabricTagProvider<Item> {
    public BLItemTagsProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator, Registry.ITEM);
    }

    @Override
    protected void generateTags() {
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
    }
}
