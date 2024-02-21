package com.auroali.bloodlust.common.registry;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.common.statuseffects.BloodSicknessEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.registry.Registry;

public class BLStatusEffects {
    public static final StatusEffect BLOOD_SICKNESS = new BloodSicknessEffect(StatusEffectCategory.HARMFUL, 0xFF3A0000);

    public static void register() {
        Registry.register(Registry.STATUS_EFFECT, BLResources.BLOOD_SICKNESS_ID, BLOOD_SICKNESS);
    }
}
