package com.auroali.sanguinisluxuria.mixin;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.registry.SLEntityAttributes;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
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
    public void sanguinisluxuria$preventSaturationForVampires(LivingEntity entity, int amplifier, CallbackInfo ci) {
        if (VampireHelper.isVampire(entity))
            ci.cancel();
    }

    @Inject(method = "applyUpdateEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", ordinal = 0), cancellable = true)
    public void sanguinisluxuria$preventPoisonFromKilling(LivingEntity entity, int amplifier, CallbackInfo ci) {
        if (VampireHelper.isVampire(entity) && entity.getDamageSources().magic().isIn(SLTags.DamageTypes.VAMPIRES_WEAK_TO)) {
            float vulnerability = (float) entity.getAttributeValue(SLEntityAttributes.VULNERABILITY);
            entity.damage(entity.getDamageSources().magic(), 1.0F / vulnerability);
            ci.cancel();
        }
    }
}
