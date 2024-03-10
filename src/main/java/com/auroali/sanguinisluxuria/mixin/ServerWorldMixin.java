package com.auroali.sanguinisluxuria.mixin;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalLongRef;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    @Shadow @Final
    List<ServerPlayerEntity> players;

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimension, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @ModifyVariable(method = "tick",
            at = @At("STORE")
    )
    public long sanguinisluxuria$modifySleep(long l) {
        if(this.isDay() && this.players.stream().filter(VampireHelper::isVampire).anyMatch(PlayerEntity::isSleeping)) {
            return properties.getTimeOfDay() + 13000;
        }
        return l;
    }

    @ModifyConstant(method = "tick",
            constant = @Constant(longValue = 24000L, ordinal = 1)
    )
    public long sanguinisluxuria$modifyTime(long constant) {
        if(isDay() && this.players.stream().filter(VampireHelper::isVampire).anyMatch(PlayerEntity::isSleeping))
            return 13000;
        return constant;
    }
}
