package com.auroali.sanguinisluxuria.mixin;

import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(
      method = "playerTick",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V", ordinal = 0),
      slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/HealthUpdateS2CPacket;<init>(FIF)V"))
    )
    public void sanguinisluxuria$syncBlood(CallbackInfo ci) {
        BLEntityComponents.BLOOD_COMPONENT.sync(this);
    }
}
