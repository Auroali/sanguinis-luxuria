package com.auroali.bloodlust.common.components.impl;

import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.BloodComponent;
import com.auroali.bloodlust.common.registry.BLTags;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class EntityBloodComponent implements BloodComponent, ServerTickingComponent, AutoSyncedComponent {
    private static final int BLOOD_GAIN_RATE = 200;
    private final LivingEntity holder;
    private int maxBlood;
    private int currentBlood;
    private int bloodGainTimer;

    public EntityBloodComponent(LivingEntity holder) {
        this.holder = holder;
        maxBlood = -1;
        currentBlood = -1;
    }

    public void initializeBloodValues() {
        if(!holder.getType().isIn(BLTags.Entities.HAS_BLOOD)) {
            maxBlood = 0;
            currentBlood = 0;
            BLEntityComponents.BLOOD_COMPONENT.sync(holder);
            return;
        }
        maxBlood = (int) Math.ceil(holder.getMaxHealth());
        if(currentBlood == -1)
            currentBlood = maxBlood;
        currentBlood = Math.min(currentBlood, maxBlood);

        BLEntityComponents.BLOOD_COMPONENT.sync(holder);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        currentBlood = tag.getInt("Blood");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("Blood", currentBlood);
    }

    @Override
    public int getBlood() {
        return currentBlood;
    }

    @Override
    public int getMaxBlood() {
        return maxBlood;
    }

    @Override
    public int addBlood(int amount) {
        // ultrakill??????
        int newBlood = Math.min(maxBlood, amount + currentBlood);
        int bloodAdded = newBlood - currentBlood;
        currentBlood = newBlood;
        return bloodAdded;
    }

    @Override
    public boolean drainBlood() {
        if(!hasBlood())
            return false;

        bloodGainTimer = 0;
        if(currentBlood > 1) {
            currentBlood--;
            BLEntityComponents.BLOOD_COMPONENT.sync(holder);
            return true;
        }

        holder.kill();
        return true;
    }

    @Override
    public boolean hasBlood() {
        return getMaxBlood() > 0;
    }

    @Override
    public void serverTick() {
        // have to do this here instead of the constructor, as health values aren't available there
        if(maxBlood == -1)
            initializeBloodValues();

        if(getMaxBlood() == 0)
            return;

        if(getBlood() < getMaxBlood() && bloodGainTimer < BLOOD_GAIN_RATE)
            bloodGainTimer++;

        if(bloodGainTimer >= BLOOD_GAIN_RATE) {
            currentBlood++;
            bloodGainTimer = 0;
            BLEntityComponents.BLOOD_COMPONENT.sync(holder);
        }
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeInt(getMaxBlood());
        buf.writeInt(getBlood());
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        maxBlood = buf.readInt();
        currentBlood = buf.readInt();
    }
}
