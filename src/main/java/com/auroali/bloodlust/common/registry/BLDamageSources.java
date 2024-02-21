package com.auroali.bloodlust.common.registry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;

public class BLDamageSources {
    public static DamageSource BLOOD_DRAIN = new DamageSource("bloodlust.drain_blood")
            .setBypassesArmor()
            .setOutOfWorld();

    public static DamageSource bloodDrain(LivingEntity entity) {
        return new EntityDamageSource("bloodlust.drain_blood_by_entity", entity)
                .setBypassesArmor()
                .setOutOfWorld();
    }
}
