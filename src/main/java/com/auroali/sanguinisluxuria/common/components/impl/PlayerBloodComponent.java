package com.auroali.sanguinisluxuria.common.components.impl;

import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.util.VampireHelper;
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
    public int getBlood() {
        if (this.holder.getWorld().isClient && !this.holder.isMainPlayer()) {
            return this.blood;
        }
        return this.holder.getHungerManager().getFoodLevel();
    }

    @Override
    public int getMaxBlood() {
        return 20;
    }

    @Override
    public void setBlood(int amount) {
        if (this.getBlood() < amount && VampireHelper.isVampire(this.holder)) {
            VampireComponent vampire = VampireComponent.KEY.get(this.holder);
            if (vampire.isDowned())
                vampire.setDowned(false);
        }
        this.holder.getHungerManager().setFoodLevel(amount);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeVarInt(this.getBlood());
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.blood = buf.readVarInt();
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player != this.holder && VampireHelper.isVampire(player);
    }
}
