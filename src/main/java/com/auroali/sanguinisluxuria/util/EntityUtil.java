package com.auroali.sanguinisluxuria.util;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.function.Predicate;

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

    public static HitResult raycastEntity(LivingEntity entity, Vec3d direction, Predicate<Entity> predicate, double distance) {
        Vec3d start = entity.getEyePos();

        Vec3d end = start.add(direction.x * distance, direction.y * distance, direction.z * distance);

        HitResult result = entity.getWorld().raycast(new RaycastContext(
          start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity
        ));

        Box box = entity.getBoundingBox().stretch(direction.multiply(distance)).expand(1.0, 1.0, 1.0);

        double targetDistance = distance * distance;
        if (result != null)
            targetDistance = result.getPos().squaredDistanceTo(start);

        EntityHitResult entityHitResult = ProjectileUtil.raycast(entity, start, end, box, predicate, targetDistance);
        if (entityHitResult != null) {
            double entityDistance = start.squaredDistanceTo(entityHitResult.getPos());
            if (entityDistance < targetDistance || result == null) {
                return entityHitResult;
            }
        }
        return result;
    }

    public static HitResult raycastEntity(LivingEntity entity, Vec3d direction, Predicate<Entity> predicate) {
        double reach = ReachEntityAttributes.getReachDistance(entity, 4.5d);
        return raycastEntity(entity, direction, predicate, reach);
    }
}
