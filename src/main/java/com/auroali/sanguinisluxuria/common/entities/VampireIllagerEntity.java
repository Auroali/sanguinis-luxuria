package com.auroali.sanguinisluxuria.common.entities;

import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.auroali.sanguinisluxuria.common.blood.BloodConstants;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.entities.goals.TeleportWhenOutOfRangeGoal;
import com.auroali.sanguinisluxuria.common.registry.SLSounds;
import com.auroali.sanguinisluxuria.common.registry.SLStatusEffects;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import com.auroali.sanguinisluxuria.util.VampireHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.village.VillagerData;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class VampireIllagerEntity extends IllagerEntity {
    private int bloodDrainTimer;
    private VillagerData villagerData;
    private NbtCompound offers;
    private int xp;

    public VampireIllagerEntity(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tickMovement() {
        this.updateDespawnCounter();
        if (this.isAffectedByDaylight()) {
            this.setOnFireFor(8);
        }
        super.tickMovement();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.bloodDrainTimer > 0)
            this.bloodDrainTimer--;

        VampireComponent vampire = VampireComponent.KEY.get(this);
        if (this.getWorld().isClient && vampire.isDowned()) {
            Box box = this.getBoundingBox();
            int max = 3;
            for (int i = 0; i < max; i++) {
                double x = box.minX + this.random.nextDouble() * box.getXLength();
                double y = box.minY + this.random.nextDouble() * box.getYLength();
                double z = box.minZ + this.random.nextDouble() * box.getZLength();
                this.getWorld().addParticle(
                  DustParticleEffect.DEFAULT,
                  x,
                  y,
                  z,
                  0,
                  0,
                  0
                );
            }
        }
    }

    @Override
    public float getMovementSpeed() {
        VampireComponent vampire = VampireComponent.KEY.get(this);
        return vampire.isDowned() ? 0.5f * super.getMovementSpeed() : super.getMovementSpeed();
    }

    @Override
    protected void initGoals() {
        super.initGoals();

        this.goalSelector.add(1, new AvoidSunlightGoal(this));
        this.goalSelector.add(4, new SwimGoal(this));
        this.goalSelector.add(2, new EscapeSunlightGoal(this, 1.0));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0f, false));
        this.goalSelector.add(2, new TeleportWhenOutOfRangeGoal(this));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(6, new ActiveTargetGoal<>(this, LivingEntity.class, true, e -> {
            BloodComponent blood = BloodComponent.KEY.get(this);
            return VampireHelper.hasBlood(e) && ((double) blood.getBlood() / blood.getMaxBlood()) < 0.4;
        }));
    }

    @Override
    public void addBonusForWave(int wave, boolean unused) {

    }

    @Override
    public boolean tryAttack(Entity target) {
        BloodComponent blood = BloodComponent.KEY.get(this);
        VampireComponent vampire = VampireComponent.KEY.get(this);
        if (target instanceof LivingEntity entity && VampireHelper.hasBlood(target)) {
            // apply weakness to the target if they have little blood left
            // to attempt to convert them
            BloodComponent targetBlood = BloodComponent.KEY.get(entity);
            if (targetBlood.getBlood() < Math.min(4, targetBlood.getMaxBlood() / 6))
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 160));

            if (!vampire.isMist()
              && this.bloodDrainTimer == 0
              && blood.getBlood() < blood.getMaxBlood()
              && !this.shouldAttemptConvert(entity, blood)
            ) {
                VampireComponent.handleBloodDrain(vampire, entity, this);
                this.playSound(SLSounds.DRAIN_BLOOD, 1.0f, 1.0f);
                this.bloodDrainTimer = BloodConstants.BLOOD_DRAIN_TIME * 2;
                this.onAttacking(target);
                return true;
            }
        }
        return super.tryAttack(target);
    }

    protected boolean shouldAttemptConvert(LivingEntity target, BloodComponent blood) {
        return target.getType().isIn(SLTags.Entities.VAMPIRES_ATTEMPT_CONVERT)
          && target.hasStatusEffect(SLStatusEffects.BLOOD_LUST)
          && blood.getBlood() > blood.getMaxBlood() / 2;
    }

    public void setVillagerData(VillagerData data) {
        this.villagerData = data;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_PILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PILLAGER_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PILLAGER_AMBIENT;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("BloodDrainTimer", this.bloodDrainTimer);
        if (this.villagerData != null)
            VillagerData.CODEC.encodeStart(NbtOps.INSTANCE, this.villagerData)
              .resultOrPartial(SanguinisLuxuria.LOGGER::error)
              .ifPresent(element -> nbt.put("VillagerData", element));
        if (this.offers != null)
            nbt.put("Offers", this.offers);
        nbt.putInt("Xp", this.xp);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.bloodDrainTimer = nbt.getInt("BloodDrainTimer");
        if (nbt.contains("VillagerData"))
            VillagerData.CODEC.parse(NbtOps.INSTANCE, nbt.get("VillagerData"))
              .resultOrPartial(SanguinisLuxuria.LOGGER::error)
              .ifPresent(this::setVillagerData);
        if (nbt.contains("Offers"))
            this.offers = nbt.getCompound("Offers");
        if (nbt.contains("Xp"))
            this.xp = nbt.getInt("Xp");
    }

    @Override
    public SoundEvent getCelebratingSound() {
        return SoundEvents.ENTITY_PILLAGER_CELEBRATE;
    }

    @Override
    public State getState() {
        return State.NEUTRAL;
    }
}
