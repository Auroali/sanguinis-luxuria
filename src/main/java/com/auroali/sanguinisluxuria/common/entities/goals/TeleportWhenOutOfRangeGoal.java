package com.auroali.sanguinisluxuria.common.entities.goals;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.abilities.active.VampireTeleportAbility;
import com.auroali.sanguinisluxuria.common.components.SLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.SLVampireAbilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;

import java.util.EnumSet;

public class TeleportWhenOutOfRangeGoal extends Goal {
    private final MobEntity mob;
    private LivingEntity target;

    public TeleportWhenOutOfRangeGoal(MobEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity livingEntity = this.mob.getTarget();
        VampireAbilityContainer container = VampireComponent.KEY.get(this.mob).getAbilityContainer();
        if (!container.has(SLVampireAbilities.TELEPORT))
            return false;

        VampireAbilityContainer.AbilityEntry entry = container.get(SLVampireAbilities.TELEPORT);
        if (entry == null)
            return false;

        if (livingEntity == null || !this.mob.getVisibilityCache().canSee(livingEntity) || entry.isOnCooldown() || livingEntity.getPos().distanceTo(this.mob.getPos()) < VampireTeleportAbility.getRange(this.mob)) {
            return false;
        } else {
            this.target = livingEntity;
            return true;
        }
    }

    @Override
    public boolean shouldContinue() {
        VampireAbilityContainer container = VampireComponent.KEY.get(this.mob).getAbilityContainer();
        double teleportRange = Math.pow(VampireTeleportAbility.getRange(this.mob), 2);

        VampireAbilityContainer.AbilityEntry entry = container.get(SLVampireAbilities.TELEPORT);
        if (entry == null)
            return false;

        if (!this.target.isAlive()) {
            return false;
        } else if (!this.mob.getVisibilityCache().canSee(this.target) || entry.isOnCooldown() || this.mob.squaredDistanceTo(this.target) < teleportRange) {
            return false;
        } else {
            return !this.mob.getNavigation().isIdle() || this.canStart();
        }
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.mob.getLookControl().lookAt(this.target, 360F, 360F);
        SLVampireAbilities.TELEPORT.activate(this.mob, VampireComponent.KEY.get(this.mob));
    }
}