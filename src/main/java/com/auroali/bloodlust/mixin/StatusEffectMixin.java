package com.auroali.bloodlust.mixin;

import com.auroali.bloodlust.VampireHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatusEffect.class)
public class StatusEffectMixin {
    @Inject(method = "applyUpdateEffect", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V",
            shift = At.Shift.BEFORE
    ), cancellable = true)
    public void bloodlust$preventSaturationForVampires(LivingEntity entity, int amplifier, CallbackInfo ci) {
        if(VampireHelper.isVampire(entity))
            ci.cancel();
    }

    @Inject(method = "applyInstantEffect", at = @At("HEAD"), cancellable = true)
    public void bloodlust$preventEffectsForVampires(Entity source, Entity attacker, LivingEntity target, int amplifier, double proximity, CallbackInfo ci) {
        if(VampireHelper.isVampire(target)) {
            StatusEffect effect = (StatusEffect) (Object) this;
            if(effect == StatusEffects.INSTANT_HEALTH || effect == StatusEffects.INSTANT_DAMAGE)
                ci.cancel();
        }
    }
}
