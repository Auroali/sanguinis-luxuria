package com.auroali.sanguinisluxuria.mixin;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import com.auroali.sanguinisluxuria.config.BLConfig;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {
    @Unique
    public PlayerEntity sanguinisluxuria$hmTrackedPlayer = null;

    @Inject(method = "update", at = @At("HEAD"))
    public void sanguinisluxuria$setTrackedPlayer(PlayerEntity player, CallbackInfo ci) {
        sanguinisluxuria$hmTrackedPlayer = player;
    }

    @ModifyConstant(method = "update",
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getHealth()F", ordinal = 1)),
            constant = @Constant(floatValue = 1.0f, ordinal = 1)
    )
    public float sanguinisluxuria$stopStarvingDamageWhenDowned(float constant) {
        if (VampireHelper.isVampire(sanguinisluxuria$hmTrackedPlayer) && BLEntityComponents.VAMPIRE_COMPONENT.get(sanguinisluxuria$hmTrackedPlayer).isDown()) {
            return 0;
        }
        return constant;
    }

    @ModifyVariable(method = "addExhaustion", at = @At("HEAD"), argsOnly = true)
    public float sanguinisluxuria$lowerExhaustionForVampires(float exhaustion) {
        if(sanguinisluxuria$hmTrackedPlayer == null)
            return exhaustion;

        if(VampireHelper.isVampire(sanguinisluxuria$hmTrackedPlayer))
            return exhaustion * BLConfig.INSTANCE.vampireExhaustionMultiplier;

        return exhaustion;
    }

    @ModifyConstant(method = "update", constant = @Constant(intValue = 10))
    public int sanguinisluxuria$modifyHealRate(int constant) {
        if(VampireHelper.isVampire(sanguinisluxuria$hmTrackedPlayer))
            return constant / 2;
        return constant;
    }

    @ModifyConstant(method = "update", constant = @Constant(intValue = 80))
    public int sanguinisluxuria$modifySecondHealRate(int constant) {
        if(VampireHelper.isVampire(sanguinisluxuria$hmTrackedPlayer))
            return constant / 4;
        return constant;
    }

    @ModifyConstant(method = "update", constant = @Constant(intValue = 18))
    public int sanguinisluxuria$modifyMinHealth(int constant) {
        if(VampireHelper.isVampire(sanguinisluxuria$hmTrackedPlayer))
            return 8;
        return constant;
    }

    @ModifyConstant(
            method = "update",
            constant = @Constant(floatValue = 6.0f, ordinal = 2)
    )
    public float sanguinisluxuria$makeRegenUseMoreExhaustion(float value) {
        if(VampireHelper.isVampire(sanguinisluxuria$hmTrackedPlayer))
            return value / (BLConfig.INSTANCE.vampireExhaustionMultiplier * 1.75f);
        return value;
    }

    @Inject(method = "eat", at = @At("HEAD"), cancellable = true)
    public void sanguinisluxuria$handleVampireEdibleFood(Item item, ItemStack stack, CallbackInfo ci) {
        if(VampireHelper.isVampire(sanguinisluxuria$hmTrackedPlayer) && !stack.isIn(BLTags.Items.VAMPIRES_GET_HUNGER_FROM))
            ci.cancel();
    }
}
