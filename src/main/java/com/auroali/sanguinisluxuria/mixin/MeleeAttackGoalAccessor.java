package com.auroali.sanguinisluxuria.mixin;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MeleeAttackGoal.class)
public interface MeleeAttackGoalAccessor {
    @Accessor("mob")
    PathAwareEntity sanguinisluxuria$getMob();

    @Accessor("pauseWhenMobIdle")
    boolean sanguinisluxuria$pauseWhenMobIdle();
}
