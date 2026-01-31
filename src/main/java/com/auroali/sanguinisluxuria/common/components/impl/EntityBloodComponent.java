package com.auroali.sanguinisluxuria.common.components.impl;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.blood.BloodConstants;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.InitializableBloodComponent;
import com.auroali.sanguinisluxuria.common.registry.SLDamageSources;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import com.auroali.sanguinisluxuria.util.VampireHelper;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;

public class EntityBloodComponent implements InitializableBloodComponent, ServerTickingComponent {
    private final LivingEntity holder;
    private int maxBlood;
    private int currentBlood;
    private int bloodGainTimer;
    private boolean wasBaby;

    public EntityBloodComponent(LivingEntity holder) {
        this.holder = holder;
        this.maxBlood = -1;
        this.currentBlood = -1;
    }

    @Override
    public void initializeBloodValues(boolean sync) {
        if (!this.holder.getType().isIn(SLTags.Entities.HAS_BLOOD)) {
            this.maxBlood = 0;
            this.currentBlood = 0;
            this.wasBaby = this.holder.isBaby();
            if (sync)
                BloodComponent.KEY.sync(this.holder);
            return;
        }

        boolean needsToSetBlood = this.maxBlood == 0 || this.currentBlood == -1;
        // if an entity isn't in the good blood tag, half the max amount of blood
        this.maxBlood = this.recalculateMaxBlood();
        // set the current blood value if it either is invalid or if this entity previously had no blood
        if (needsToSetBlood)
            this.currentBlood = this.maxBlood;
        this.currentBlood = Math.min(this.currentBlood, this.maxBlood);
        this.wasBaby = this.holder.isBaby();

        if (sync)
            BloodComponent.KEY.sync(this.holder);
    }

    @Override
    public void initializeBloodValues() {
        this.initializeBloodValues(this.holder.getWorld() instanceof ServerWorld);
    }

    @Override
    public boolean hasInitialized() {
        if (this.holder.getType().isIn(SLTags.Entities.HAS_BLOOD)) {
            return this.maxBlood > 0;
        }
        return this.maxBlood == 0;
    }

    protected int recalculateMaxBlood() {
        if (this.holder.isBaby())
            return 1;

        float maxBloodFromHealth = (float) this.holder.getAttributeBaseValue(EntityAttributes.GENERIC_MAX_HEALTH);
        if (!this.holder.getType().isIn(SLTags.Entities.GOOD_BLOOD))
            maxBloodFromHealth = MathHelper.clamp(maxBloodFromHealth / 2.f, 1.f, Float.MAX_VALUE);
        return (int) Math.ceil(maxBloodFromHealth);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.currentBlood = Math.min(tag.getInt("Blood"), this.maxBlood);
        this.bloodGainTimer = tag.getInt("BloodTimer");
        this.wasBaby = tag.getBoolean("Baby");
        if (!this.hasInitialized() || this.wasBaby != this.holder.isBaby())
            this.initializeBloodValues();
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("Blood", this.currentBlood);
        tag.putInt("BloodTimer", this.bloodGainTimer);
        tag.putBoolean("Baby", this.wasBaby);
    }

    @Override
    public int getBlood() {
        return this.currentBlood;
    }

    @Override
    public int getMaxBlood() {
        return this.maxBlood;
    }

    @Override
    public void setBlood(int amount) {
        this.currentBlood = amount;
        this.bloodGainTimer = 0;
        if (this.currentBlood == 0)
            this.killHolderFromBloodloss(null);
        BloodComponent.KEY.sync(this.holder);
    }

    @Override
    public boolean drainBlood(int amount, LivingEntity drainer) {
        if (this.isEmpty())
            return false;

        if (this.getBlood() < amount)
            return false;

        if (this.getBlood() - amount <= 0)
            this.killHolderFromBloodloss(drainer);

        this.setBlood(this.getBlood() - amount);
        return true;
    }

    protected void killHolderFromBloodloss(LivingEntity drainer) {
        // vampires can't die from blood loss
        if (VampireHelper.isVampire(this.holder) || this.holder.getType().isIn(SLTags.Entities.IMMUNE_TO_BLOOD_LOSS))
            return;

        if (drainer == null)
            this.holder.damage(SLDamageSources.get(this.holder.getWorld(), SLResources.BLOOD_DRAIN_DAMAGE_KEY), Float.MAX_VALUE);
        else
            this.holder.damage(SLDamageSources.bloodDrain(drainer), Float.MAX_VALUE);
    }

    @Override
    public void serverTick() {
        // reset to max blood if the entity grows up
        if (this.wasBaby != this.holder.isBaby()) {
            this.initializeBloodValues();
            this.currentBlood = this.maxBlood;
        }

        // don't tick the blood timer logic if unnecessary
        if (this.getMaxBlood() == 0 || VampireHelper.isVampire(this.holder))
            return;

        if (this.getBlood() < this.getMaxBlood() && this.bloodGainTimer < BloodConstants.BLOOD_GAIN_RATE)
            this.bloodGainTimer++;

        if (this.bloodGainTimer >= BloodConstants.BLOOD_GAIN_RATE) {
            this.currentBlood++;
            this.bloodGainTimer = 0;
            BloodComponent.KEY.sync(this.holder);
        }
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeVarInt(this.getMaxBlood());
        buf.writeVarInt(this.getBlood());
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.maxBlood = buf.readVarInt();
        this.currentBlood = buf.readVarInt();
    }
}
