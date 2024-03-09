package com.auroali.sanguinisluxuria.common.abilities;

import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class VampireAttributeModifierAbility extends VampireAbility {
    private final EntityAttribute targetAttribute;
    private final EntityAttributeModifier modifier;
    private final int minBloodAmount;

    public VampireAttributeModifierAbility(Supplier<ItemStack> icon, VampireAbility parent, EntityAttribute targetAttribute, EntityAttributeModifier modifier) {
        this(icon, parent, targetAttribute, modifier, 0);
    }


    public VampireAttributeModifierAbility(Supplier<ItemStack> icon, VampireAbility parent, EntityAttribute targetAttribute, EntityAttributeModifier modifier, int minBloodAmount) {
        super(icon, parent);
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
