package com.auroali.sanguinisluxuria.util;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.function.Predicate;

public class RaycastHelper {
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
