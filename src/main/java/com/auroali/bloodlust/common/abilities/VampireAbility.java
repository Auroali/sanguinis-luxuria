package com.auroali.bloodlust.common.abilities;

import com.auroali.bloodlust.common.components.BloodComponent;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.auroali.bloodlust.common.registry.BLRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class VampireAbility {
    private final VampireAbility parent;
    private final Supplier<ItemStack> icon;
    private final List<Supplier<VampireAbility>> incompatibilities;
    private String transKey;
    public VampireAbility(Supplier<ItemStack> icon, VampireAbility parent) {
        this.icon = icon;
        this.parent = parent;
        this.incompatibilities = new ArrayList<>();
    }

    private final RegistryEntry.Reference<VampireAbility> holder = BLRegistry.VAMPIRE_ABILITIES.createEntry(this);
    public abstract void tick(LivingEntity entity, VampireComponent component, BloodComponent blood);

    public RegistryEntry.Reference<VampireAbility> getRegistryEntry() {
        return holder;
    }

    public boolean isKeybindable() {
        return false;
    }

    public boolean isIn(TagKey<VampireAbility> tag) {
        return getRegistryEntry().isIn(tag);
    }

    public VampireAbility getParent() {
        return parent;
    }

    public ItemStack getIcon() {
        return icon.get();
    }

    public String getTranslationKey() {
        if(transKey == null && getRegistryEntry().getKey().isPresent()) {
            Identifier id = getRegistryEntry().getKey().get().getValue();
            transKey = "vampire_ability.%s.%s".formatted(id.getNamespace(), id.getPath());
        }
        return transKey == null ? "" : transKey;
    }

    // todo: implement way to set this
    public int getRequiredSkillPoints() {
        return 0;
    }

    public boolean activate(LivingEntity entity, VampireComponent component) {
        return false;
    }

    public boolean canTickCooldown(LivingEntity entity, VampireComponent vampireComponent) {
        return true;
    }

    public boolean incompatibleWith(VampireAbility ability) {
        return incompatibilities
                .stream()
                .map(Supplier::get)
                .anyMatch(a -> a == ability)
                || ability.incompatibilities
                .stream()
                .map(Supplier::get)
                .anyMatch(a -> a == ability);
    }

    public List<VampireAbility> getIncompatibilities() {
        return incompatibilities.stream().map(Supplier::get).toList();
    }
    public VampireAbility incompatible(Supplier<VampireAbility> abilitySupplier) {
        incompatibilities.add(abilitySupplier);
        return this;
    }
}
