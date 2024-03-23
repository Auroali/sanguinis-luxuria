package com.auroali.sanguinisluxuria.common.components;

import com.auroali.sanguinisluxuria.common.registry.BLEnchantments;
import com.auroali.sanguinisluxuria.mixin.PersistentProjectileEntityAccessor;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.OptionalInt;
import java.util.UUID;

public class BloodTransferComponent implements Component, AutoSyncedComponent {
    Entity latchedEntity = null;
    UUID latchedEntityId = null;
    PersistentProjectileEntity holder;
    int bloodTransferLevel = 0;

    public BloodTransferComponent(PersistentProjectileEntity entity) {
        this.holder = entity;
    }

    public int getBloodTransferLevel() {
        if(holder.world.isClient)
            return bloodTransferLevel;
        return EnchantmentHelper.getLevel(BLEnchantments.BLOOD_DRAIN, ((PersistentProjectileEntityAccessor)holder).sanguinisluxuria$asItemStack());
    }

    public Entity getLatchedEntity() {
        if(holder.getWorld() instanceof ServerWorld world && latchedEntityId != null && (latchedEntity == null || !latchedEntity.isAlive() || latchedEntity.isRemoved())) {
            latchedEntity = null;
            Entity newEntity = world.getEntity(latchedEntityId);
            if(newEntity == null) {
                latchedEntity = null;
                latchedEntityId = null;
                BLEntityComponents.BLOOD_TRANSFER_COMPONENT.sync(holder);
                return null;
            }
            latchedEntity = newEntity;
            BLEntityComponents.BLOOD_TRANSFER_COMPONENT.sync(holder);
        }
        return latchedEntity;
    }

    public void setLatchedEntity(Entity entity) {
        latchedEntity = entity;
        latchedEntityId = null;
        if(entity != null)
            latchedEntityId = entity.getUuid();
        BLEntityComponents.BLOOD_TRANSFER_COMPONENT.sync(holder);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if(tag.contains("LatchedEntity", NbtElement.INT_ARRAY_TYPE))
            latchedEntityId = tag.getUuid("LatchedEntity");
        getLatchedEntity();
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if(latchedEntityId != null)
            tag.putUuid("LatchedEntity", latchedEntityId);
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeVarInt(getBloodTransferLevel());
        if(latchedEntity != null)
            buf.writeVarInt(latchedEntity.getId());
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        bloodTransferLevel = buf.readVarInt();
        if(buf.isReadable())
            latchedEntity = holder.world.getEntityById(buf.readVarInt());
        else
            latchedEntity = null;
    }
}
