package com.auroali.bloodlust.common.registry;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.common.enchantments.BloodDrainEnchantment;
import com.auroali.bloodlust.common.enchantments.SunProtectionEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.registry.Registry;

public class BLEnchantments {
    public static final Enchantment SUN_PROTECTION = new SunProtectionEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_HEAD, new EquipmentSlot[]{ EquipmentSlot.HEAD });
    public static final Enchantment BLOOD_DRAIN = new BloodDrainEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.TRIDENT, new EquipmentSlot[]{ EquipmentSlot.MAINHAND });

    public static void register() {
        Registry.register(Registry.ENCHANTMENT, BLResources.SUN_PROTECTION_ID, SUN_PROTECTION);
        Registry.register(Registry.ENCHANTMENT, BLResources.BLOOD_DRAIN_ID, BLOOD_DRAIN);
    }
}
