package com.auroali.sanguinisluxuria.mixin;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class CakeBlockMixin {
    @Inject(method = "tryEat", at = @At("HEAD"))
    private static void sanguinisluxuria$setPlayerForCakeEatOverride(WorldAccess world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<ActionResult> cir, @Share("player") LocalRef<PlayerEntity> playerShare) {
        playerShare.set(player);
    }

    @Redirect(method = "tryEat", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V"))
    private static void sanguinisluxuria$preventCakeEat(HungerManager instance, int food, float saturationModifier, @Share("player") LocalRef<PlayerEntity> playerShare) {
        if(VampireHelper.isVampire(playerShare.get()))
            return;

        instance.add(food, saturationModifier);
    }
}
