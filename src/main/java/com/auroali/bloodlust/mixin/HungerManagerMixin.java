package com.auroali.bloodlust.mixin;

import com.auroali.bloodlust.VampireHelper;
import com.auroali.bloodlust.common.registry.BLTags;
import com.auroali.bloodlust.config.BLConfig;
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
    public PlayerEntity bloodlust$hmTrackedPlayer = null;

    @Inject(method = "update", at = @At("HEAD"))
    public void bloodlust$setTrackedPlayer(PlayerEntity player, CallbackInfo ci) {
        bloodlust$hmTrackedPlayer = player;
    }

    @ModifyVariable(method = "addExhaustion", at = @At("HEAD"), argsOnly = true)
    public float bloodlust$lowerExhaustionForVampires(float exhaustion) {
        if(bloodlust$hmTrackedPlayer == null)
            return exhaustion;

        if(VampireHelper.isVampire(bloodlust$hmTrackedPlayer))
            return exhaustion * BLConfig.INSTANCE.vampireExhaustionMultiplier;

        return exhaustion;
    }

    @ModifyConstant(method = "update", constant = @Constant(intValue = 10))
    public int bloodlust$modifyHealRate(int constant) {
        if(VampireHelper.isVampire(bloodlust$hmTrackedPlayer))
            return constant / 2;
        return constant;
    }

    @ModifyConstant(method = "update", constant = @Constant(intValue = 80))
    public int bloodlust$modifySecondHealRate(int constant) {
        if(VampireHelper.isVampire(bloodlust$hmTrackedPlayer))
            return constant / 4;
        return constant;
    }

    @ModifyConstant(method = "update", constant = @Constant(intValue = 18))
    public int bloodlust$modifyMinHealth(int constant) {
        if(VampireHelper.isVampire(bloodlust$hmTrackedPlayer))
            return 15;
        return constant;
    }

    @ModifyConstant(
            method = "update",
            constant = @Constant(floatValue = 6.0f, ordinal = 2)
    )
    public float bloodlust$makeRegenUseMoreExhaustion(float value) {
        if(VampireHelper.isVampire(bloodlust$hmTrackedPlayer))
            return value / BLConfig.INSTANCE.vampireExhaustionMultiplier;
        return value;
    }

    @Inject(method = "eat", at = @At("HEAD"), cancellable = true)
    public void bloodlust$handleVampireEdibleFood(Item item, ItemStack stack, CallbackInfo ci) {
        if(VampireHelper.isVampire(bloodlust$hmTrackedPlayer) && !stack.isIn(BLTags.Items.VAMPIRES_GET_HUNGER_FROM))
            ci.cancel();
    }
}
