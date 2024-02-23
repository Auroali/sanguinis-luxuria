package com.auroali.bloodlust.common.registry;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.common.blocks.BloodSplatterBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.util.registry.Registry;

public class BLBlocks {
    public static final Block BLOOD_SPLATTER = new BloodSplatterBlock(
            FabricBlockSettings.of(Material.DECORATION)
                    .breakInstantly()
                    .noCollision()
                    .ticksRandomly()
    );

    public static void register() {
        Registry.register(Registry.BLOCK, BLResources.BLOOD_SPLATTER_ID, BLOOD_SPLATTER);
    }
}
