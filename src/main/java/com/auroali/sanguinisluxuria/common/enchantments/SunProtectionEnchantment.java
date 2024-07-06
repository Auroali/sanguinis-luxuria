package com.auroali.sanguinisluxuria.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.EquipmentSlot;

public class SunProtectionEnchantment extends Enchantment {
    public SunProtectionEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        if(other instanceof ProtectionEnchantment enchantment) {
            return enchantment.protectionType == ProtectionEnchantment.Type.FALL;
        }
        return true;
    }
}
