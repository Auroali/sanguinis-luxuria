package com.auroali.sanguinisluxuria.common.enchantments;

import com.auroali.sanguinisluxuria.common.registry.BLEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class SunProtectionEnchantment extends Enchantment {
    public SunProtectionEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        if (other instanceof ProtectionEnchantment enchantment) {
            return enchantment.protectionType == ProtectionEnchantment.Type.FALL;
        }
        return true;
    }

    @Override
    public float getAttackDamage(int level, EntityGroup group) {
        return super.getAttackDamage(level, group);
    }

    public static double getForLevel(int level) {
        return 10.d * level;
    }

    public static double calculateForEntity(LivingEntity entity) {
        double sunProt = 0.0d;
        for (ItemStack stack : entity.getArmorItems()) {
            int level = EnchantmentHelper.getLevel(BLEnchantments.SUN_PROTECTION, stack);
            if (level > 0) {
                sunProt += getForLevel(level);
            }
        }
        return sunProt;
    }
}
