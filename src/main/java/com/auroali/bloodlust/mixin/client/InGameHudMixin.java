package com.auroali.bloodlust.mixin.client;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.VampireHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "renderStatusBars", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;getHeartCount(Lnet/minecraft/entity/LivingEntity;)I",
            shift = At.Shift.AFTER
    ))
    public void bloodlust$injectHungerIcons(MatrixStack matrices, CallbackInfo ci) {
        if(VampireHelper.isVampire(MinecraftClient.getInstance().player))
            RenderSystem.setShaderTexture(0, BLResources.ICONS);
    }

    @Inject(method = "renderStatusBars", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;getMaxAir()I",
            shift = At.Shift.AFTER
    ))
    public void bloodlust$resetIcons(MatrixStack matrices, CallbackInfo ci) {
        if(VampireHelper.isVampire(MinecraftClient.getInstance().player))
            RenderSystem.setShaderTexture(0, InGameHud.GUI_ICONS_TEXTURE);
    }
}
