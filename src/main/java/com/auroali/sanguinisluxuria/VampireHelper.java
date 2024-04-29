package com.auroali.sanguinisluxuria;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.BLAdvancementCriterion;
import com.auroali.sanguinisluxuria.common.registry.BLStatusEffects;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

public class VampireHelper {
    /**
     * Checks whether a living entity is a vampire
     * @param entity the entity to check
     * @return whether the entity is a vampire
     */
    public static boolean isVampire(Entity entity) {
        return BLEntityComponents.VAMPIRE_COMPONENT.isProvidedBy(entity) && BLEntityComponents.VAMPIRE_COMPONENT.get(entity).isVampire();
    }

    /**
     * Checks whether an entity is both a vampire and wearing a carved mask
     * @param entity the entity to check
     * @return whether the entity is both a vampire and wearing a carved mask
     */
    public static boolean isMasked(LivingEntity entity) {
        return isVampire(entity)
                && TrinketsApi.getTrinketComponent(entity)
                .map(c -> c.isEquipped(i -> i.isIn(BLTags.Items.VAMPIRE_MASKS)))
                .orElse(false);
    }

    /**
     * Checks whether an entity can be converted to a vampire
     * @param entity the potentially convertible entity
     * @return if the entity can be converted (has a vampire component and is not currently a vampire)
     */
    public static boolean canBeConvertedToVampire(LivingEntity entity) {
        return BLEntityComponents.VAMPIRE_COMPONENT.isProvidedBy(entity) && !BLEntityComponents.VAMPIRE_COMPONENT.get(entity).isVampire();
    }

    /**
     * Checks if an entity has an ability unlocked that is incompatible with the provided ability
     * @param entity the entity to check
     * @param ability the ability to check for incompatibilities with
     * @return if the entity has an incompatible ability unlocked
     */
    public static boolean hasIncompatibleAbility(LivingEntity entity, VampireAbility ability) {
        if(!isVampire(entity))
            return false;

        VampireComponent component = BLEntityComponents.VAMPIRE_COMPONENT.get(entity);

        return hasIncompatibleAbility(component.getAbilties(), ability);
    }

    /**
     * Checks if an ability container has an ability unlocked that is incompatible with the provided ability
     * @param container the ability container to check
     * @param ability the ability to check for incompatibilities with
     * @return if the ability container has an incompatible ability unlocked
     */
    public static boolean hasIncompatibleAbility(VampireAbilityContainer container, VampireAbility ability) {
        for(VampireAbility other : container) {
            if(ability.incompatibleWith(other)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds all the toxic blood status effects to an entity
     * @param entity the entity to add the effects to
     */
    public static void addToxicBloodEffects(LivingEntity entity) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 300, 3));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 0));
        if(entity.getRandom().nextDouble() > 0.75)
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));
    }

    /**
     * Increments the level of blood sickness on an entity. If the entity does not currently have blood sickness, this will add it
     * @param entity the entity to increment the blood sickness level of
     */
    public static void incrementBloodSickness(LivingEntity entity) {
        int level = entity.hasStatusEffect(BLStatusEffects.BLOOD_SICKNESS) ? entity.getStatusEffect(BLStatusEffects.BLOOD_SICKNESS).getAmplifier() + 1 : 0;
        entity.addStatusEffect(new StatusEffectInstance(BLStatusEffects.BLOOD_SICKNESS, 3600, level));
    }

    /**
     * Transfers status effects from one entity to the other, clearing the effects from the original entity
     * @param from the entity to transfer effects from
     * @param to the entity to transfer effects to
     */
    public static void transferStatusEffects(LivingEntity from, LivingEntity to) {
        for (StatusEffectInstance instance : from.getStatusEffects()) {
            to.addStatusEffect(instance);
        }

        if (from instanceof ServerPlayerEntity player) {
            BLAdvancementCriterion.TRANSFER_EFFECTS.trigger(player, player.getStatusEffects().size());
        }

        from.clearStatusEffects();
    }
}
