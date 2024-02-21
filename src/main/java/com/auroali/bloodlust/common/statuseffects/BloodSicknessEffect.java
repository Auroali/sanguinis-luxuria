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
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        if(amplifier > 4 && !VampireHelper.isVampire(entity) && isRightConditions(entity)) {
            BLEntityComponents.VAMPIRE_COMPONENT.get(entity).setIsVampire(true);
            entity.clearStatusEffects();
        }
    }

    public boolean isRightConditions(LivingEntity entity) {
        return entity.hasStatusEffect(StatusEffects.WEAKNESS);
    }
}
