package com.auroali.sanguinisluxuria.common.abilities;

import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
    public void onAbilityRemoved(LivingEntity entity, VampireComponent vampire) {
        super.onAbilityRemoved(entity, vampire);
        AttributeContainer attributes = entity.getAttributes();
        if(attributes.getCustomInstance(targetAttribute).hasModifier(modifier))
            attributes.getCustomInstance(targetAttribute).removeModifier(modifier);
    }

    @Override
    public AbilityTicker<?> createTicker() {
        return checkType(VampireAttributeModifierAbility::tick);
    }

    public static void tick(VampireAttributeModifierAbility ability, World world, LivingEntity entity, VampireComponent component, VampireAbilityContainer container, BloodComponent blood) {
        AttributeContainer attributes = entity.getAttributes();
        if(blood.getBlood() >= ability.minBloodAmount && !attributes.getCustomInstance(ability.targetAttribute).hasModifier(ability.modifier))
            attributes.getCustomInstance(ability.targetAttribute).addTemporaryModifier(ability.modifier);
        else if(blood.getBlood() < ability.minBloodAmount && attributes.getCustomInstance(ability.targetAttribute).hasModifier(ability.modifier))
            attributes.getCustomInstance(ability.targetAttribute).removeModifier(ability.modifier);
    }
}
