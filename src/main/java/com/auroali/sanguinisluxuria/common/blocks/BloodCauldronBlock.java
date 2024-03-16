package com.auroali.sanguinisluxuria.common.blocks;

import com.auroali.sanguinisluxuria.common.registry.BLCauldronBehaviours;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class BloodCauldronBlock extends LeveledCauldronBlock {
    public BloodCauldronBlock(Settings settings) {
        super(settings, precipitation -> false, BLCauldronBehaviours.BLOOD_CAULDRON_BEHAVIOUR);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return Blocks.CAULDRON.getPickStack(world, pos, state);
    }
}
