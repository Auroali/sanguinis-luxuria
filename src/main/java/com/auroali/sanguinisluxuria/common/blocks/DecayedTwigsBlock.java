package com.auroali.sanguinisluxuria.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class DecayedTwigsBlock extends HorizontalFacingBlock {
    public static final VoxelShape Z_SHAPE = Block.createCuboidShape(8, 0, 0, 9, 16, 16);
    public static final VoxelShape X_SHAPE = Block.createCuboidShape(0, 0, 8, 16, 16, 9);

    public DecayedTwigsBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case EAST, WEST -> X_SHAPE;
            default -> Z_SHAPE;
        };
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos supportingPos = pos.offset(state.get(FACING));
        BlockState supportingState = world.getBlockState(supportingPos);
        return supportingState.isSideSolidFullSquare(world, supportingPos, state.get(FACING).getOpposite());
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = this.getDefaultState();
        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                state = state.with(FACING, direction);
                if (state.canPlaceAt(ctx.getWorld(), ctx.getBlockPos()))
                    return state;
            }
        }
        return null;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
        if (!state.canPlaceAt(world, pos))
            world.removeBlock(pos, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }
}
