package com.auroali.sanguinisluxuria.mixin;

import com.auroali.sanguinisluxuria.common.registry.SLBlockEntities;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
    // my ide is complaining but it compiles so i think it just doesnt know how to handle this
    @WrapOperation(method = "<clinit>", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/block/entity/BlockEntityType$Builder;create(Lnet/minecraft/block/entity/BlockEntityType$BlockEntityFactory;[Lnet/minecraft/block/Block;)Lnet/minecraft/block/entity/BlockEntityType$Builder;",
      ordinal = 7
    ))
    private static <T extends BlockEntity> BlockEntityType.Builder<T> sanguinisluxuria$modifySignBlocks(BlockEntityType.BlockEntityFactory<? extends T> factory, Block[] blocks, Operation<BlockEntityType.Builder<T>> original) {
        List<Block> newBlocks = new ArrayList<>(Arrays.asList(blocks));
        newBlocks.addAll(SLBlockEntities.SIGNS);
        blocks = newBlocks.toArray(Block[]::new);
        return original.call(factory, blocks);
    }

    @WrapOperation(method = "<clinit>", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/block/entity/BlockEntityType$Builder;create(Lnet/minecraft/block/entity/BlockEntityType$BlockEntityFactory;[Lnet/minecraft/block/Block;)Lnet/minecraft/block/entity/BlockEntityType$Builder;",
      ordinal = 8
    ))
    private static <T extends BlockEntity> BlockEntityType.Builder<T> sanguinisluxuria$modifyHangingSignBlocks(BlockEntityType.BlockEntityFactory<? extends T> factory, Block[] blocks, Operation<BlockEntityType.Builder<T>> original) {
        List<Block> newBlocks = new ArrayList<>(Arrays.asList(blocks));
        newBlocks.addAll(SLBlockEntities.HANGING_SIGNS);
        blocks = newBlocks.toArray(Block[]::new);
        return original.call(factory, blocks);
    }
}
