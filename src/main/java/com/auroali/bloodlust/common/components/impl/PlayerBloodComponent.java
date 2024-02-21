package com.auroali.bloodlust.common.components.impl;

import com.auroali.bloodlust.common.components.BloodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class PlayerBloodComponent implements BloodComponent {
    private final PlayerEntity holder;

    public PlayerBloodComponent(PlayerEntity holder) {
        this.holder = holder;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {}

    @Override
    public void writeToNbt(NbtCompound tag) {}

    @Override
    public int getBlood() {
        return holder.getHungerManager().getFoodLevel();
    }

    @Override
    public int getMaxBlood() {
        return 20;
    }

    @Override
    public int addBlood(int amount) {
        // ultrakill??????
        int newBlood = Math.min(getMaxBlood(), amount + getBlood());
        int bloodAdded = newBlood - getBlood();
        holder.getHungerManager().setFoodLevel(newBlood);
        return bloodAdded;
    }

    @Override
    public void setBlood(int amount) {
        holder.getHungerManager().setFoodLevel(amount);
    }

    @Override
    public boolean drainBlood(LivingEntity entity) {
        int currentBlood = getBlood();
        if(currentBlood > 0) {
            holder.getHungerManager().setFoodLevel(currentBlood - 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean drainBlood() {
        return drainBlood(null);
    }

    @Override
    public boolean hasBlood() {
        return true;
    }
}
