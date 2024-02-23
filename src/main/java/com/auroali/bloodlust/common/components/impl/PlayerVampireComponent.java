package com.auroali.bloodlust.common.components.impl;

import com.auroali.bloodlust.VampireHelper;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.BloodComponent;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.auroali.bloodlust.common.items.BloodStorageItem;
import com.auroali.bloodlust.common.registry.BLSounds;
import com.auroali.bloodlust.common.registry.BLTags;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class PlayerVampireComponent implements VampireComponent {
    private final PlayerEntity holder;
    private boolean isVampire;
    private LivingEntity target;
    private int bloodDrainTimer;
    private int timeInSun;


    public PlayerVampireComponent(PlayerEntity holder) {
        this.holder = holder;
    }

    @Override
    public boolean isVampire() {
        return isVampire;
    }

    @Override
    public void setIsVampire(boolean isVampire) {
        this.isVampire = isVampire;
        BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
    }

    @Override
    public void drainBloodFrom(LivingEntity entity) {
        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(entity);
        if(!blood.hasBlood() || !blood.drainBlood(holder))
            return;

        if(entity.getType().isIn(BLTags.Entities.GOOD_BLOOD))
            holder.getHungerManager().add(2, 0.05f);
        else
            holder.getHungerManager().add(1, 0);

        if(entity.getType().isIn(BLTags.Entities.TOXIC_BLOOD))
            addToxicBloodEffects();

        if(entity.world instanceof ServerWorld serverWorld && entity instanceof VillagerEntity villager) {
            serverWorld.handleInteraction(EntityInteraction.VILLAGER_HURT, holder, villager);
            if(holder.getRandom().nextDouble() > 0.5f)
                entity.wakeUp();
        }
    }

    public void addToxicBloodEffects() {
        holder.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 300, 3));
        holder.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 0));
        if(holder.getRandom().nextDouble() > 0.75)
            holder.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        isVampire = tag.getBoolean("IsVampire");
        timeInSun = tag.getInt("TimeInSun");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("IsVampire", isVampire);
        tag.putInt("TimeInSun", timeInSun);
    }

    @Override
    public void serverTick() {
        if(!isVampire)
            return;

        tickSunEffects();

        if(target != null) {
            tickBloodDrain();
        }
    }

    public void tickSunEffects() {
        if(!isAffectedByDaylight()) {
            if(timeInSun > 0) {
                timeInSun = 0;
                BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
            }
            return;
        }

        if(timeInSun < getMaxTimeInSun()) {
            timeInSun++;
            BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
            return;
        }

        holder.setOnFireFor(8);
    }

    public void tickBloodDrain() {
        updateTarget();
        if(target == null) {
            bloodDrainTimer = 0;
            BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
            return;
        }

        bloodDrainTimer++;

        target.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SLOWNESS,
                2,
                4,
                true,
                false,
                false
        ));

        if(bloodDrainTimer % 4 == 0)
            holder.world.playSound(
                    null,
                    holder.getX(),
                    holder.getY(),
                    holder.getZ(),
                    BLSounds.DRAIN_BLOOD,
                    SoundCategory.PLAYERS,
                    0.5f,
                    0.85f
            );
        if(bloodDrainTimer >= BLOOD_TIMER_LENGTH) {
            drainBloodFrom(target);
            bloodDrainTimer = 0;
        }

        BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
    }

    // from MobEntity
    public boolean isAffectedByDaylight() {
        if (holder.world.isDay() && !holder.world.isClient) {
            float f = holder.getBrightnessAtEyes();
            BlockPos blockPos = new BlockPos(holder.getX(), holder.getEyeY(), holder.getZ());
            boolean bl = holder.isWet() || holder.inPowderSnow || holder.wasInPowderSnow;
            return f > 0.5F
                    && !bl
                    && holder.world.isSkyVisible(blockPos);
        }

        return false;
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeBoolean(isVampire);
        buf.writeInt(bloodDrainTimer);
        buf.writeInt(timeInSun);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        isVampire = buf.readBoolean();
        bloodDrainTimer = buf.readInt();
        timeInSun = buf.readInt();
    }

    @Override
    public void tryStartSuckingBlood() {
        if(canDrainBlood() && target == null) {
            updateTarget();
            if(target == null)
                tryToFillStorage();
        }
    }

    public void tryToFillStorage() {
        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(holder);
        if(blood.getBlood() == 0)
            return;

        if(BloodStorageItem.tryAddBloodToItemInHand(holder, 1))
            blood.drainBlood();
    }

    @Override
    public void stopSuckingBlood() {
        target = null;
        bloodDrainTimer = 0;
        BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
    }

    boolean canDrainBlood() {
        return !VampireHelper.isMasked(holder);
    }

    @Override
    public int getBloodDrainTimer() {
        return bloodDrainTimer;
    }

    @Override
    public int getMaxTimeInSun() {
        int maxTime = 40;
        ItemStack helmet = holder.getEquippedStack(EquipmentSlot.HEAD);
        if(helmet.isEmpty())
            return maxTime;

        if(helmet.isIn(BLTags.Items.SUN_BLOCKING_HELMETS))
            maxTime *= 4;

        // todo: add enchantment to protect from the sun?
        return maxTime;
    }

    @Override
    public int getTimeInSun() {
        return timeInSun;
    }

    public void updateTarget() {
        HitResult result = getTarget();
        if(!canDrainBlood() || result.getType() != HitResult.Type.ENTITY) {
            target = null;
            bloodDrainTimer = 0;
            return;
        }

        LivingEntity entity = ((EntityHitResult) result).getEntity() instanceof LivingEntity living ? living : null;

        if(entity == null
                || !entity.getType().isIn(BLTags.Entities.HAS_BLOOD)
                || !BLEntityComponents.BLOOD_COMPONENT.get(entity).hasBlood()
                || BLEntityComponents.BLOOD_COMPONENT.get(entity).getBlood() == 0
        ) {
            target = null;
            bloodDrainTimer = 0;
            return;
        }

        target = entity;
    }

    public HitResult getTarget() {
        double reachDistance = ReachEntityAttributes.getAttackRange(holder, 3.0);
        Vec3d start = holder.getEyePos();
        Vec3d end = start.add(holder.getRotationVector().multiply(reachDistance));

        HitResult result = holder.world.raycast(new RaycastContext(
                start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, holder
        ));

        Vec3d vec3d2 = holder.getRotationVec(1.0F);
        Vec3d vec3d3 = start.add(vec3d2.x * reachDistance, vec3d2.y * reachDistance, vec3d2.z * reachDistance);

        Box box = holder.getBoundingBox().stretch(vec3d2.multiply(reachDistance)).expand(1.0, 1.0, 1.0);

        double d = reachDistance * reachDistance;
        if(result != null)
            d = result.getPos().squaredDistanceTo(start);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(holder, start, vec3d3, box, entity -> !entity.isSpectator() && entity.canHit(), d);
        if(entityHitResult != null) {
            double g = start.squaredDistanceTo(entityHitResult.getPos());
            if (g < d || result == null) {
                return entityHitResult;
            }
        }
        return result;
    }
}
