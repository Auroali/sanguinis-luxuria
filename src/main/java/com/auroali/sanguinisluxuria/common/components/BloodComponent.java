package com.auroali.sanguinisluxuria.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;

public interface BloodComponent extends Component, AutoSyncedComponent {
    /**
     * @return the amount of blood the component holder has
     */
    int getBlood();

    /**
     * @return the maximum amount of blood the component holder can have
     */
    int getMaxBlood();

    /**
     * Sets the current amount of blood
     *
     * @param amount the new amount of blood
     */
    void setBlood(int amount);

    /**
     * Adds blood to the current amount
     *
     * @param amount the amount to add
     * @return the amount actually added
     */
    default int addBlood(int amount) {
        int amountAdded = Math.min(amount, this.getMaxBlood() - this.getBlood());
        this.setBlood(this.getBlood() + amountAdded);
        return amountAdded;
    }

    /**
     * Tries to drain a specified amount of blood
     *
     * @param amount  the amount of blood to drain
     * @param drainer the entity draining the component holder's blood
     * @return whether blood was actually drained
     * @see BloodComponent#drainBlood(int)
     */
    default boolean drainBlood(int amount, LivingEntity drainer) {
        if (this.isEmpty())
            return false;

        if (this.getBlood() < amount)
            return false;

        this.setBlood(this.getBlood() - amount);
        return true;
    }

    /**
     * Tries to drain a specified amount of blood
     *
     * @param amount the amount of blood to drain
     * @return whether blood was actually drained
     * @see BloodComponent#drainBlood(int, LivingEntity)
     */
    default boolean drainBlood(int amount) {
        return this.drainBlood(amount, null);
    }

    /**
     * @return whether the component holder has blood
     */
    default boolean isEmpty() {
        return this.getBlood() <= 0 || this.getMaxBlood() <= 0;
    }
}
