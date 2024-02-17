package com.auroali.bloodlust.common.components.impl;

import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.BloodComponent;
import com.auroali.bloodlust.common.components.VampireComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class PlayerVampireComponent implements VampireComponent {
    private final PlayerEntity holder;
    private boolean isVampire;

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
}
