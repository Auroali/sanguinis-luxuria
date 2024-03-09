package com.auroali.bloodlust.datagen;

import com.auroali.bloodlust.common.registry.BLItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class BLModelProvider extends FabricModelProvider {
    public BLModelProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(BLItems.MASK_1, BLModels.MASK);
        itemModelGenerator.register(BLItems.MASK_2, BLModels.MASK);
        itemModelGenerator.register(BLItems.MASK_3, BLModels.MASK);

        itemModelGenerator.register(BLItems.BLOOD_BAG, "_1", Models.GENERATED);
        itemModelGenerator.register(BLItems.BLOOD_BAG, "_2", Models.GENERATED);
        itemModelGenerator.register(BLItems.BLOOD_BAG, "_3", Models.GENERATED);

        itemModelGenerator.register(BLItems.BLOOD_BOTTLE, "_2", Models.GENERATED);
        itemModelGenerator.register(BLItems.TWISTED_BLOOD, Models.GENERATED);

        itemModelGenerator.register(BLItems.BLESSED_BLOOD, Models.GENERATED);
    }
}
