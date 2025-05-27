package com.auroali.sanguinisluxuria.client;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.SanguinisLuxuriaClient;
import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.BloodDrainComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;

import java.util.Map;

public class SLHud {
    public static void render(DrawContext context, float deltaTick) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null)
            return;
        VampireComponent vampire = VampireComponent.KEY.get(client.player);
        if (!vampire.isVampire())
            return;
        drawBloodDrainIndicator(context, client, vampire, context.getScaledWindowWidth(), context.getScaledWindowHeight());
        showAbilityCooldowns(context, client, context.getScaledWindowHeight(), vampire.getAbilityContainer());
    }

    private static void drawBloodDrainIndicator(DrawContext context, MinecraftClient client, VampireComponent vampire, int width, int height) {
        if (!SanguinisLuxuriaClient.isLookingAtValidTarget())
            return;

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
          GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO
        );


        Entity targetedEntity = ((EntityHitResult) client.crosshairTarget).getEntity();
        BloodComponent blood = BloodComponent.KEY.get(targetedEntity);
        BloodDrainComponent drainer = BloodDrainComponent.KEY.get(client.player);
        int timeToDrain = drainer.getTimeToDrain();

        double drainPercent = (double) drainer.getTimeDraining() / timeToDrain;
        double bloodPercent = (double) blood.getBlood() / blood.getMaxBlood();

        int fangX = (width - 26) / 2;
        int fangY = (height - 9) / 2;

        int bloodBarX = (width - 14) / 2;
        int bloodBarY = height / 2 + 5;
        if (!VampireHelper.isMasked(client.player) && !vampire.isMist())
            context.drawTexture(SLResources.ICONS, fangX, fangY, 0, 0, 26, 7, 256, 256);
        context.drawTexture(SLResources.ICONS, bloodBarX, bloodBarY, 0, 14, 14, 3, 256, 256);

        RenderSystem.disableBlend();

        context.drawTexture(SLResources.ICONS, bloodBarX + 1, bloodBarY, 15, 14, (int) (bloodPercent * 13), 3, 256, 256);
        int drainPosY = (int) (7 * drainPercent);
        if (!VampireHelper.isMasked(client.player))
            context.drawTexture(SLResources.ICONS, fangX, fangY + (7 - drainPosY), 0, 7 + (7 - drainPosY), 26, drainPosY, 256, 256);
    }

    public static void showAbilityCooldowns(DrawContext context, MinecraftClient client, int height, VampireAbilityContainer container) {
        TextRenderer renderer = client.textRenderer;
        context.getMatrices().push();
        context.getMatrices().translate(2, -2, 0);
        for (Map.Entry<VampireAbility, VampireAbilityContainer.AbilityEntry> entry : container) {
            VampireAbility ability = entry.getKey();
            VampireAbilityContainer.AbilityEntry abilityEntry = entry.getValue();

            if (!abilityEntry.isOnCooldown())
                continue;

            int cooldown = abilityEntry.getCooldown();
            int maxCooldown = abilityEntry.getMaxCooldown();
            float cooldownPercent = MathHelper.clamp(cooldown / (float) maxCooldown, 0.f, 1.f);
            context.drawTexture(SLResources.ICONS, 0, height - 17, 0, 46, 64, 17);
            context.drawText(client.textRenderer, Text.translatable(ability.getTranslationKey()), 4, height - 13, -1, false);
            context.drawTexture(SLResources.ICONS, 3, height - 3, 0, 63, (int) (56 * cooldownPercent), 1);
            context.getMatrices().translate(0, -20, 0);
        }
        context.getMatrices().pop();
    }
}
