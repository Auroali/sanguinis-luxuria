package com.auroali.sanguinisluxuria.mixin.legacy;

import com.auroali.sanguinisluxuria.common.legacy.RegistryOverrides;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> implements MutableRegistry<T> {
    @Shadow
    public abstract boolean contains(RegistryKey<T> key);

    @Shadow
    public abstract boolean containsId(Identifier id);

    @Inject(method = "get(Lnet/minecraft/registry/RegistryKey;)Ljava/lang/Object;", at = @At("HEAD"), cancellable = true)
    public void sanguinisluxuria$overrideKeyGet(@Nullable RegistryKey<T> key, CallbackInfoReturnable<T> cir) {
        if (key != null) {
            RegistryOverrides.getKeyOverride((Registry<?>) this, key)
              .ifPresent(override -> {
                  cir.setReturnValue(this.get((RegistryKey<T>) override));
              });
        }
    }

    @Inject(method = "get(Lnet/minecraft/util/Identifier;)Ljava/lang/Object;", at = @At("HEAD"), cancellable = true)
    public void sanguinisluxuria$overrideIdGet(@Nullable Identifier id, CallbackInfoReturnable<T> cir) {
        if (id != null) {
            RegistryOverrides.getIdOverride((Registry<?>) this, id)
              .ifPresent(override -> {
                  cir.setReturnValue(this.get(override));
              });
        }
    }

    @Inject(method = "getEntry(Lnet/minecraft/registry/RegistryKey;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    public void sanguinisluxuria$overrideGetEntry(RegistryKey<T> key, CallbackInfoReturnable<Optional<RegistryEntry.Reference<T>>> cir) {
        if (key != null) {
            RegistryOverrides.getKeyOverride((Registry<?>) this, key)
              .ifPresent(override -> {
                  cir.setReturnValue(this.getEntry((RegistryKey<T>) override));
              });
        }
    }

    @Inject(method = "contains", at = @At("HEAD"), cancellable = true)
    public void sanguinisluxuria$overrideContains(RegistryKey<T> key, CallbackInfoReturnable<Boolean> cir) {
        RegistryOverrides.getKeyOverride(this, key)
          .ifPresent(override -> cir.setReturnValue(this.contains(key)));
    }

    @Inject(method = "containsId", at = @At("HEAD"), cancellable = true)
    public void sanguinisluxuria$overrideContainsId(Identifier id, CallbackInfoReturnable<Boolean> cir) {
        RegistryOverrides.getIdOverride(this, id)
          .ifPresent(override -> cir.setReturnValue(this.containsId(override)));
    }
}
