package com.auroali.bloodlust.common.registry;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.common.blocks.BloodSplatterBlock;
import com.auroali.bloodlust.common.blocks.SkillUpgraderBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.registry.Registry;

public class BLBlocks {
    public static final Block BLOOD_SPLATTER = new BloodSplatterBlock(
            FabricBlockSettings.of(Material.DECORATION)
                    .breakInstantly()
                    .noCollision()
                    .ticksRandomly()
    );
    public static final Block SKILL_UPGRADER = new SkillUpgraderBlock(FabricBlockSettings.of(Material.STONE));

    public static void register() {
        Registry.register(Registry.BLOCK, BLResources.BLOOD_SPLATTER_ID, BLOOD_SPLATTER);
        Registry.register(Registry.BLOCK, BLResources.SKILL_UPGRADER_ID, SKILL_UPGRADER);
    }
}
