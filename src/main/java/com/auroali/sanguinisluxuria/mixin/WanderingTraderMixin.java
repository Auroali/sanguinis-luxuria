package com.auroali.sanguinisluxuria.mixin;

import com.auroali.sanguinisluxuria.VampireHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.ZoglinEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderMixin extends MerchantEntity {
    public WanderingTraderMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    public void sanguinisluxuria$preventUnmaskedVampiresFromTrading(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if(VampireHelper.isVampire(player) && !VampireHelper.isMasked(player)) {
            player.incrementStat(Stats.TALKED_TO_VILLAGER);
            cir.setReturnValue(ActionResult.success(player.world.isClient));
        }
    }

    @Inject(method = "initGoals", at = @At("RETURN"))
    public void sanguinisluxuria$makeWanderingTradersScaredOfVampires(CallbackInfo ci) {
        //this.goalSelector.add(1, new FleeEntityGoal<>(this, ZoglinEntity.class, 10.0F, 0.5, 0.5));
        this.goalSelector.add(1, new FleeEntityGoal<>(this, LivingEntity.class, 10.f, 0.5f, 0.5f, e -> VampireHelper.isVampire(e) && !VampireHelper.isMasked(e)));
    }
}
