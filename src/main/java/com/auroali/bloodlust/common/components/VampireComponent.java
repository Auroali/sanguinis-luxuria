package com.auroali.bloodlust.common.components;

import com.auroali.bloodlust.config.BLConfig;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;

public interface VampireComponent extends Component, AutoSyncedComponent, ServerTickingComponent {
    int BLOOD_TIMER_LENGTH = 25;

    boolean isVampire();
    void setIsVampire(boolean isVampire);
    void drainBloodFrom(LivingEntity entity);

    void tryStartSuckingBlood();

    void stopSuckingBlood();
    int getBloodDrainTimer();
    int getMaxTimeInSun();
    int getTimeInSun();

    static float calculateDamage(float amount, DamageSource source) {
        if(source.getAttacker() instanceof LivingEntity entity) {
            ItemStack stack = entity.getMainHandStack();
            int level = EnchantmentHelper.getLevel(Enchantments.SMITE, stack);
            if(level > 0)
                return amount * (level * (BLConfig.INSTANCE.vampireDamageMultiplier / Enchantments.SMITE.getMaxLevel()));
        }

        if(isEffectiveAgainstVampires(source))
            return amount * BLConfig.INSTANCE.vampireDamageMultiplier;

        return amount;
    }

    static boolean isEffectiveAgainstVampires(DamageSource source) {
        if(source.isFire())
            return true;

        if(source.isMagic())
            return true;

        if(source.getAttacker() instanceof LivingEntity entity) {
            ItemStack stack = entity.getMainHandStack();
            int level = EnchantmentHelper.getLevel(Enchantments.SMITE, stack);
            if(level > 0)
                return true;
        }

        return false;
    }
}
