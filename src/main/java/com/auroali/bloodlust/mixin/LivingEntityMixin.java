package com.auroali.bloodlust.mixin;

import com.auroali.bloodlust.config.BLConfig;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "damage", at = @At(value = "HEAD"))
    public void bloodlust$increaseDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir, @Share("source") LocalRef<DamageSource> dmgSource) {
        dmgSource.set(source);
    }

    @ModifyVariable(method = "damage", at = @At(
            value = "HEAD"
    ), argsOnly = true)
    public float bloodlust$actuallyIncreaseDamage(float amount, @Share("source") LocalRef<DamageSource> source) {
        if(source.get().isFire())
            return amount * BLConfig.INSTANCE.vampireDamageMultiplier;

        if(source.get().isMagic())
            return amount * BLConfig.INSTANCE.vampireDamageMultiplier;

        if(source.get().getAttacker() instanceof LivingEntity entity) {
            ItemStack stack = entity.getMainHandStack();
            int level = EnchantmentHelper.getLevel(Enchantments.SMITE, stack);
            if(level > 0)
                return level * (BLConfig.INSTANCE.vampireDamageMultiplier / Enchantments.SMITE.getMaxLevel());
        }

        return amount;
    }
}
