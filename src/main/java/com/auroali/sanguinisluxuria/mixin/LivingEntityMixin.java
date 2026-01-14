package com.auroali.sanguinisluxuria.mixin;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.BloodDrainComponent;
import com.auroali.sanguinisluxuria.common.components.InitializableBloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.entities.VampireDamageHandler;
import com.auroali.sanguinisluxuria.common.registry.SLEntityAttributes;
import com.auroali.sanguinisluxuria.common.registry.SLStatusEffects;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public abstract boolean isUndead();

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow
    public abstract void remove(RemovalReason reason);

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    protected abstract void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition);

    @Shadow
    public abstract boolean removeStatusEffect(StatusEffect type);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "damage", at = @At(
      value = "HEAD"
    ), argsOnly = true)
    public float sanguinisluxuria$increaseDamage(float amount, @Local(argsOnly = true) DamageSource source) {
        float blessedDamageMod = 0.0f;
        if (this.isUndead() && source.getAttacker() instanceof LivingEntity entity && entity.getAttributes().hasAttribute(SLEntityAttributes.BLESSED_DAMAGE)) {
            blessedDamageMod += (float) entity.getAttributeValue(SLEntityAttributes.BLESSED_DAMAGE);
        }
        if (VampireHelper.isVampire(this)) {
            return blessedDamageMod + VampireDamageHandler.modifyIncomingDamage((LivingEntity) (Object) this, source, amount);
        }
        return blessedDamageMod + amount;
    }

    @Inject(method = "applyDamage", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/entity/LivingEntity;setHealth(F)V"))
    public void sanguinisluxuria$cancelBloodDrainOnDamageTaken(DamageSource source, float amount, CallbackInfo ci) {
        if (!BloodDrainComponent.KEY.isProvidedBy(this))
            return;

        BloodDrainComponent vampire = BloodDrainComponent.KEY.get(this);
        vampire.cancelDrain();
    }

    @WrapOperation(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;tryUseTotem(Lnet/minecraft/entity/damage/DamageSource;)Z"))
    public boolean sanguinisluxuria$tryPreventDeath(LivingEntity instance, DamageSource source, Operation<Boolean> original) {
        if (original.call(instance, source))
            return true;

        if (VampireDamageHandler.canKillVampire(source) || source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return false;

        if (!VampireHelper.hasBlood(instance) || !VampireHelper.isVampire(instance)) {
            return VampireHelper.attemptConvertToVampire((LivingEntity) (Object) this);
        }

        VampireComponent vampire = VampireComponent.KEY.get(instance);
        BloodComponent blood = BloodComponent.KEY.get(instance);

        instance.setHealth(Math.min(instance.getMaxHealth(), (float) blood.getBlood()));
        vampire.setDowned(true);
        blood.setBlood(0);
        this.sanguinisluxuria$notifyAttackerDowned();
        this.removeStatusEffect(SLStatusEffects.BLOOD_LUST);
        return true;
    }

    @Unique
    protected void sanguinisluxuria$notifyAttackerDowned() {
        // make entities targeting this entity stop when this one is downed
        this.getWorld().getEntitiesByType(
            TypeFilter.instanceOf(LivingEntity.class),
            this.getBoundingBox().expand(16.d),
            e ->
              e.getLastAttacker() == (Entity) this
                || e.getAttacking() == (Entity) this
                || e instanceof MobEntity m && m.getTarget() == (Entity) this
          )
          .forEach(e -> {
              if (e.getAttacking() == (Entity) this)
                  e.setAttacking(null);
              if (e.getLastAttacker() == (Entity) this)
                  e.setAttacker(null);
              if (e instanceof MobEntity m && m.getTarget() == (Entity) this)
                  m.setTarget(null);
          });
    }

    @Inject(method = "getGroup", at = @At("HEAD"), cancellable = true)
    public void sanguinisluxuria$modifyVampireGroup(CallbackInfoReturnable<EntityGroup> cir) {
        if (VampireHelper.isVampire(this))
            cir.setReturnValue(EntityGroup.UNDEAD);
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void sanguinisluxuria$addAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue()
          .add(SLEntityAttributes.BLESSED_DAMAGE)
          .add(SLEntityAttributes.BLINK_RANGE)
          .add(SLEntityAttributes.BLINK_COOLDOWN)
          .add(SLEntityAttributes.SUN_RESISTANCE)
          .add(SLEntityAttributes.VULNERABILITY);
    }

    @Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
    public void sanguinisluxuria$preventBloodLustEffectForVampires(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (effect.getEffectType() == SLStatusEffects.BLOOD_LUST && (VampireHelper.isVampire(this) || this.hasStatusEffect(SLStatusEffects.BLOOD_PROTECTION)))
            cir.setReturnValue(false);
        else if (effect.getEffectType() == StatusEffects.REGENERATION && ((Entity) this) instanceof PlayerEntity && VampireHelper.isVampire(this)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void sanguinisluxuria$initBloodComponents(EntityType<?> entityType, World world, CallbackInfo ci) {
        if (BloodComponent.KEY.isProvidedBy(this)) {
            BloodComponent blood = BloodComponent.KEY.get(this);
            if (blood instanceof InitializableBloodComponent init && !init.hasInitialized()) {
                init.initializeBloodValues();
            }
        }
    }

    @ModifyReturnValue(method = "getAttackDistanceScalingFactor", at = @At("RETURN"))
    public double sanguinisluxuria$modifyVisibility(double original) {
        if (VampireHelper.isVampire(this)) {
            VampireComponent vampire = VampireComponent.KEY.get(this);
            if (vampire.isDowned())
                return Math.min(0.1f, original);
            if (vampire.isMist())
                return Math.min(0.07f, original);
        }
        return original;
    }

    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    public void sanguinisluxuria$preventPushingWhileMist(CallbackInfoReturnable<Boolean> cir) {
        if (VampireHelper.isVampire(this) && VampireComponent.KEY.get(this).isMist())
            cir.setReturnValue(false);
    }
}
