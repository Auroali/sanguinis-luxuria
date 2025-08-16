package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

public class SLDamageSources {
    private static RegistryEntry<DamageType> getEntry(World world, RegistryKey<DamageType> key) {
        return world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key);
    }

    public static DamageSource get(World world, RegistryKey<DamageType> key) {
        return new DamageSource(getEntry(world, key));
    }

    public static DamageSource bloodDrain(Entity entity) {
        return new DamageSource(getEntry(entity.getWorld(), SLResources.BLOOD_DRAIN_DAMAGE_KEY), entity);
    }

    public static DamageSource blessedWater(Entity entity) {
        return new DamageSource(getEntry(entity.getWorld(), SLResources.BLESSED_WATER_DAMAGE_KEY), entity);
    }

    public static DamageSource blessedWater(Entity entity, Entity attacker) {
        return new DamageSource(getEntry(entity.getWorld(), SLResources.BLESSED_WATER_DAMAGE_KEY), entity, attacker);
    }

    public static DamageSource bite(LivingEntity entity) {
        return new DamageSource(getEntry(entity.getWorld(), SLResources.BITE_DAMAGE_KEY), entity);
    }

    public static DamageSource piercing(LivingEntity entity) {
        return new DamageSource(getEntry(entity.getWorld(), SLResources.PIERCING_DAMAGE_KEY), entity);
    }
}
