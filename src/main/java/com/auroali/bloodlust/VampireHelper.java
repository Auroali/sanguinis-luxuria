package com.auroali.bloodlust;

import com.auroali.bloodlust.common.abilities.VampireAbility;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.auroali.bloodlust.common.registry.BLTags;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class VampireHelper {
    /**
     * Checks whether a living entity is a vampire
     * @param entity the entity to check
     * @return whether the entity is a vampire
     */
    public static boolean isVampire(LivingEntity entity) {
        return entity instanceof PlayerEntity && BLEntityComponents.VAMPIRE_COMPONENT.get(entity).isVampire();
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

    public static boolean canBeConvertedToVampire(LivingEntity entity) {
        return BLEntityComponents.VAMPIRE_COMPONENT.isProvidedBy(entity);
    }

    public static boolean hasIncompatibleAbility(LivingEntity entity, VampireAbility ability) {
        if(!isVampire(entity))
            return false;

        VampireComponent component = BLEntityComponents.VAMPIRE_COMPONENT.get(entity);

        for(VampireAbility other : component.getAbilties()) {
            if(ability.incompatibleWith(other)) {
                return true;
            }
        }
        return false;
    }
}
