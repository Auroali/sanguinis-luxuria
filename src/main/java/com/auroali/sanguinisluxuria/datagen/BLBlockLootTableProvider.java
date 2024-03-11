package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.BLBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.item.Items;

public class BLBlockLootTableProvider extends FabricBlockLootTableProvider {
    public BLBlockLootTableProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateBlockLootTables() {
        addDrop(BLBlocks.SKILL_UPGRADER);
        addDrop(BLBlocks.PEDESTAL);
        addDrop(BLBlocks.BLOOD_CAULDRON, Items.CAULDRON);
    }
}
