package com.auroali.sanguinisluxuria.common.abilities.active;

import com.auroali.sanguinisluxuria.common.abilities.SyncableVampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.abilities.passive.InfectiousAbility;
import com.auroali.sanguinisluxuria.common.components.EntityTrackingDrainer;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.SLDamageSources;
import com.auroali.sanguinisluxuria.common.registry.SLParticles;
import com.auroali.sanguinisluxuria.common.registry.SLStatusEffects;
import com.auroali.sanguinisluxuria.common.registry.SLVampireAbilities;
import com.auroali.sanguinisluxuria.util.EntityUtil;
import com.auroali.sanguinisluxuria.util.VampireHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;

public class BiteAbility extends VampireAbility {
    @Override
    public void activate(LivingEntity entity, VampireComponent component) {
        VampireAbilityContainer.AbilityEntry entry = component.getAbilityContainer().get(this);
        if (entry.isOnCooldown() || VampireHelper.isMasked(entity))
            return;

        HitResult result = EntityUtil.raycastEntity(entity, entity.getRotationVector(), e -> e instanceof LivingEntity);
        if (result.getType() != HitResult.Type.ENTITY)
            return;

        LivingEntity target = ((EntityHitResult) result).getEntity() instanceof LivingEntity e ? e : null;
        if (target == null)
            return;

        target.damage(SLDamageSources.bite(entity), 3);
        target.addStatusEffect(new StatusEffectInstance(SLStatusEffects.BLEEDING, 100, 0));
        // spawn particles
        if (entity.getWorld() instanceof ServerWorld serverWorld) {
            Box entityBox = target.getBoundingBox();

            serverWorld.spawnParticles(
              SLParticles.DRIPPING_BLOOD,
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
        if (component.getAbilityContainer().has(SLVampireAbilities.INFECTIOUS)) {
            SyncableVampireAbility.syncAbility(
              entity,
              SLVampireAbilities.INFECTIOUS,
              InfectiousAbility.InfectiousData.create(target, InfectiousAbility.transferStatusEffects(entity, target, true))
            );
        }
        if (component instanceof EntityTrackingDrainer drainer && target.isAlive()) {
            drainer.setLastDrained(target);
        }
        entry.setCooldown(220);
    }
}
