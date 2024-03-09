package com.auroali.bloodlust.common.abilities;

import com.auroali.bloodlust.common.components.BloodComponent;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.auroali.bloodlust.common.registry.BLDamageSources;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.function.Supplier;

public class BiteAbility extends VampireAbility{
    public BiteAbility(Supplier<ItemStack> icon, VampireAbility parent) {
        super(icon, parent);
    }

    @Override
    public void tick(LivingEntity entity, VampireComponent component, BloodComponent blood) {}

    @Override
    public boolean isKeybindable() {
        return true;
    }

    @Override
    public boolean activate(LivingEntity entity, VampireComponent component) {
        if(component.getAbilties().isOnCooldown(this))
            return false;

        HitResult result = getTarget(entity);
        if(result.getType() != HitResult.Type.ENTITY)
            return false;

        LivingEntity target = ((EntityHitResult)result).getEntity() instanceof LivingEntity e ? e : null;
        if(target == null)
            return false;

        target.damage(BLDamageSources.bite(entity), 3);
        component.getAbilties().setCooldown(this, 150);
        return true;
    }

    private HitResult getTarget(LivingEntity entity) {
        double reachDistance = ReachEntityAttributes.getAttackRange(entity, 3.0);
        Vec3d start = entity.getEyePos();
        Vec3d end = start.add(entity.getRotationVector().multiply(reachDistance));

        HitResult result = entity.world.raycast(new RaycastContext(
                start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity
        ));

        Vec3d vec3d2 = entity.getRotationVec(1.0F);
        Vec3d vec3d3 = start.add(vec3d2.x * reachDistance, vec3d2.y * reachDistance, vec3d2.z * reachDistance);

        Box box = entity.getBoundingBox().stretch(vec3d2.multiply(reachDistance)).expand(1.0, 1.0, 1.0);

        double d = reachDistance * reachDistance;
        if(result != null)
            d = result.getPos().squaredDistanceTo(start);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(entity, start, vec3d3, box, e -> !e.isSpectator() && e.canHit(), d);
        if(entityHitResult != null) {
            double g = start.squaredDistanceTo(entityHitResult.getPos());
            if (g < d || result == null) {
                return entityHitResult;
            }
        }
        return result;
    }
}
