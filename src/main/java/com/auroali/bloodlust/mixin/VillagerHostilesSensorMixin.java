package com.auroali.bloodlust.mixin;

import com.auroali.bloodlust.VampireHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public class VillagerHostilesSensorMixin {
    @Inject(method = "isHostile", at = @At("HEAD"), cancellable = true)
    public void bloodlust$makeVillagersScaredOfVampire(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if(VampireHelper.isVampire(entity) && !VampireHelper.isMasked(entity))
            cir.setReturnValue(true);
    }

    @Inject(method = "isCloseEnoughForDanger", at = @At("HEAD"), cancellable = true)
    public void bloodlust$injectVampireDangerRadius(LivingEntity villager, LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if(!VampireHelper.isVampire(target) || VampireHelper.isMasked(target))
            return;

        if(villager.isSleeping()) {
            cir.setReturnValue(false);
            return;
        }

        if(villager.squaredDistanceTo(target) < 8*8) {
            cir.setReturnValue(true);
            return;
        }
        cir.setReturnValue(false);
    }
}
