package com.auroali.sanguinisluxuria.common.network.packets;

import com.auroali.sanguinisluxuria.BLResources;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;

public record FillBloodItemC2S(Hand hand) implements FabricPacket {
    public static final PacketType<FillBloodItemC2S> ID = PacketType.create(BLResources.FILL_BLOOD_ITEM_C2S, FillBloodItemC2S::new);

    public FillBloodItemC2S(PacketByteBuf buf) {
        this(Hand.values()[buf.readByte()]);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeByte(this.hand.ordinal());
    }

    @Override
    public PacketType<?> getType() {
        return ID;
    }
}
