package com.auroali.bloodlust.common.abilities;

import com.auroali.bloodlust.common.components.BloodComponent;
import com.auroali.bloodlust.common.components.VampireComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

/**
 * Vampire ability with an empty tick function
 */
public class SimpleVampireAbility extends VampireAbility{
    public SimpleVampireAbility(Supplier<ItemStack> icon, VampireAbility parent) {
        super(icon, parent);
    }

    @Override
    public void tick(LivingEntity entity, VampireComponent component, BloodComponent blood) {

    }
}
