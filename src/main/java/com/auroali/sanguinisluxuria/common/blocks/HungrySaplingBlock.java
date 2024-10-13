package com.auroali.sanguinisluxuria.common.blocks;

import com.auroali.sanguinisluxuria.common.registry.BLBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class HungrySaplingBlock extends Block {
    public HungrySaplingBlock(Settings settings) {
        super(settings);
    }

    public void grow(World world, BlockPos pos, Random random) {
        int height = random.nextBetween(3, 4);
        int hungryLogPos = random.nextBetween(1, height - 2);
        List<BlockPos> placedBlocks = new ArrayList<>(15);

        // place the main logs
        for (int i = 0; i < height; i++) {
            BlockState logBlock = i != hungryLogPos ? BLBlocks.DECAYED_LOG.getDefaultState() : BLBlocks.HUNGRY_DECAYED_LOG.getDefaultState();
            world.setBlockState(pos.up(i), logBlock, Block.NOTIFY_ALL);
            if (!logBlock.isOf(BLBlocks.HUNGRY_DECAYED_LOG))
                placedBlocks.add(pos.up(i));
        }

        // place supporting logs
        for (Direction direction : Direction.Type.HORIZONTAL) {
            if (random.nextInt(3) != 0)
                continue;

            int lowerBranchHeight = random.nextBetween(2, 3);
            for (int i = 0; i < lowerBranchHeight; i++) {
                BlockState logBlock = i == 0 ? BLBlocks.DECAYED_WOOD.getDefaultState() : BLBlocks.DECAYED_LOG.getDefaultState();
                BlockPos logPos = pos.offset(direction).down(i);
                ItemPlacementContext placementContext = new AutomaticItemPlacementContext(world, logPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP);
                if (!world.getBlockState(logPos).canReplace(placementContext))
                    break;
                world.setBlockState(logPos, logBlock, Block.NOTIFY_ALL);
                placedBlocks.add(logPos);
            }
        }

        // post process all placed log blocks
        // adds in the twigs
        BlockState decayedTwigsState = BLBlocks.DECAYED_TWIGS.getDefaultState();
        for (BlockPos logPos : placedBlocks) {
            for (Direction direction : Direction.Type.HORIZONTAL) {
                if (random.nextInt(6) != 0 || !world.getBlockState(logPos.offset(direction)).isAir())
                    continue;
                world.setBlockState(logPos.offset(direction), decayedTwigsState.with(Properties.HORIZONTAL_FACING, direction.getOpposite()), Block.NOTIFY_ALL);
            }
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(25) == 0) {
            grow(world, pos, random);
        }
    }
}
