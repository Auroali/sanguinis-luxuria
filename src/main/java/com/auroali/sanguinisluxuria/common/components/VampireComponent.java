package com.auroali.sanguinisluxuria.common.components;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.config.BLConfig;
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

    /**
     * @return if the component holder is a vampire
     */
    boolean isVampire();

    /**
     * Sets whether the component holder is a vampire
     * @param isVampire whether the holder is a vampire
     */
    void setIsVampire(boolean isVampire);

    /**
     * Drains blood from a target entity, filling up the component holder's blood
     * or damaging them if the target has blood protection
     * @param entity the entity to drain from
     * @see BloodComponent#drainBlood(LivingEntity)
     */
    void drainBloodFrom(LivingEntity entity);

    /**
     * Attempts to find a valid target to drain blood from, and start draining blood
     * if one is found. If a valid target cannot be found, it will instead attempt to
     * fill any blood storage items in the component holder's hands
     */
    void tryStartSuckingBlood();

    /**
     * Cancels any blood draining
     */
    void stopSuckingBlood();

    /**
     * Gets the current blood draining progress
     * @return the amount of time blood has been draining for, in ticks
     * @see VampireComponent#BLOOD_TIMER_LENGTH
     */
    int getBloodDrainTimer();

    /**
     * Gets the max time that can be spent in the sun before burning
     * @return the amount of time that can be spent in the sun, in ticks
     */
    int getMaxTimeInSun();

    /**
     * Gets the amount of time spent in the sun
     * @return how long the component holder has been in the sun, in ticks
     */
    int getTimeInSun();

    VampireAbilityContainer getAbilties();

    int getSkillPoints();
    void setSkillPoints(int i);
    void setLevel(int level);
    int getLevel();

    void unlockAbility(VampireAbility ability);

    /**
     * Gets the downed state of this component
     * @return the downed state
     */
    boolean isDown();

    /**
     * Sets the 'downed' state of this component
     * @param down whether downed or not
     */
    void setDowned(boolean down);

    /**
     * Calculates the amount of damage taken by a vampire for a given damage source
     * @param amount the initial amount of damage
     * @param source the damage source
     * @return the amount of damage that should be taken by the vampire
     * @see BLConfig#vampireDamageMultiplier
     */
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

    /**
     * Checks if a particular damage source is effective against vampires
     * @param source the damage source
     * @return whether the source is effective and damage should be increased
     */
    static boolean isEffectiveAgainstVampires(DamageSource source) {
        if(source.isFire())
            return true;

        if(source.isMagic())
            return true;

        if(source.getAttacker() instanceof LivingEntity entity) {
            ItemStack stack = entity.getMainHandStack();
            int level = EnchantmentHelper.getLevel(Enchantments.SMITE, stack);
            return level > 0;
        }

        return false;
    }

}
