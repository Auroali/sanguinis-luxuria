package com.auroali.bloodlust.datagen;

import com.auroali.bloodlust.common.registry.BLItems;
import com.auroali.bloodlust.common.registry.BLTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
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
    }
}
