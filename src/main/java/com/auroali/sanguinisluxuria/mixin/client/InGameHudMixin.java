package com.auroali.sanguinisluxuria.mixin.client;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "renderStatusBars", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
            ordinal = 0
    ), slice = @Slice(from =@At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
            ordinal = 1)
    ))
    public void sanguinisluxuria$injectHungerIcons(MatrixStack matrices, CallbackInfo ci, @Share("hasSet") LocalBooleanRef hasSet) {
        if(VampireHelper.isVampire(MinecraftClient.getInstance().player)) {
            RenderSystem.setShaderTexture(0, BLResources.ICONS);
            hasSet.set(true);
        }
    }

    @Inject(method = "renderStatusBars", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
            ordinal = 1
    ), slice = @Slice(from =@At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
            ordinal = 1)
    ))
    public void sanguinisluxuria$resetIcons(MatrixStack matrices, CallbackInfo ci, @Share("hasSet") LocalBooleanRef hasSet) {
        if(hasSet.get())
            RenderSystem.setShaderTexture(0, InGameHud.GUI_ICONS_TEXTURE);
    }

    @Redirect(method = "renderVignetteOverlay", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;vignetteDarkness:F"
    ))
    public float sanguinisluxuria$showSunTimeProgress(InGameHud hud) {
        PlayerEntity entity = MinecraftClient.getInstance().player;
        if(VampireHelper.isVampire(entity)) {
            VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(entity);
            if(vampire.getTimeInSun() == 0)
                return hud.vignetteDarkness;
            return (float) vampire.getTimeInSun() / vampire.getMaxTimeInSun();
        }
        return hud.vignetteDarkness;
    }
}
