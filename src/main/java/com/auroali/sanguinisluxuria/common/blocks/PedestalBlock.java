package com.auroali.sanguinisluxuria.common.blocks;

import com.auroali.sanguinisluxuria.common.blockentities.PedestalBlockEntity;
import com.auroali.sanguinisluxuria.common.registry.SLBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class PedestalBlock extends BlockWithEntity implements Waterloggable {
    private static final VoxelShape SHAPE = Block.createCuboidShape(4, 0, 4, 12, 9, 12);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public PedestalBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        PedestalBlockEntity entity = world.getBlockEntity(pos) instanceof PedestalBlockEntity e ? e : null;
        if (entity == null)
            return super.onUse(state, world, pos, player, hand, hit);

        ItemStack held = player.getStackInHand(hand);
        ItemStack stored = entity.getItem();
        if (held.isEmpty() || (!stored.isEmpty() && (!ItemStack.canCombine(held, stored) || stored.getCount() == stored.getMaxCount()))) {
            player.setStackInHand(hand, stored);
            entity.setItem(held);
            if (stored.isEmpty() && !held.isEmpty())
                this.playInsertSound(player, world);
            else if (!stored.isEmpty())
                this.playRemoveSound(player, world);

            return ActionResult.success(world.isClient);
        }

        if (stored.isEmpty()) {
            entity.setItem(held.split(1));
            this.playInsertSound(player, world);
            return ActionResult.success(world.isClient);
        }

        int maxAdded = Math.min(stored.getMaxCount() - stored.getCount(), held.getCount());

        held.decrement(maxAdded);
        stored.increment(maxAdded);
        entity.setItem(stored);

        this.playInsertSound(player, world);

        return ActionResult.success(world.isClient);
    }

    private void playInsertSound(PlayerEntity player, World world) {
        world.playSound(
          player,
          player.getX(), player.getY(), player.getZ(),
          SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM,
          player.getSoundCategory(),
          1.f,
          1.f
        );
    }

    private void playRemoveSound(PlayerEntity player, World world) {
        world.playSound(
          player,
          player.getX(), player.getY(), player.getZ(),
          SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM,
          player.getSoundCategory(),
          1.f,
          1.f
        );
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof PedestalBlockEntity e) {
                ItemScatterer.spawn(world, pos, e.getInventory());
                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PedestalBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient)
            return checkType(type, SLBlockEntities.PEDESTAL, PedestalBlockEntity::tickClient);
        return super.getTicker(world, state, type);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state != null)
            return state.with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER));
        return null;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED))
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {
            ItemStack stack = pedestal.getInventory().getStack(0);
            return MathHelper.floor(14.f * stack.getCount() / stack.getMaxCount()) + (stack.isEmpty() ? 0 : 1);
        }
        return 0;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }
}
