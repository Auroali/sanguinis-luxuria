package com.auroali.sanguinisluxuria.common.abilities;

import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.BLRegistry;
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

    /**
     * Called every tick for this ability
     * @param entity the entity that has the ability
     * @param component the vampire component of the entity
     * @param blood the blood component of the entity
     */
    public abstract void tick(LivingEntity entity, VampireComponent component, BloodComponent blood);

    public RegistryEntry.Reference<VampireAbility> getRegistryEntry() {
        return holder;
    }

    /**
     * @return whether this ability can be bound to a key
     */
    public boolean isKeybindable() {
        return false;
    }

    public boolean isIn(TagKey<VampireAbility> tag) {
        return getRegistryEntry().isIn(tag);
    }

    /**
     * Whether this ability should be hidden from an entity.
     * A hidden ability cannot be unlocked or viewed in the skill tree.
     * @param entity the entity to check
     * @return if this ability should be hidden
     */
    public boolean isHidden(LivingEntity entity) {
        if(conditions.isEmpty())
            return false;
        VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(entity);
        for(VampireAbilityCondition condition : conditions) {
            if(!condition.test(entity, vampire, vampire.getAbilties()))
                return true;
        }
        return false;
    }

    /**
     * Adds a condition for this ability to be visible
     * @param condition the condition to add
     * @return this ability, to allow chaining more functions
     */
    public VampireAbility condition(VampireAbilityCondition condition) {
        this.conditions.add(condition);
        return this;
    }

    /**
     * Gets this ability's parent
     * @return this ability's parent, or null if it doesn't have one
     */
    public VampireAbility getParent() {
        return parent;
    }

    /**
     * Gets the icon for this ability
     * @return the icon, as an item stack.
     * @implNote this calls the supplier passed in to the constructor
     * and does not cache the result
     */
    public ItemStack getIcon() {
        return icon.get();
    }

    /**
     * @return the ability's translation key, usually in the form 'vampire_ability.modid.ability_id'
     */
    public String getTranslationKey() {
        if(transKey == null && getRegistryEntry().getKey().isPresent()) {
            Identifier id = getRegistryEntry().getKey().get().getValue();
            transKey = "vampire_ability.%s.%s".formatted(id.getNamespace(), id.getPath());
        }
        return transKey == null ? "" : transKey;
    }

    /**
     * Gets the amount of skill points required to unlock this ability
     * @return the required skill points amount
     */
    public int getRequiredSkillPoints() {
        return skillPoints;
    }

    /**
     * Activates the ability when the bound key is pressed
     * @param entity the entity using the ability
     * @param component the entity's vampire component
     * @return if the ability successfully activated
     */
    public boolean activate(LivingEntity entity, VampireComponent component) {
        return false;
    }

    /**
     * If this ability's cooldown can be ticked
     * @param entity the entity with the ability
     * @param vampireComponent the entity's vampire component
     * @return if this ability's cooldown can be ticked
     * @see VampireTeleportAbility#canTickCooldown(LivingEntity, VampireComponent)
     */
    public boolean canTickCooldown(LivingEntity entity, VampireComponent vampireComponent) {
        return true;
    }

    /**
     * Returns if this ability is incompatible with another ability
     * @param ability the other ability
     * @return if this ability is compatible with the other ability
     */
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

    /**
     * Called when an entity's state changes from vampire to non-vampire
     * @param entity the entity
     * @param vampire the entity's vampire component
     */
    public void onUnVampire(LivingEntity entity, VampireComponent vampire) {
        this.onAbilityRemoved(entity, vampire);
    }

    /**
     * Called when this ability is removed
     * @param entity the entity that used to have this ability
     * @param vampire the entity's vampire component
     */
    public void onAbilityRemoved(LivingEntity entity, VampireComponent vampire) {}

    /**
     * Gets all abilities that this one is incompatible with
     * @return a list of abilities this one is incompatible with
     */
    public List<VampireAbility> getIncompatibilities() {
        return incompatibilities.stream().map(Supplier::get).toList();
    }

    /**
     * Marks this ability as incompatible with another
     * @param abilitySupplier the ability this one is incompatible with
     * @return this ability
     */
    public VampireAbility incompatible(Supplier<VampireAbility> abilitySupplier) {
        incompatibilities.add(abilitySupplier);
        return this;
    }

    /**
     * Sets the amount of skill points required to unlock this ability
     * @param points the amount of skill points required for this ability
     * @return this ability
     */
    public VampireAbility skillPoints(int points) {
        this.skillPoints = points;
        return this;
    }

    @FunctionalInterface
    public interface VampireAbilityCondition {
        boolean test(LivingEntity entity, VampireComponent vampire, VampireAbilityContainer container);
    }
}
