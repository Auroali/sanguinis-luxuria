package com.auroali.sanguinisluxuria.common.network.packets;

import com.auroali.sanguinisluxuria.SLResources;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

public record EmitParticlesS2C(ParticleEffect parameters, double x, double y, double z, double velocityX,
                               double velocityY,
                               double velocityZ, double varianceX, double varianceY, double varianceZ,
                               int count) implements FabricPacket {
    public static PacketType<EmitParticlesS2C> ID = PacketType.create(SLResources.EMIT_PARTICLES_S2C, EmitParticlesS2C::read);

    public static EmitParticlesS2C read(PacketByteBuf buf) {
        ParticleType<?> type = buf.readRegistryValue(Registries.PARTICLE_TYPE);
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        double velocityX = buf.readDouble();
        double velocityY = buf.readDouble();
        double velocityZ = buf.readDouble();
        double varianceX = buf.readDouble();
        double varianceY = buf.readDouble();
        double varianceZ = buf.readDouble();
        int count = buf.readVarInt();
        ParticleEffect parameters = readType(buf, (ParticleType<? extends ParticleEffect>) type);
        return new EmitParticlesS2C(parameters, x, y, z, velocityX, velocityY, velocityZ, varianceX, varianceY, varianceZ, count);
    }

    private static <T extends ParticleEffect> T readType(PacketByteBuf buf, ParticleType<T> type) {
        return type.getParametersFactory().read(type, buf);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeRegistryValue(Registries.PARTICLE_TYPE, this.parameters.getType());
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeDouble(this.velocityX);
        buf.writeDouble(this.velocityY);
        buf.writeDouble(this.velocityZ);
        buf.writeDouble(this.varianceX);
        buf.writeDouble(this.varianceY);
        buf.writeDouble(this.varianceZ);
        buf.writeVarInt(this.count);
        this.parameters.write(buf);
    }

    @Override
    public PacketType<?> getType() {
        return ID;
    }
}
