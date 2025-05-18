package com.auroali.sanguinisluxuria.common.abilities.active;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.abilities.SyncableVampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.abilities.passive.InfectiousAbility;
import com.auroali.sanguinisluxuria.common.components.EntityTrackingDrainer;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.BLDamageSources;
import com.auroali.sanguinisluxuria.common.registry.BLParticles;
import com.auroali.sanguinisluxuria.common.registry.BLStatusEffects;
import com.auroali.sanguinisluxuria.common.registry.BLVampireAbilities;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class BiteAbility extends VampireAbility {
    @Override
    public void activate(LivingEntity entity, VampireComponent component) {
        VampireAbilityContainer.AbilityEntry entry = component.getAbilties().getAbility(this);
        if (entry.isOnCooldown() || VampireHelper.isMasked(entity))
            return;

        HitResult result = this.getTarget(entity);
        if (result.getType() != HitResult.Type.ENTITY)
            return;

        LivingEntity target = ((EntityHitResult) result).getEntity() instanceof LivingEntity e ? e : null;
        if (target == null)
            return;

        target.damage(BLDamageSources.bite(entity), 3);
        target.addStatusEffect(new StatusEffectInstance(BLStatusEffects.BLEEDING, 100, 0));
        // spawn particles
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            Box entityBox = target.getBoundingBox();

            serverWorld.spawnParticles(
              BLParticles.DRIPPING_BLOOD,
              entityBox.getCenter().getX(),
              entityBox.getCenter().getY(),
              entityBox.getCenter().getZ(),
              20,
              entityBox.getXLength() / 2.d,
              entityBox.getYLength() / 2.d,
              entityBox.getZLength() / 2.d,
              0.d
            );
        }
        if (component.getAbilties().hasAbility(BLVampireAbilities.INFECTIOUS)) {
            SyncableVampireAbility.syncAbility(
              entity,
              BLVampireAbilities.INFECTIOUS,
              InfectiousAbility.InfectiousData.create(target, VampireHelper.transferStatusEffects(entity, target))
            );
        }
        if (component instanceof EntityTrackingDrainer drainer && target.isAlive()) {
            drainer.setLastDrained(target);
        }
        entry.setCooldown(220);
    }

    private HitResult getTarget(LivingEntity entity) {
        double reachDistance = ReachEntityAttributes.getAttackRange(entity, 3.0);
        Vec3d start = entity.getEyePos();
        Vec3d end = start.add(entity.getRotationVector().multiply(reachDistance));

        HitResult result = entity.getWorld().raycast(new RaycastContext(
          start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity
        ));

        Vec3d vec3d2 = entity.getRotationVec(1.0F);
        Vec3d vec3d3 = start.add(vec3d2.x * reachDistance, vec3d2.y * reachDistance, vec3d2.z * reachDistance);

        Box box = entity.getBoundingBox().stretch(vec3d2.multiply(reachDistance)).expand(1.0, 1.0, 1.0);

        double d = reachDistance * reachDistance;
        if (result != null)
            d = result.getPos().squaredDistanceTo(start);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(entity, start, vec3d3, box, e -> !e.isSpectator() && e.canHit(), d);
        if (entityHitResult != null) {
            double g = start.squaredDistanceTo(entityHitResult.getPos());
            if (g < d || result == null) {
                return entityHitResult;
            }
        }
        return result;
    }
}
