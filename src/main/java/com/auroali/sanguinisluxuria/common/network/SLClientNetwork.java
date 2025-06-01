package com.auroali.sanguinisluxuria.common.network;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.abilities.SyncableVampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.network.packets.AltarRecipeStartS2C;
import com.auroali.sanguinisluxuria.common.network.packets.EmitParticlesS2C;
import com.auroali.sanguinisluxuria.common.registry.SLRegistries;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class SLClientNetwork {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(SLResources.ABILITY_SYNC_CHANNEL, (client, handler, buf, responseSender) -> {
            int id = buf.readVarInt();
            VampireAbility ability = buf.readRegistryValue(SLRegistries.VAMPIRE_ABILITIES);
            if (client.world != null && client.world.getEntityById(id) instanceof LivingEntity entity && ability instanceof SyncableVampireAbility<?> s)
                s.handlePacket(entity, buf, client::execute);
        });

        ClientPlayNetworking.registerGlobalReceiver(AltarRecipeStartS2C.ID, (packet, player, responseSender) -> {
            World world = player.getWorld();
            final int density = 4;
            for (BlockPos pedestalPos : packet.pedestals()) {
                for (int i = 0; i < pedestalPos.getManhattanDistance(packet.pos()) * density; i++) {
                    Vec3d pos = pedestalPos.toCenterPos();
                    Vec3d offset = packet.pos().toCenterPos().subtract(pedestalPos.toCenterPos())
                      .normalize()
                      .multiply((double) i / density);
                    pos = pos.add(offset);
                    world.addParticle(
                      DustParticleEffect.DEFAULT,
                      pos.getX() + world.getRandom().nextGaussian() * 0.07,
                      pos.getY() + world.getRandom().nextGaussian() * 0.07,
                      pos.getZ() + world.getRandom().nextGaussian() * 0.07,
                      0,
                      0,
                      0
                    );
                }
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(EmitParticlesS2C.ID, (packet, player, responseSender) -> {
            Random random = player.getRandom();
            for (int i = 0; i < packet.count(); i++) {
                double velocityX = random.nextGaussian() * packet.varianceX() + packet.velocityX();
                double velocityY = random.nextGaussian() * packet.varianceY() + packet.velocityY();
                double velocityZ = random.nextGaussian() * packet.varianceZ() + packet.velocityZ();
                player.getWorld().addParticle(
                  packet.parameters(),
                  packet.x(),
                  packet.y(),
                  packet.z(),
                  velocityX,
                  velocityY,
                  velocityZ
                );
            }
        });
    }
}
