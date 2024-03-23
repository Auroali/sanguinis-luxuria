package com.auroali.sanguinisluxuria.common.abilities;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.registry.BLRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Consumer;

public interface SyncableVampireAbility<T> {
    default void writePacket(PacketByteBuf buf, LivingEntity entity, T data) {}
    default T readPacket(PacketByteBuf buf, LivingEntity entity) { return null; }
    default void sync(LivingEntity entity, T data) {
        if(!VampireAbility.class.isAssignableFrom(this.getClass()))
            throw new IllegalStateException("SyncableVampireAbility must be implemented on a VampireAbility!");
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(entity.getId());
        buf.writeRegistryValue(BLRegistry.VAMPIRE_ABILITIES, (VampireAbility)this);
        writePacket(buf, entity, data);
        PlayerLookup.tracking(entity).forEach(p ->
            ServerPlayNetworking.send(p, BLResources.ABILITY_SYNC_CHANNEL, buf)
        );
        if(entity instanceof ServerPlayerEntity p)
            ServerPlayNetworking.send(p, BLResources.ABILITY_SYNC_CHANNEL, buf);
    }
    default void handlePacket(LivingEntity entity, PacketByteBuf buf, Consumer<Runnable> executor) {
        T data = readPacket(buf, entity);
        executor.accept(() -> handle(entity, data));
    }
    void handle(LivingEntity entity, T data);
}
