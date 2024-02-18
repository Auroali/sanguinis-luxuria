package com.auroali.bloodlust.common.components.impl;

import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.BloodComponent;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.auroali.bloodlust.common.registry.BLTags;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
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
        if(!blood.hasBlood() || !blood.drainBlood())
            return;

        System.out.println("added food");
        holder.getHungerManager().add(1, 0.25f);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        isVampire = tag.getBoolean("IsVampire");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("IsVampire", isVampire);
    }

    @Override
    public void serverTick() {
        if(!isVampire)
            return;

        if(isAffectedByDaylight())
            holder.setOnFireFor(8);

        if(target != null) {
            tickBloodDrain();
        }
    }

    public void tickBloodDrain() {
        updateTarget();
        if(target == null) {
            bloodDrainTimer = 0;
            BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
            return;
        }

        bloodDrainTimer++;
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
            return f > 0.5F && holder.getRandom().nextFloat() * 30.0F < (f - 0.4F) * 2.0F && !bl && holder.world.isSkyVisible(blockPos);
        }

        return false;
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeBoolean(isVampire);
        buf.writeInt(bloodDrainTimer);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        isVampire = buf.readBoolean();
        bloodDrainTimer = buf.readInt();
    }

    @Override
    public void tryStartSuckingBlood() {
        if(target == null)
            updateTarget();

    }

    @Override
    public void stopSuckingBlood() {
        target = null;
        bloodDrainTimer = 0;
        BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
    }

    @Override
    public int getBloodDrainTimer() {
        return bloodDrainTimer;
    }

    public void updateTarget() {
        HitResult result = getTarget();
        if(result.getType() != HitResult.Type.ENTITY) {
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
