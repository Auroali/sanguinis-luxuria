package com.auroali.bloodlust.common.statuseffects;

import com.auroali.bloodlust.VampireHelper;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;

public class BloodSicknessEffect extends StatusEffect {
    public BloodSicknessEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration == 1;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        if(isRightConditions(entity, amplifier) && BLEntityComponents.VAMPIRE_COMPONENT.isProvidedBy(entity))
            BLEntityComponents.VAMPIRE_COMPONENT.get(entity).setIsVampire(true);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
    }

    public boolean isRightConditions(LivingEntity entity, int amplifier) {
        return !VampireHelper.isVampire(entity) && amplifier >= 4 && entity.hasStatusEffect(StatusEffects.WEAKNESS);
    }
}
