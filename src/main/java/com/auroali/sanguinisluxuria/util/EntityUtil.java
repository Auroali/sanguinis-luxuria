package com.auroali.sanguinisluxuria.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class EntityUtil {
    /**
     * Teleports an entity to a random position near their current one. Has the same behaviour as a chorus fruit
     *
     * @param entity the entity to teleport
     */
    public static void teleportRandomly(LivingEntity entity, int radius) {
        World world = entity.getWorld();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        for (int i = 0; i < 16; ++i) {
            double newPosX = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * radius;
            double newPosY = MathHelper.clamp(
              entity.getY() + (double) (entity.getRandom().nextInt(radius) - radius / 2.f),
              world.getBottomY(),
              (world.getBottomY() + ((ServerWorld) world).getLogicalHeight() - 1)
            );
            double newPosZ = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * radius;
            if (entity.hasVehicle()) {
                entity.stopRiding();
            }

            Vec3d pos = entity.getPos();
            if (entity.teleport(newPosX, newPosY, newPosZ, true)) {
                world.emitGameEvent(GameEvent.TELEPORT, pos, GameEvent.Emitter.of(entity));
                SoundEvent soundEvent = SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                world.playSound(null, x, y, z, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                entity.playSound(soundEvent, 1.0F, 1.0F);
                break;
            }
        }
    }
}
