package com.auroali.sanguinisluxuria.common.network.packets;

import com.auroali.sanguinisluxuria.SLResources;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record DrainBloodC2S(boolean draining) implements FabricPacket {
    public static final PacketType<DrainBloodC2S> ID = PacketType.create(SLResources.DRAIN_BLOOD_C2S, DrainBloodC2S::new);

    public DrainBloodC2S(PacketByteBuf buf) {
        this(buf.readBoolean());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(this.draining);
    }

    @Override
    public PacketType<?> getType() {
        return ID;
    }
}
