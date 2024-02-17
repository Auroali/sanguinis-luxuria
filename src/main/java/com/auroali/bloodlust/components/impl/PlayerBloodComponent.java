package com.auroali.bloodlust.components.impl;

import com.auroali.bloodlust.components.BloodComponent;
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
    public boolean drainBlood() {
        int currentBlood = getBlood();
        if(currentBlood > 0) {
            holder.getHungerManager().setFoodLevel(currentBlood - 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasBlood() {
        return true;
    }
}
