package com.auroali.sanguinisluxuria.common.abilities;

import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.BLRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
    private final List<VampireAbilityCondition> conditions = new ArrayList<>();
    private int skillPoints;
    private String transKey;
    private final RegistryEntry.Reference<VampireAbility> holder = BLRegistry.VAMPIRE_ABILITIES.createEntry(this);
    public VampireAbility(Supplier<ItemStack> icon, VampireAbility parent) {
        this.icon = icon;
        this.parent = parent;
        this.incompatibilities = new ArrayList<>();
        this.skillPoints = 1;
    }

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

    public boolean isHidden(PlayerEntity entity) {
        if(conditions.isEmpty())
            return false;
        VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(entity);
        for(VampireAbilityCondition condition : conditions) {
            if(!condition.test(entity, vampire, vampire.getAbilties()))
                return true;
        }
        return false;
    }

    public VampireAbility condition(VampireAbilityCondition condition) {
        this.conditions.add(condition);
        return this;
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

    public int getRequiredSkillPoints() {
        return skillPoints;
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

    public void onUnVampire(LivingEntity entity, VampireComponent vampire) {
        this.onAbilityRemoved(entity, vampire);
    }

    public void onAbilityRemoved(LivingEntity entity, VampireComponent vampire) {}

    public List<VampireAbility> getIncompatibilities() {
        return incompatibilities.stream().map(Supplier::get).toList();
    }

    public VampireAbility incompatible(Supplier<VampireAbility> abilitySupplier) {
        incompatibilities.add(abilitySupplier);
        return this;
    }

    public VampireAbility skillPoints(int points) {
        this.skillPoints = points;
        return this;
    }

    @FunctionalInterface
    public interface VampireAbilityCondition {
        boolean test(PlayerEntity entity, VampireComponent vampire, VampireAbilityContainer container);
    }
}
