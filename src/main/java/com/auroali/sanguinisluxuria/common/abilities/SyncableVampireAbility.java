package com.auroali.sanguinisluxuria.common.abilities;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.registry.BLRegistries;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.function.Consumer;

/**
 * Represents a VampireAbility that can send data of type <code>T</code> to the client
 * <br> To use, you must implement writePacket, readPacket, and handle.
 * <br> To send data to the client you must call {@link SyncableVampireAbility#syncAbility(LivingEntity, VampireAbility, Object)} and provide data of the correct type.
 *
 * @param <T> the type of data to be sent to the client. Can be anything, as long as it can be written to and read from a packet
 */
public interface SyncableVampireAbility<T> {
    /**
     * Reads the ability data in from the ability sync packet
     *
     * @param buf   the packet buffer
     * @param world the entity's world
     * @apiNote this only needs to be overridden if extra data needs to be
     * sent to the client and can be ignored if the packet
     * only acts as a ping
     */
    default void writePacket(PacketByteBuf buf, World world, T data) {
    }

    /**
     * Reads the ability data in from the ability sync packet
     *
     * @param buf    the packet buffer
     * @param entity the entity holding the ability
     * @return the ability data
     * @apiNote this only needs to be overridden if extra data needs to be
     * sent to the client, returns null by default and can be ignored if the packet
     * only acts as a ping
     */
    default T readPacket(PacketByteBuf buf, World entity) {
        return null;
    }

    /**
     * Sends an ability sync packet to all nearby players
     *
     * @param entity the entity holding the ability
     * @param data   the data to send to the client
     * @throws IllegalStateException if the provided ability does not implement {@link SyncableVampireAbility}
     */
    default void sync(LivingEntity entity, T data) {
        if (!VampireAbility.class.isAssignableFrom(this.getClass()))
            throw new IllegalStateException("SyncableVampireAbility must be implemented on a VampireAbility!");
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(entity.getId());
        buf.writeRegistryValue(BLRegistries.VAMPIRE_ABILITIES, (VampireAbility) this);
        this.writePacket(buf, entity.getWorld(), data);
        PlayerLookup.tracking(entity).forEach(p ->
          ServerPlayNetworking.send(p, BLResources.ABILITY_SYNC_CHANNEL, buf)
        );
        if (entity instanceof ServerPlayerEntity p)
            ServerPlayNetworking.send(p, BLResources.ABILITY_SYNC_CHANNEL, buf);
    }

    /**
     * Handles the ability sync packet, performing necessary setup by calling {@link SyncableVampireAbility#readPacket(PacketByteBuf, World)}
     * and executing {@link SyncableVampireAbility#handle(LivingEntity, Object)} on the client thread
     *
     * @param entity   the entity holding the ability
     * @param buf      the packet data
     * @param executor the executor to run on the client thread
     * @apiNote {@link SyncableVampireAbility#handle(LivingEntity, Object)} should be overridden instead, as
     * this method handles reading the ability data in and executing {@link SyncableVampireAbility#handle(LivingEntity, Object)}
     * on the client thread by default
     */
    default void handlePacket(LivingEntity entity, PacketByteBuf buf, Consumer<Runnable> executor) {
        T data = this.readPacket(buf, entity.getWorld());
        executor.accept(() -> this.handle(entity, data));
    }

    /**
     * Handles the ability sync packet
     *
     * @param entity the entity holding the ability
     * @param data   the ability data
     */
    void handle(LivingEntity entity, T data);

    /**
     * Syncs the provided ability to all observers of the given entity
     *
     * @param entity  the entity that has the ability
     * @param ability the ability to sync
     * @param data    the data type to sync
     * @param <T>     the data type
     * @throws IllegalStateException if the provided ability does not implement {@link SyncableVampireAbility}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    static <T> void syncAbility(LivingEntity entity, VampireAbility ability, T data) {
        if (ability instanceof SyncableVampireAbility syncable) {
            syncable.sync(entity, data);
        }
    }
}
