package com.auroali.sanguinisluxuria.mixin.compat.connectormod.present;

import com.auroali.sanguinisluxuria.util.VampireHelper;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalLongRef;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ForgeServerWorldMixin extends World {
    @Shadow
    @Final
    List<ServerPlayerEntity> players;

    protected ForgeServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Shadow
    public abstract void setTimeOfDay(long timeOfDay);

    // make sleeping during day as a vampire set it to night
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getTimeOfDay()J", ordinal = 0))
    public void sanguinisluxuria$modifySleep(BooleanSupplier shouldKeepTicking, CallbackInfo ci, @Share("hasSetTime") LocalBooleanRef hasSetTime, @Share("time") LocalLongRef time) {
        if (this.isDay() && this.players.stream().filter(VampireHelper::isVampire).anyMatch(PlayerEntity::isSleeping)) {
            time.set(this.properties.getTimeOfDay());
            hasSetTime.set(true);
        }
    }

    // make sleeping during day as a vampire set it to night
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V", shift = At.Shift.AFTER))
    public void sanguinisluxuria$setTimeOfDay(BooleanSupplier shouldKeepTicking, CallbackInfo ci, @Share("hasSetTime") LocalBooleanRef hasSetTime, @Share("time") LocalLongRef time) {
        if (hasSetTime.get()) {
            long timeOfDay = time.get() + 24000L;
            this.setTimeOfDay((timeOfDay - timeOfDay % 24000L) - 11000L);
        }
    }
}
