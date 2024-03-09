package com.auroali.sanguinisluxuria.common.registry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;

public class BLDamageSources {
    public static final DamageSource BLOOD_DRAIN = new DamageSource("bloodlust.drain_blood")
            .setBypassesArmor()
            .setOutOfWorld();

    public static final DamageSource BLESSED_WATER = new DamageSource("bloodlust.blessed_water")
            .setBypassesArmor()
            .setUsesMagic();

    public static final DamageSource BLESSED_BLOOD = new DamageSource("bloodlust.blessed_water")
            .setOutOfWorld();

    public static DamageSource bloodDrain(Entity entity) {
        return new EntityDamageSource("bloodlust.drain_blood_by_entity", entity)
                .setBypassesArmor()
                .setOutOfWorld();
    }

    public static DamageSource blessedWater(Entity entity) {
        return new EntityDamageSource("bloodlust.blessed_water_entity", entity)
                .setBypassesArmor()
                .setUsesMagic();
    }

    public static DamageSource blessedWater(Entity entity, Entity attacker) {
        return new ProjectileDamageSource("bloodlust.blessed_water_entity", entity, attacker)
                .setBypassesArmor()
                .setUsesMagic();
    }

    public static DamageSource bite(LivingEntity entity) {
        return new EntityDamageSource("bloodlust.bite", entity)
                .setBypassesArmor();
    }
}
