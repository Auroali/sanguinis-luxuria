package com.auroali.sanguinisluxuria.common.entities.goals;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.BLVampireAbilities;
import com.auroali.sanguinisluxuria.mixin.MeleeAttackGoalAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;

public class VampireAttackGoal extends MeleeAttackGoal {
    public VampireAttackGoal(PathAwareEntity mob, float speed, boolean pauseWhenIdle) {
        super(mob, speed, pauseWhenIdle);
    }

    @Override
    public boolean canStart() {
        if(mob.getTarget() != null) {
            MobEntity mob = ((MeleeAttackGoalAccessor)this).sanguinisluxuria$getMob();
            VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(mob);
            VampireAbilityContainer container = vampire.getAbilties();
            double range = Math.pow(BLVampireAbilities.TELEPORT.getRange(container), 2);
            if(!container.isOnCooldown(BLVampireAbilities.TELEPORT) && container.hasAbility(BLVampireAbilities.TELEPORT) && mob.distanceTo(mob.getTarget()) > range)
                return false;
        }
        return super.canStart();
    }

    @Override
    public boolean shouldContinue() {
        LivingEntity target = mob.getTarget();
        boolean pauseWhenMobIdle = ((MeleeAttackGoalAccessor)this).sanguinisluxuria$pauseWhenMobIdle();
        MobEntity mob = ((MeleeAttackGoalAccessor)this).sanguinisluxuria$getMob();
        VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(mob);
        VampireAbilityContainer container = vampire.getAbilties();
        double range = Math.pow(BLVampireAbilities.TELEPORT.getRange(container), 2);
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if(!container.isOnCooldown(BLVampireAbilities.TELEPORT) && container.hasAbility(BLVampireAbilities.TELEPORT) && mob.distanceTo(target) > range) {
            return false;
        } else if (!pauseWhenMobIdle) {
            return !this.mob.getNavigation().isIdle();
        } else if (!this.mob.isInWalkTargetRange(target.getBlockPos())) {
            return false;
        } else {
            return !(target instanceof PlayerEntity) || !target.isSpectator() && !((PlayerEntity)target).isCreative();
        }
    }
}
