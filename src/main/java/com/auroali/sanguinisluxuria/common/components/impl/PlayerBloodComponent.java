package com.auroali.sanguinisluxuria.common.components.impl;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerBloodComponent implements BloodComponent {
    private final PlayerEntity holder;
    private int blood;

    public PlayerBloodComponent(PlayerEntity holder) {
        this.holder = holder;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {}

    @Override
    public void writeToNbt(NbtCompound tag) {}

    @Override
    public int getBlood() {
        if(holder.world.isClient) {
            return blood;
        }
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
        if(VampireHelper.isVampire(holder) && bloodAdded > 0) {
            VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(holder);
            vampire.setDowned(false);
        }
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
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeVarInt(getBlood());
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        blood = buf.readVarInt();
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
