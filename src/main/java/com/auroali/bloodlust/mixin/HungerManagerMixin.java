package com.auroali.bloodlust.mixin;

import com.auroali.bloodlust.VampireHelper;
import com.auroali.bloodlust.config.BLConfig;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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
}
