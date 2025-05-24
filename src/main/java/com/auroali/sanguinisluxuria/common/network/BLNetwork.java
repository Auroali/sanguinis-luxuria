package com.auroali.sanguinisluxuria.common.network;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.BloodDrainComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.items.EntityTrackingItem;
import com.auroali.sanguinisluxuria.common.network.packets.ActivateAbilityC2S;
import com.auroali.sanguinisluxuria.common.network.packets.DrainBloodC2S;
import com.auroali.sanguinisluxuria.common.network.packets.FillBloodItemC2S;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class BLNetwork {
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(ActivateAbilityC2S.ID, (packet, player, responseSender) -> {
            if (!VampireHelper.isVampire(player))
                return;
            VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(player);
            VampireAbilityContainer container = vampire.getAbilityContainer();
            if (container.has(packet.ability()))
                packet.ability().activate(player, vampire);
        });

        ServerPlayNetworking.registerGlobalReceiver(DrainBloodC2S.ID, (packet, player, responseSender) -> {
            if (!VampireHelper.isVampire(player))
                return;
            BloodDrainComponent drainer = BLEntityComponents.BLOOD_DRAIN_COMPONENT.get(player);
            HitResult result = VampireHelper.raycastEntity(player, player.getRotationVector(), Entity::isAlive);
            if (result.getType() != HitResult.Type.ENTITY)
                return;

            LivingEntity target = ((EntityHitResult) result).getEntity() instanceof LivingEntity living ? living : null;
            if (!VampireHelper.hasBlood(target))
                return;

            if (packet.draining()) {
                drainer.beginDrain(target);
            } else {
                drainer.cancelDrain();
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(FillBloodItemC2S.ID, (packet, player, responseSender) -> {
            ItemStack stack = player.getStackInHand(packet.hand());
            BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(player);
            BloodDrainComponent drainer = BLEntityComponents.BLOOD_DRAIN_COMPONENT.get(player);
            if (blood.getBlood() == 0)
                return;

            int filled = VampireHelper.fillHeldBloodStorage(player, stack, packet.hand(), 1, s -> {
                if (EntityTrackingItem.canTrackEntity(s) && drainer.getLastDrained() != null) {
                    EntityTrackingItem.setEntity(s, drainer.getLastDrained());
                    drainer.setLastDrained(null);
                }
            });

            blood.setBlood(blood.getBlood() - filled);
        });
    }
}
