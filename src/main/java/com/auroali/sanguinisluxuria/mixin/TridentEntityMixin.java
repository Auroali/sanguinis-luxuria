package com.auroali.sanguinisluxuria.mixin;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.registry.BLEnchantments;
import com.auroali.sanguinisluxuria.common.registry.BLSounds;
import com.auroali.sanguinisluxuria.common.registry.BLStatusEffects;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
    @Shadow private ItemStack tridentStack;

    @Shadow protected abstract boolean isOwnerAlive();

    @Unique
    private static final TrackedData<Byte> sanguinisluxuria$BLOOD_DRAIN = DataTracker.registerData(TridentEntity.class, TrackedDataHandlerRegistry.BYTE);

    @Unique
    private int sanguinisluxuria$latchedTicks = 0;
    @Unique
    private Entity sanguinisluxuria$latchedEntity;

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;getOwner()Lnet/minecraft/entity/Entity;", shift = At.Shift.BY, by = 2), cancellable = true)
    public void sanguinisluxuria$handleBloodDrainLogic(CallbackInfo ci, @Local(name = "entity") Entity owner) {
        int bloodDrainLevel = dataTracker.get(sanguinisluxuria$BLOOD_DRAIN);
        if(bloodDrainLevel != 0 && sanguinisluxuria$latchedEntity != null && sanguinisluxuria$latchedEntity.isAlive() && !sanguinisluxuria$latchedEntity.isRemoved()) {
            if(!this.isOwnerAlive()) {
                if (!this.world.isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1F);
                }
                this.discard();
                ci.cancel();
                return;
            }
            if(sanguinisluxuria$latchedTicks > 300) {
                sanguinisluxuria$latchedEntity = null;
                sanguinisluxuria$latchedTicks = 0;
                return;
            }

            BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(sanguinisluxuria$latchedEntity);
            BloodComponent ownerBlood = BLEntityComponents.BLOOD_COMPONENT.get(owner);

            VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(owner);

            if((sanguinisluxuria$latchedEntity instanceof LivingEntity livingTarget && livingTarget.hasStatusEffect(BLStatusEffects.BLOOD_PROTECTION)) || blood.getBlood() <= Math.max(1, blood.getMaxBlood() / (1 + bloodDrainLevel))) {
                sanguinisluxuria$latchedEntity = null;
                sanguinisluxuria$latchedTicks = 0;
                return;
            }

            setPosition(sanguinisluxuria$latchedEntity.getPos().add(0, sanguinisluxuria$latchedEntity.getEyeHeight(sanguinisluxuria$latchedEntity.getPose()) * 0.75, 0));
            setVelocity(Vec3d.ZERO);

            if(sanguinisluxuria$latchedTicks % 20 == 0 && !world.isClient && blood.drainBlood()) {
                if(!(owner instanceof LivingEntity entity && BloodStorageItem.tryAddBloodToItemInHand(entity, 1)) && vampire.isVampire()) {
                    ownerBlood.addBlood(1);
                }
                playSound(BLSounds.DRAIN_BLOOD, 1.0f, 1.0f);
            }

            sanguinisluxuria$latchedTicks++;
            ci.cancel();
        }
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
    public void sanguinisluxuria$initTrackedData(World world, LivingEntity owner, ItemStack stack, CallbackInfo ci) {
        dataTracker.set(sanguinisluxuria$BLOOD_DRAIN, (byte) EnchantmentHelper.getLevel(BLEnchantments.BLOOD_DRAIN, stack));
    }

    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/TridentEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"))
    public void sanguinisluxuria$latchOnEntity(EntityHitResult entityHitResult, CallbackInfo ci, @Local(name = "entity") Entity target, @Local(name = "entity2") Entity owner) {
        if(dataTracker.get(sanguinisluxuria$BLOOD_DRAIN) != 0 && VampireHelper.isVampire(owner) && target.getType().isIn(BLTags.Entities.HAS_BLOOD)) {
            if(target instanceof LivingEntity livingTarget && livingTarget.hasStatusEffect(BLStatusEffects.BLOOD_PROTECTION))
                return;
            sanguinisluxuria$latchedEntity = target;
            sanguinisluxuria$latchedTicks = 0;
        }
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    public void sanguinisluxuria$injectTrackedData(CallbackInfo ci) {
        this.dataTracker.startTracking(sanguinisluxuria$BLOOD_DRAIN, (byte) 0);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void sanguinisluxuria$readBloodDrainFromNbt(NbtCompound nbt, CallbackInfo ci) {
        dataTracker.set(sanguinisluxuria$BLOOD_DRAIN, (byte) EnchantmentHelper.getLevel(BLEnchantments.BLOOD_DRAIN, tridentStack));
    }
}
