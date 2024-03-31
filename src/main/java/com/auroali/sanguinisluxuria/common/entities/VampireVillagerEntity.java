package com.auroali.sanguinisluxuria.common.entities;

import com.auroali.sanguinisluxuria.common.BloodConstants;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.entities.goals.TeleportWhenOutOfRangeGoal;
import com.auroali.sanguinisluxuria.common.entities.goals.VampireAttackGoal;
import com.auroali.sanguinisluxuria.common.registry.BLSounds;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class VampireVillagerEntity extends HostileEntity {
    int bloodDrainTimer;

    public VampireVillagerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tickMovement() {
        this.updateDespawnCounter();
        super.tickMovement();
    }

    @Override
    public void tick() {
        super.tick();
        if(bloodDrainTimer > 0)
            bloodDrainTimer--;

        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(this);
        if(blood.getBlood() > 1 && getHealth() < getMaxHealth() && bloodDrainTimer == 0 && blood.drainBlood()) {
            setHealth(getHealth() + 1);
            playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1.0f, 1.0f);
            bloodDrainTimer = BloodConstants.BLOOD_DRAIN_TIME * 4;
        }
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(2, new AvoidSunlightGoal(this));
        this.goalSelector.add(3, new EscapeSunlightGoal(this, 1.0));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(3, new VampireAttackGoal(this, 1.0f, false));
        this.goalSelector.add(1, new TeleportWhenOutOfRangeGoal(this));
        this.goalSelector.add(5, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.goalSelector.add(5, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
        this.goalSelector.add(5, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public boolean tryAttack(Entity target) {
        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(this);
        VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(this);
        if(target instanceof LivingEntity entity && target.getType().isIn(BLTags.Entities.HAS_BLOOD) && bloodDrainTimer == 0 && blood.getBlood() < blood.getMaxBlood()) {
            vampire.drainBloodFrom(entity);
            playSound(BLSounds.DRAIN_BLOOD, 1.0f, 1.0f);
            bloodDrainTimer = BloodConstants.BLOOD_DRAIN_TIME * 4;
            return true;
        }
        return super.tryAttack(target);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VILLAGER_AMBIENT;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("BloodDrainTimer", bloodDrainTimer);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        bloodDrainTimer = nbt.getInt("BloodDrainTimer");
    }
}
