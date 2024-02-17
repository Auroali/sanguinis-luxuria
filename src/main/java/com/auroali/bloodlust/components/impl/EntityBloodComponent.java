package com.auroali.bloodlust.components.impl;

import com.auroali.bloodlust.components.BloodComponent;
import com.auroali.bloodlust.registry.BLTags;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

public class EntityBloodComponent implements BloodComponent, ServerTickingComponent {
    private static final int BLOOD_GAIN_RATE = 0;
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
        if(!holder.getType().isIn(BLTags.Entity.HAS_BLOOD)) {
            maxBlood = 0;
            currentBlood = 0;
        }
        maxBlood = (int) Math.ceil(holder.getMaxHealth());
        if(currentBlood == -1)
            currentBlood = maxBlood;
        currentBlood = Math.min(currentBlood, maxBlood);
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
    public boolean drainBlood() {
        if(!hasBlood())
            return false;
        if(currentBlood > 1)
            currentBlood--;

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

        if(bloodGainTimer >= BLOOD_GAIN_RATE)
            currentBlood++;
    }
}
