package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.SLBlocks;
import com.auroali.sanguinisluxuria.common.registry.SLItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.item.Items;

public class SLBlockLootTableProvider extends FabricBlockLootTableProvider {
    public SLBlockLootTableProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generate() {
        this.addDrop(SLBlocks.ALTAR);
        this.addDrop(SLBlocks.PEDESTAL);
        this.addDrop(SLBlocks.BLOOD_CAULDRON, Items.CAULDRON);

        this.oreDrops(SLBlocks.SILVER_ORE, SLItems.RAW_SILVER);
        this.oreDrops(SLBlocks.DEEPSLATE_SILVER_ORE, SLItems.RAW_SILVER);

        this.addDrop(SLBlocks.SILVER_BLOCK);
        this.addDrop(SLBlocks.RAW_SILVER_BLOCK);

        this.addDrop(SLBlocks.STRIPPED_HUNGRY_DECAYED_LOG);
        this.addDrop(SLBlocks.HUNGRY_DECAYED_LOG);
        this.addDrop(SLBlocks.DECAYED_LOG);
        this.addDrop(SLBlocks.DECAYED_WOOD);
        this.addDrop(SLBlocks.STRIPPED_DECAYED_LOG);
        this.addDrop(SLBlocks.STRIPPED_DECAYED_WOOD);
        this.addDrop(SLBlocks.DECAYED_TWIGS);
        this.addDrop(SLBlocks.GRAFTED_SAPLING);

        this.addDrop(SLBlocks.DECAYED_PRESSURE_PLATE);
        this.addDrop(SLBlocks.SILVER_PRESSURE_PLATE);

        this.addDrop(SLBlocks.DECAYED_PLANKS);
        this.addDrop(SLBlocks.DECAYED_STAIRS);
        this.addDrop(SLBlocks.DECAYED_DOOR);
        this.addDrop(SLBlocks.DECAYED_SIGN);
        this.addDrop(SLBlocks.DECAYED_HANGING_SIGN);
        this.addDrop(SLBlocks.DECAYED_WALL_HANGING_SIGN);
        this.addDrop(SLBlocks.DECAYED_WALL_SIGN);
        this.addDrop(SLBlocks.DECAYED_FENCE);
        this.addDrop(SLBlocks.DECAYED_FENCE_GATE);
        this.addDrop(SLBlocks.DECAYED_SLAB);
        this.addDrop(SLBlocks.DECAYED_BUTTON);
        this.addDrop(SLBlocks.SILVER_BARS);

        this.addPottedPlantDrops(SLBlocks.POTTED_GRAFTED_SAPLING);
    }
}
