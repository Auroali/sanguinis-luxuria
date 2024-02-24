package com.auroali.bloodlust.common.abilities;

import com.auroali.bloodlust.common.components.BloodComponent;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.auroali.bloodlust.common.registry.BLRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.RegistryEntry;

public abstract class VampireAbility {
    private final RegistryEntry.Reference<VampireAbility> holder = BLRegistry.VAMPIRE_ABILITIES.createEntry(this);
    public abstract void tick(LivingEntity entity, VampireComponent component, BloodComponent blood);

    public RegistryEntry.Reference<VampireAbility> getRegistryEntry() {
        return holder;
    }


    public boolean isIn(TagKey<VampireAbility> tag) {
        return getRegistryEntry().isIn(tag);
    }
}
