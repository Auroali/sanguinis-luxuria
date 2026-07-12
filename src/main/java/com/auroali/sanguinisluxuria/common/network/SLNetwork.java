package com.auroali.sanguinisluxuria.common.network;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.blood.BloodConstants;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.BloodDrainComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.items.EntityTrackingItem;
import com.auroali.sanguinisluxuria.common.network.packets.ActivateAbilityC2S;
import com.auroali.sanguinisluxuria.common.network.packets.DrainBloodC2S;
import com.auroali.sanguinisluxuria.common.network.packets.FillBloodItemC2S;
import com.auroali.sanguinisluxuria.util.EntityUtil;
import com.auroali.sanguinisluxuria.util.ItemUtil;
import com.auroali.sanguinisluxuria.util.VampireHelper;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class SLNetwork {
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(ActivateAbilityC2S.ID, (packet, player, responseSender) -> {
            if (!VampireHelper.isVampire(player))
                return;
            VampireComponent vampire = VampireComponent.KEY.get(player);
            VampireAbilityContainer container = vampire.getAbilityContainer();
            if (container.has(packet.ability()))
                packet.ability().activate(player, vampire);
        });

        ServerPlayNetworking.registerGlobalReceiver(DrainBloodC2S.ID, (packet, player, responseSender) -> {
            if (!VampireHelper.isVampire(player))
                return;
            BloodDrainComponent drainer = BloodDrainComponent.KEY.get(player);
            HitResult result = EntityUtil.raycastEntity(player, player.getRotationVector(), Entity::isAlive);
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
            if (!VampireHelper.isVampire(player))
                return;

            ItemStack stack = player.getStackInHand(packet.hand());
            BloodComponent blood = BloodComponent.KEY.get(player);
            BloodDrainComponent drainer = BloodDrainComponent.KEY.get(player);
            if (blood.isEmpty())
                return;

            int toFill = Math.min(BloodConstants.BLOOD_PER_BOTTLE, blood.getBlood());
            int filled = ItemUtil.fillHeldBloodStorage(player, stack, packet.hand(), toFill, s -> {
                if (player.isSneaking() && EntityTrackingItem.canTrackEntity(s) && !EntityTrackingItem.hasEntity(stack)) {
                    EntityTrackingItem.setEntity(
                      s,
                      drainer.getLastDrained() == null
                        ? player
                        : drainer.getLastDrained()
                    );
                    drainer.setLastDrained(null);
                }
            });

            blood.setBlood(blood.getBlood() - filled);
            player.getHungerManager().setSaturationLevel(BloodConstants.adjustSaturation(player, filled, -BloodConstants.SATURATION_PER_BLOOD_FILLED));
        });
    }
}
