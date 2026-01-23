package com.auroali.sanguinisluxuria.mixin.compat.connectormod.present;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.util.VampireHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "net.minecraftforge.client.gui.overlay.ForgeGui", remap = false)
public class ForgeGuiMixin {
    @WrapOperation(method = "renderFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;m_280218_(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", remap = false))
    public void sanguinisluxuria$forgeInjectHungerIcons(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (VampireHelper.consumesBlood(MinecraftClient.getInstance().player)) {
            original.call(instance, SLResources.ICONS, x, y, u, v, width, height);
            return;
        }

        original.call(instance, texture, x, y, u, v, width, height);
    }
}
