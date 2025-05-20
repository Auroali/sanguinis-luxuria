package com.auroali.sanguinisluxuria.common.components;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.blood.BloodConstants;
import com.auroali.sanguinisluxuria.common.registry.BLStatusEffects;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class BloodDrainComponent implements Component, ServerTickingComponent, AutoSyncedComponent, EntityTrackingDrainer {
    private static final int ENTITY_TRACKING_TICKS = 3600;
    private final LivingEntity holder;
    private LivingEntity target;
    private int ticksDraining;
    private boolean targetHasBleeding;
    private Entity lastDrained;
    private int lastDrainedTimer;

    public BloodDrainComponent(LivingEntity holder) {
        this.holder = holder;
    }

    public void beginDrain(LivingEntity entity) {
        if (!VampireHelper.isVampire(this.holder) || VampireHelper.isMasked(this.holder))
            return;

        this.target = entity;
        this.targetHasBleeding = entity.hasStatusEffect(BLStatusEffects.BLEEDING);
        BLEntityComponents.BLOOD_DRAIN_COMPONENT.sync(this.holder);
    }

    public void cancelDrain() {
        this.target = null;
        this.ticksDraining = 0;
        BLEntityComponents.BLOOD_DRAIN_COMPONENT.sync(this.holder);
    }

    public int getTimeToDrain() {
        return this.targetHasBleeding
          ? BloodConstants.BLOOD_DRAIN_TIME_BLEEDING
          : BloodConstants.BLOOD_DRAIN_TIME;
    }

    public int getTimeDraining() {
        return this.ticksDraining;
    }

    @Override
    public void serverTick() {
        if (this.getLastDrained() != null && !this.getLastDrained().isAlive() || this.lastDrainedTimer <= 0) {
            this.setLastDrained(null);
        }

        if (this.lastDrainedTimer > 0)
            this.lastDrainedTimer--;

        if (this.target == null)
            return;

        if (VampireHelper.isMasked(this.holder)) {
            this.cancelDrain();
            return;
        }

        HitResult result = VampireHelper.raycastEntity(
          this.holder,
          this.holder.getRotationVector(),
          Entity::isAlive
        );
        switch (result.getType()) {
            case ENTITY -> {
                if (((EntityHitResult) result).getEntity() != this.target && ((EntityHitResult) result).getEntity() instanceof LivingEntity newTarget) {
                    if (!VampireHelper.hasBlood(newTarget)) {
                        this.cancelDrain();
                        return;
                    }
                    this.target = newTarget;
                    this.ticksDraining = 0;
                }
            }
            default -> {
                this.cancelDrain();
                return;
            }
        }

        this.targetHasBleeding = this.target.hasStatusEffect(BLStatusEffects.BLEEDING);

        if (++this.ticksDraining >= this.getTimeToDrain()) {
            VampireComponent.handleBloodDrain(
              BLEntityComponents.VAMPIRE_COMPONENT.get(this.holder),
              this.target,
              this.holder
            );
            this.setLastDrained(this.target);
        }

        this.ticksDraining %= this.getTimeToDrain();
        BLEntityComponents.BLOOD_DRAIN_COMPONENT.sync(this.holder);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {

    }

    @Override
    public void writeToNbt(NbtCompound tag) {

    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player == this.holder;
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeVarInt(this.ticksDraining);
        buf.writeBoolean(this.targetHasBleeding);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.ticksDraining = buf.readVarInt();
        this.targetHasBleeding = buf.readBoolean();
    }

    @Override
    public void setLastDrained(Entity entity) {
        if (entity == null || !entity.isAlive()) {
            this.lastDrained = null;
            this.lastDrainedTimer = 0;
            return;
        }
        this.lastDrained = entity;
        this.lastDrainedTimer = ENTITY_TRACKING_TICKS;
    }

    @Override
    public Entity getLastDrained() {
        return this.lastDrained;
    }
}
