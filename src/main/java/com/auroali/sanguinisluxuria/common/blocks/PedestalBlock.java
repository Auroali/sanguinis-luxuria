package com.auroali.sanguinisluxuria.common.blocks;

import com.auroali.sanguinisluxuria.common.blockentities.PedestalBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PedestalBlock extends BlockWithEntity {
    private static final VoxelShape SHAPE = Block.createCuboidShape(4, 0, 4, 12, 9, 12);
    public PedestalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        PedestalBlockEntity entity = world.getBlockEntity(pos) instanceof PedestalBlockEntity e ? e : null;
        if(entity == null)
            return super.onUse(state, world, pos, player, hand, hit);

        ItemStack stack = player.getStackInHand(hand);
        if(stack.isEmpty() && !entity.getItem().isEmpty()) {
            if(!world.isClient) {
                player.setStackInHand(hand, entity.getItem());
                entity.setItem(ItemStack.EMPTY);
            }
            return ActionResult.success(world.isClient);
        }

        if(!stack.isEmpty() && entity.getItem().isEmpty()) {
            if(!world.isClient) {
                player.setStackInHand(hand, ItemStack.EMPTY);
                entity.setItem(stack);
            }
            return ActionResult.success(world.isClient);
        }

        return super.onUse(state, world, pos, player, hand, hit);
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
}
