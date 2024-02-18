package com.auroali.bloodlust.client;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.BloodlustClient;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.BloodComponent;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class BLHud {
    public static void render(MatrixStack stack, float deltaTick) {
        if(BloodlustClient.targetEntity == null)
            return;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BLResources.ICONS);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO
        );

        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(BloodlustClient.targetEntity);

        double percent = (double) BloodlustClient.suckBloodTimer / BloodlustClient.BLOOD_TIMER_LENGTH;
        double bloodPercent = (double) blood.getBlood() / blood.getMaxBlood();

        int currentBloodX2 = width / 2 - 10 + (int) (bloodPercent * 20);

        int fangX = (width - 26) / 2;
        int fangY = (height - 9) / 2;

        DrawableHelper.drawTexture(stack, fangX, fangY, 0, 0, 26, 9, 256, 256);

        RenderSystem.disableBlend();

        DrawableHelper.drawTexture(stack, fangX, fangY + (int) (9 * (1 - percent)), 0, 9 + (int) (9 * (1 - percent)), 26, (int) (9 * percent), 256, 256);

        DrawableHelper.fill(stack, width / 2 - 10, height / 2 - 10, width / 2 + 10, height / 2 - 6, 0xFFA40000);
        DrawableHelper.fill(stack, width / 2 - 10, height / 2 - 10, currentBloodX2, height / 2 - 6, 0xFFDF0000);
    }
}
