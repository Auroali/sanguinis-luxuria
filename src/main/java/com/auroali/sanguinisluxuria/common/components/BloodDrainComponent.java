package com.auroali.sanguinisluxuria.common.components;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.blood.BloodConstants;
import com.auroali.sanguinisluxuria.common.registry.BLSounds;
import com.auroali.sanguinisluxuria.common.registry.BLStatusEffects;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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

    /**
     * Begins draining an entity
     *
     * @param entity the entity to drain
     */
    public void beginDrain(LivingEntity entity) {
        if (!VampireHelper.isVampire(this.holder) || VampireHelper.isMasked(this.holder) || !VampireHelper.hasBlood(entity) || this.target == entity)
            return;

        this.target = entity;
        this.ticksDraining = 0;
        this.targetHasBleeding = entity.hasStatusEffect(BLStatusEffects.BLEEDING);
        BLEntityComponents.BLOOD_DRAIN_COMPONENT.sync(this.holder);
    }

    /**
     * Cancels the current drain, clearing the target entity
     * and drain timer
     */
    public void cancelDrain() {
        this.target = null;
        this.ticksDraining = 0;
        BLEntityComponents.BLOOD_DRAIN_COMPONENT.sync(this.holder);
    }

    /**
     * @return the amount of time required to drain one unit of blood
     * from the target entity
     */
    public int getTimeToDrain() {
        return this.targetHasBleeding
          ? BloodConstants.BLOOD_DRAIN_TIME_BLEEDING
          : BloodConstants.BLOOD_DRAIN_TIME;
    }

    /**
     * @return the current amount of time the holding entity has
     * been draining the target entity for
     */
    public int getTimeDraining() {
        return this.ticksDraining;
    }

    private void applyDrainEffects() {
        if (this.target.getWorld().getTime() % 2 == 0)
            this.target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 4, 6, true, false, false));
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
                Entity entity = ((EntityHitResult) result).getEntity();
                if (entity != this.target) {
                    if (!(entity instanceof LivingEntity) || !VampireHelper.hasBlood(entity)) {
                        this.cancelDrain();
                        return;
                    }
                    this.target = (LivingEntity) entity;
                    this.ticksDraining = 0;
                }
            }
            default -> {
                this.cancelDrain();
                return;
            }
        }

        this.targetHasBleeding = this.target.hasStatusEffect(BLStatusEffects.BLEEDING);

        this.applyDrainEffects();

        if (++this.ticksDraining >= this.getTimeToDrain()) {
            this.holder.getWorld().playSound(
              null,
              this.holder.getX(),
              this.holder.getY(),
              this.holder.getZ(),
              BLSounds.DRAIN_BLOOD,
              this.holder.getSoundCategory(),
              1.0f,
              0.9f + this.holder.getRandom().nextFloat() * 0.1f
            );
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
        // do nothing
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        // do nothing
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        // only sync with the holding player
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
