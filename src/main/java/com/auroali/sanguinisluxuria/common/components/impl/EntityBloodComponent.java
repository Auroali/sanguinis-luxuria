package com.auroali.sanguinisluxuria.common.components.impl;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.BloodConstants;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.InitializableBloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.BLDamageSources;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class EntityBloodComponent implements InitializableBloodComponent, ServerTickingComponent {
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
        bloodGainTimer = tag.getInt("BloodTimer");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("Blood", currentBlood);
        tag.putInt("BloodTimer", bloodGainTimer);
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
        BLEntityComponents.BLOOD_COMPONENT.sync(holder);
        if(VampireHelper.isVampire(holder) && bloodAdded > 0) {
            VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(holder);
            vampire.setDowned(false);
        }
        return bloodAdded;
    }

    @Override
    public void setBlood(int amount) {
        this.currentBlood = amount;
        BLEntityComponents.BLOOD_COMPONENT.sync(holder);
    }

    @Override
    public boolean drainBlood(LivingEntity drainer) {
        if(!hasBlood())
            return false;

        bloodGainTimer = 0;
        if(currentBlood > 1) {
            currentBlood--;
            BLEntityComponents.BLOOD_COMPONENT.sync(holder);
            return true;
        }

        currentBlood = 0;
        BLEntityComponents.BLOOD_COMPONENT.sync(holder);
        if(drainer == null)
            holder.damage(BLDamageSources.get(holder.getWorld(), BLResources.BLOOD_DRAIN_DAMAGE_KEY), Float.MAX_VALUE);
        else
            holder.damage(BLDamageSources.bloodDrain(drainer), Float.MAX_VALUE);
        return true;
    }

    @Override
    public boolean drainBlood() {
        return drainBlood(null);
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

        if(getMaxBlood() == 0 || VampireHelper.isVampire(holder))
            return;

        if(getBlood() < getMaxBlood() && bloodGainTimer < BloodConstants.BLOOD_GAIN_RATE)
            bloodGainTimer++;

        if(bloodGainTimer >= BloodConstants.BLOOD_GAIN_RATE) {
            currentBlood++;
            bloodGainTimer = 0;
            BLEntityComponents.BLOOD_COMPONENT.sync(holder);
        }
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeVarInt(getMaxBlood());
        buf.writeVarInt(getBlood());
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        maxBlood = buf.readVarInt();
        currentBlood = buf.readVarInt();
    }
}
