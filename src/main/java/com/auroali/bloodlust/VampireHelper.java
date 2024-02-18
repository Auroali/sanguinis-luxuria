package com.auroali.bloodlust;

import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.items.MaskItem;
import com.auroali.bloodlust.common.registry.BLItems;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class VampireHelper {
    public static boolean isVampire(LivingEntity entity) {
        return entity instanceof PlayerEntity && BLEntityComponents.VAMPIRE_COMPONENT.get(entity).isVampire();
    }

    public static boolean isMasked(LivingEntity entity) {
        return isVampire(entity)
                && TrinketsApi.getTrinketComponent(entity)
                .map(c -> c.isEquipped(i -> i.getItem() instanceof MaskItem))
                .orElse(false);
    }
}
