package com.auroali.sanguinisluxuria.mixin.client;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.components.impl.PlayerVampireComponent;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @ModifyExpressionValue(method = "renderVignetteOverlay", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F", ordinal = 1))
    public float sanguinisluxuria$showSunTimeProgress(float original, @Local(argsOnly = true) Entity entity) {
        if (VampireHelper.isVampire(entity) && VampireComponent.KEY.get(entity) instanceof PlayerVampireComponent vampire) {
            if (vampire.getTimeInSun() == 0)
                return original;
            return MathHelper.clamp(vampire.getTimeInSun() / (float) vampire.getMaxTimeInSun(), 0.f, 1.f);
        }
        return original;
    }

    @Inject(method = "renderVignetteOverlay", at = @At(
      value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;setShaderColor(FFFF)V", ordinal = 1, shift = At.Shift.AFTER))
    public void sanguinisluxuria$modifyVignetteColourInSun(DrawContext context, Entity entity, CallbackInfo ci) {
        if (VampireHelper.isVampire(entity) && VampireComponent.KEY.get(entity) instanceof PlayerVampireComponent vampire) {
            if (vampire.getTimeInSun() != 0) {
                float multiplier = MathHelper.clamp(vampire.getTimeInSun() / (float) vampire.getMaxTimeInSun(), 0.f, 1.f);
                context.setShaderColor(multiplier * 0.6f, multiplier * 0.95f, multiplier, 0.0f);
            }
        }
    }
}
