package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.blocks.BloodCauldronBlock;
import com.auroali.sanguinisluxuria.common.blocks.BloodSplatterBlock;
import com.auroali.sanguinisluxuria.common.blocks.PedestalBlock;
import com.auroali.sanguinisluxuria.common.blocks.SkillUpgraderBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public class BLBlocks {
    public static final Block BLOOD_SPLATTER = new BloodSplatterBlock(
            FabricBlockSettings.of(Material.DECORATION)
                    .breakInstantly()
                    .noCollision()
                    .ticksRandomly()
                    .sounds(BlockSoundGroup.HONEY)
    );

    //public static final FluidBlock BLOOD = new FluidBlock(BLFluids.BLOOD_STILL, FabricBlockSettings.of(Material.LAVA));
    public static final Block BLOOD_CAULDRON = new BloodCauldronBlock(FabricBlockSettings.copy(Blocks.CAULDRON));
    public static final Block ALTAR = new SkillUpgraderBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(1.5F, 6.0F));
    public static final Block PEDESTAL = new PedestalBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(1.5F, 6.0F));
    public static final Block SILVER_BLOCK = new Block(FabricBlockSettings.copy(Blocks.IRON_BLOCK).sounds(BlockSoundGroup.NETHERITE));
    public static final Block SILVER_ORE = new Block(FabricBlockSettings.copy(Blocks.IRON_ORE));
    public static final Block DEEPSLATE_SILVER_ORE = new Block(FabricBlockSettings.copy(Blocks.DEEPSLATE_IRON_ORE));
    public static final Block RAW_SILVER_BLOCK = new Block(FabricBlockSettings.copy(Blocks.RAW_COPPER_BLOCK));

    public static void register() {
        Registry.register(Registry.BLOCK, BLResources.BLOOD_SPLATTER_ID, BLOOD_SPLATTER);
        Registry.register(Registry.BLOCK, BLResources.ALTAR_ID, ALTAR);
        Registry.register(Registry.BLOCK, BLResources.PEDESTAL_ID, PEDESTAL);
        //Registry.register(Registry.BLOCK, BLResources.BLOOD_STILL, BLOOD);
        Registry.register(Registry.BLOCK, BLResources.BLOOD_CAULDRON_ID, BLOOD_CAULDRON);
        Registry.register(Registry.BLOCK, BLResources.SILVER_BLOCK_ID, SILVER_BLOCK);
        Registry.register(Registry.BLOCK, BLResources.SILVER_ORE_ID, SILVER_ORE);
        Registry.register(Registry.BLOCK, BLResources.DEEPSLATE_SILVER_ORE_ID, DEEPSLATE_SILVER_ORE);
        Registry.register(Registry.BLOCK, BLResources.RAW_SILVER_BLOCK_ID, RAW_SILVER_BLOCK);
    }
}
