package com.auroali.bloodlust.client.screen;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.common.abilities.VampireAbility;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.auroali.bloodlust.common.registry.BLRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VampireAbilitiesScreen extends Screen {
    List<VampireAbilityWidget> abilities;
    double scrollX = 0;
    double scrollY = -32;
    public VampireAbilitiesScreen() {
        super(NarratorManager.EMPTY);
        abilities = new ArrayList<>();
        for (VampireAbility ability : BLRegistry.VAMPIRE_ABILITIES) {
            abilities.add(new VampireAbilityWidget(ability, null));
        }

        abilities.forEach(a -> a.resolveParent(abilities));
        VampireAbilitiesPositioner.position(abilities);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        VampireComponent component = BLEntityComponents.VAMPIRE_COMPONENT.get(client.player);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BLResources.ABILITIES_BG);
        int panelX = (width - 252) / 2;
        int panelY = (height - 147) / 2;

        drawTexture(matrices, panelX + 9, panelY + 18, 0, 0, 234, 113);

        int centerX = width / 2;
        int centerY = height / 2;

        enableScissor(panelX + 9, panelY + 18, panelX + 242, panelY + 131);
        abilities.forEach(a -> a.render(matrices, component.getAbilties(), (int) (centerX + scrollX), (int) (centerY + scrollY), mouseX, mouseY));
        disableScissor();

        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderTexture(0, BLResources.ABILITIES_SCREEN);
        drawTexture(matrices, panelX, panelY, 0, 0, 252, 148);
        RenderSystem.disableBlend();

        textRenderer.draw(matrices, Text.translatable("gui.bloodlust.abilities"), panelX + 8, panelY + 6, 4210752);
        textRenderer.draw(matrices, Text.translatable("gui.bloodlust.skill_points", component.getSkillPoints()), panelX + 8, panelY + 134, 4210752);

        for(VampireAbilityWidget ability : abilities) {
            if(ability.isMouseOver(mouseX, mouseY, (int) (centerX + scrollX), (int) (centerY + scrollY))) {
                renderTooltip(matrices, Text.translatable(ability.ability.getTranslationKey()), mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(VampireAbilityWidget ability : abilities) {
            if(button == 0 && ability.isMouseOver((int) mouseX, (int) mouseY, (int) (width / 2 + scrollX), (int) (height / 2 + scrollY)) && ability.onClick())
                return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        boolean dragged = super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        if(!dragged) {
            scrollX += deltaX;
            scrollY += deltaY;
            return false;
        }
        return true;
    }
}
