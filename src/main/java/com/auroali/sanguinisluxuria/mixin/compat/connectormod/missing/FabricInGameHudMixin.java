package com.auroali.sanguinisluxuria.mixin.compat.connectormod.missing;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.VampireHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(InGameHud.class)
public class FabricInGameHudMixin {
    @WrapOperation(method = "renderStatusBars", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"
    ), slice = @Slice(from = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
      ordinal = 1), to = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 2
    )))
    public void sanguinisluxuria$injectHungerIcons(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (VampireHelper.consumesBlood(MinecraftClient.getInstance().player)) {
            original.call(instance, SLResources.ICONS, x, y, u, v, width, height);
            return;
        }

        original.call(instance, texture, x, y, u, v, width, height);
    }
}
