package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.blocks.BloodSplatterBlock;
import com.auroali.sanguinisluxuria.common.blocks.PedestalBlock;
import com.auroali.sanguinisluxuria.common.blocks.SkillUpgraderBlock;
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
    public static final Block PEDESTAL = new PedestalBlock(FabricBlockSettings.of(Material.DECORATION));

    public static void register() {
        Registry.register(Registry.BLOCK, BLResources.BLOOD_SPLATTER_ID, BLOOD_SPLATTER);
        Registry.register(Registry.BLOCK, BLResources.SKILL_UPGRADER_ID, SKILL_UPGRADER);
        Registry.register(Registry.BLOCK, BLResources.PEDESTAL_ID, PEDESTAL);
    }
}
