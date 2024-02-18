package com.auroali.bloodlust.mixin;

import com.auroali.bloodlust.common.components.BLEntityComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolemEntity.class)
public abstract class IronGolemEntityMixin extends GolemEntity implements Angerable {
    protected IronGolemEntityMixin(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("HEAD"))
    public void bloodlust$makeIronGolemsAngryAtPlayers(CallbackInfo ci) {
        targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, e -> BLEntityComponents.VAMPIRE_COMPONENT.get(e).isVampire()));
    }
}
