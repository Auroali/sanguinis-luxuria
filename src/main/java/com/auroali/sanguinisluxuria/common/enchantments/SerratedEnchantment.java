package com.auroali.sanguinisluxuria.common.enchantments;

import com.auroali.sanguinisluxuria.common.registry.BLEnchantments;
import com.auroali.sanguinisluxuria.common.registry.BLStatusEffects;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

public class SerratedEnchantment extends Enchantment {
    public SerratedEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        super.onTargetDamaged(user, target, level);
        if(target.getWorld().random.nextInt(25 / level) == 0 && target instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(BLStatusEffects.BLEEDING, 12 * level));
        }
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return other != BLEnchantments.BLOOD_DRAIN && other != Enchantments.IMPALING;
    }
}
