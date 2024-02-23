package com.auroali.bloodlust.mixin;

import com.auroali.bloodlust.VampireHelper;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.BloodComponent;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow protected abstract boolean tryUseTotem(DamageSource source);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "damage", at = @At(value = "HEAD"))
    public void bloodlust$increaseDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir, @Share("source") LocalRef<DamageSource> dmgSource) {
        dmgSource.set(source);
    }

    @ModifyVariable(method = "damage", at = @At(
            value = "HEAD"
    ), argsOnly = true)
    public float bloodlust$actuallyIncreaseDamage(float amount, @Share("source") LocalRef<DamageSource> source) {
        if(VampireHelper.isVampire((LivingEntity)(Object)this))
            return VampireComponent.calculateDamage(amount, source.get());
        return amount;
    }

    @Inject(method = "applyDamage", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;setHealth(F)V"))
    public void bloodlust$cancelBloodDrainOnDamageTaken(DamageSource source, float amount, CallbackInfo ci) {
        if(!VampireHelper.isVampire((LivingEntity)(Object)this))
            return;

        VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(this);
        vampire.stopSuckingBlood();
    }

    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z"))
    public boolean bloodlust$tryPreventDeath(LivingEntity instance, DamageSource source) {
        if(tryUseTotem(source))
            return true;

        if(!VampireHelper.isVampire(instance))
            return false;

//        VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(instance);
        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(instance);

        if(blood.getBlood() == 0 || VampireComponent.isEffectiveAgainstVampires(source))
            return false;

        instance.setHealth(Math.max(Math.min(instance.getMaxHealth(), (float) blood.getBlood() / 4), 1));
        blood.setBlood(0);
        return true;
    }
}
