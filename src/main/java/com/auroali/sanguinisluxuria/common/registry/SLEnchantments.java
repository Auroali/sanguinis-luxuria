package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.enchantments.BloodDrainEnchantment;
import com.auroali.sanguinisluxuria.common.enchantments.SerratedEnchantment;
import com.auroali.sanguinisluxuria.common.enchantments.SunProtectionEnchantment;
import com.auroali.sanguinisluxuria.common.legacy.RegistryOverrides;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class SLEnchantments {
    public static final Enchantment SUN_PROTECTION = new SunProtectionEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_HEAD, new EquipmentSlot[]{ EquipmentSlot.HEAD });
    public static final Enchantment BLOOD_TRANSFER = new BloodDrainEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.TRIDENT, new EquipmentSlot[]{ EquipmentSlot.MAINHAND });
    public static final Enchantment SERRATED = new SerratedEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.TRIDENT, new EquipmentSlot[]{ EquipmentSlot.MAINHAND });

    public static void register() {
        Registry.register(Registries.ENCHANTMENT, SLResources.SUN_PROTECTION_ID, SUN_PROTECTION);
        Registry.register(Registries.ENCHANTMENT, SLResources.BLOOD_TRANSFER_COMPONENT_ID, BLOOD_TRANSFER);
        Registry.register(Registries.ENCHANTMENT, SLResources.SERRATED_ID, SERRATED);

        RegistryOverrides.register(Registries.ENCHANTMENT, new Identifier("sanguinisluxuria", "blood_drain"), BLOOD_TRANSFER);
    }
}
