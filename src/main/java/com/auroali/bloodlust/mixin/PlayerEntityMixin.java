package com.auroali.bloodlust.mixin;

import com.auroali.bloodlust.VampireHelper;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.BloodComponent;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "updatePose", at = @At("HEAD"), cancellable = true)
    public void bloodlust$forceCrawlingWhenNoBlood(CallbackInfo ci) {
        if(!VampireHelper.isVampire(this))
            return;

        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(this);
        if(!hasVehicle() && blood.getBlood() == 0) {
            setPose(EntityPose.SWIMMING);
            ci.cancel();
        }
    }
}
