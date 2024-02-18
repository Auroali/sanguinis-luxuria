package com.auroali.bloodlust.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "damage", at = @At(value = "HEAD"))
    public void bloodlust$increaseDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir, @Share("source") LocalRef<DamageSource> dmgSource) {
        dmgSource.set(source);
    }

    @ModifyVariable(method = "damage", at = @At(
            value = "HEAD"
    ))
    public float bloodlust$actuallyIncreaseDamage(float amount, @Share("source") LocalRef<DamageSource> source) {
        if(source.get().isFire())
            return amount * 2;
        return amount;
    }
}
