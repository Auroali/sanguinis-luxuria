package com.auroali.bloodlust.client;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.BloodlustClient;
import com.auroali.bloodlust.common.abilities.VampireAbility;
import com.auroali.bloodlust.common.abilities.VampireAbilityContainer;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.BloodComponent;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.EntityHitResult;

public class BLHud {
    public static void render(MatrixStack stack, float deltaTick) {
        MinecraftClient client = MinecraftClient.getInstance();
        VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(client.player);
        if(!vampire.isVampire())
            return;
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        drawBloodDrainIndicator(stack, client, vampire, width, height);
        drawCooldowns(stack, client, width, height, vampire.getAbilties());

    }

    private static void drawBloodDrainIndicator(MatrixStack stack, MinecraftClient client, VampireComponent vampire, int width, int height) {
        if(!BloodlustClient.isLookingAtValidTarget())
            return;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BLResources.ICONS);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO
        );


        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(((EntityHitResult) client.crosshairTarget).getEntity());

        double percent = (double) vampire.getBloodDrainTimer() / VampireComponent.BLOOD_TIMER_LENGTH;
        double bloodPercent = (double) blood.getBlood() / blood.getMaxBlood();

        int currentBloodX2 = width / 2 - 10 + (int) (bloodPercent * 20);

        int fangX = (width - 26) / 2;
        int fangY = (height - 9) / 2;

        DrawableHelper.drawTexture(stack, fangX, fangY, 0, 0, 26, 9, 256, 256);

        RenderSystem.disableBlend();

        DrawableHelper.drawTexture(stack, fangX, fangY + (int) (9 * (1 - percent)), 0, 9 + (int) (9 * (1 - percent)), 26, (int) (9 * percent), 256, 256);

        DrawableHelper.fill(stack, width / 2 - 10, height / 2 - 10, width / 2 + 10, height / 2 - 6, 0xFF040000);
        DrawableHelper.fill(stack, width / 2 - 10, height / 2 - 10, currentBloodX2, height / 2 - 6, 0xFFDF0000);
    }

    public static void drawCooldowns(MatrixStack matrices, MinecraftClient client, int width, int height, VampireAbilityContainer container) {
        matrices.push();
        for(VampireAbility ability : container) {
            if(!container.isOnCooldown(ability))
                continue;

            double progress = (double) container.getCooldown(ability) / container.getMaxCooldown(ability);
            client.getItemRenderer().renderInGui(ability.getIcon(), 0, height - 16);
            RenderSystem.disableDepthTest();
            DrawableHelper.fill(matrices, 0, height, 16, height - (int) (16 * progress), 0x7AFFFFFF);
            matrices.translate(0, -32, 0);
        }
        matrices.pop();
    }
}
