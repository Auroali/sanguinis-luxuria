package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.statuseffects.BlessedWaterEffect;
import com.auroali.sanguinisluxuria.common.statuseffects.BloodProtectionEffect;
import com.auroali.sanguinisluxuria.common.statuseffects.BloodSicknessEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.registry.Registry;

public class BLStatusEffects {
    public static final StatusEffect BLOOD_SICKNESS = new BloodSicknessEffect(StatusEffectCategory.HARMFUL, 0xFF3A0000);
    public static final StatusEffect BLESSED_WATER = new BlessedWaterEffect(StatusEffectCategory.BENEFICIAL, 0x7AD0E6);
    public static final StatusEffect BLOOD_PROTECTION = new BloodProtectionEffect(StatusEffectCategory.BENEFICIAL, 0x7AD0E6);

    public static final Potion BLESSED_WATER_POTION = new Potion(new StatusEffectInstance(BLESSED_WATER, 0, 0));

    public static void register() {
        Registry.register(Registry.STATUS_EFFECT, BLResources.BLOOD_SICKNESS_ID, BLOOD_SICKNESS);
        Registry.register(Registry.STATUS_EFFECT, BLResources.BLESSED_WATER_ID, BLESSED_WATER);
        Registry.register(Registry.STATUS_EFFECT, BLResources.BLOOD_PROTECTION_ID, BLOOD_PROTECTION);

        Registry.register(Registry.POTION, BLResources.BLESSED_WATER_ID, BLESSED_WATER_POTION);
    }
}
