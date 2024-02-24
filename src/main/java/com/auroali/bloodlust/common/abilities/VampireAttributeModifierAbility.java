package com.auroali.bloodlust.common.abilities;

import com.auroali.bloodlust.common.components.BloodComponent;
import com.auroali.bloodlust.common.components.VampireComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public class VampireAttributeModifierAbility extends VampireAbility {
    private final EntityAttribute targetAttribute;
    private final EntityAttributeModifier modifier;
    private final int minBloodAmount;

    public VampireAttributeModifierAbility(EntityAttribute targetAttribute, EntityAttributeModifier modifier) {
        this(targetAttribute, modifier, 0);
    }


    public VampireAttributeModifierAbility(EntityAttribute targetAttribute, EntityAttributeModifier modifier, int minBloodAmount) {
        this.targetAttribute = targetAttribute;
        this.modifier = modifier;
        this.minBloodAmount = minBloodAmount;
    }

    @Override
    public void tick(LivingEntity entity, VampireComponent component, BloodComponent blood) {
        AttributeContainer attributes = entity.getAttributes();
        if(blood.getBlood() >= minBloodAmount && !attributes.getCustomInstance(targetAttribute).hasModifier(modifier))
            attributes.getCustomInstance(targetAttribute).addTemporaryModifier(modifier);
        else if(blood.getBlood() < minBloodAmount && attributes.getCustomInstance(targetAttribute).hasModifier(modifier))
            attributes.getCustomInstance(targetAttribute).removeModifier(modifier);
    }
}
