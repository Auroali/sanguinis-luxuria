package com.auroali.sanguinisluxuria.mixin;

import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.util.VampireHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BeehiveBlock.class)
public class BeehiveBlockMixin {
    @WrapOperation(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/CampfireBlock;isLitCampfireInRange(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z"))
    public boolean sanguinisluxuria$preventBeeAngerWhenMist(World world, BlockPos pos, Operation<Boolean> original, @Local(argsOnly = true) PlayerEntity player) {
        boolean result = original.call(world, pos);
        return result || VampireHelper.isVampire(player) && VampireComponent.KEY.get(player).isMist();
    }
}
