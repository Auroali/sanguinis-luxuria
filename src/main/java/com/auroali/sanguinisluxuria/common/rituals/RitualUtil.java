package com.auroali.sanguinisluxuria.common.rituals;

import com.auroali.sanguinisluxuria.common.network.packets.EmitParticlesS2C;
import com.auroali.sanguinisluxuria.common.registry.SLParticles;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class RitualUtil {
    public static void spawnSuccessParticles(RitualParameters parameters) {
        spawnSuccessParticlesAt(parameters, parameters.pos().toCenterPos());
    }

    public static void spawnSuccessParticlesAt(RitualParameters parameters, Vec3d pos) {
        if (parameters.world() instanceof ServerWorld world) {
            EmitParticlesS2C packet = new EmitParticlesS2C(
              SLParticles.ALTAR,
              pos.getX(),
              pos.getY(),
              pos.getZ(),
              0.f,
              0.f,
              0.f,
              0.1,
              0.04f,
              0.1f,
              100
            );
            PlayerLookup.tracking(world, parameters.pos())
              .forEach(player -> ServerPlayNetworking.send(
                player, packet
              ));
        }
    }
}
