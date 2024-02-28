package com.auroali.bloodlust.client.screen;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.VampireHelper;
import com.auroali.bloodlust.common.abilities.VampireAbility;
import com.auroali.bloodlust.common.abilities.VampireAbilityContainer;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.auroali.bloodlust.common.registry.BLRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class VampireAbilityWidget extends DrawableHelper {
    public static final int WIDTH = 19;
    public static final int HEIGHT = 19;

    VampireAbility ability;
    VampireAbilityWidget parent;
    ItemStack icon;
    List<VampireAbilityWidget> children;
    private int x;
    private int y;

    public VampireAbilityWidget(VampireAbility ability, VampireAbilityWidget parent) {
        this.ability = ability;
        this.parent = parent;
        this.icon = ability.getIcon();
        this.children = new ArrayList<>();
    }

    public void resolveParent(List<VampireAbilityWidget> abilities) {
        for(VampireAbilityWidget widget : abilities) {
            if(widget.ability == ability.getParent()) {
                this.parent = widget;
                this.parent.getChildren().add(this);
                return;
            }
        }
    }

    public void render(MatrixStack matrices, VampireAbilityContainer container, int offsetX, int offsetY, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BLResources.ICONS);

        drawLines(matrices, container, offsetX, offsetY);
        drawTexture(
                matrices,
                offsetX + x,
                offsetY + y,
                0,
                container.hasAbility(ability) ? 46 + HEIGHT : 46,
                WIDTH,
                HEIGHT,
                256,
                256
        );
        MinecraftClient.getInstance().getItemRenderer()
                .renderInGui(
                        icon,
                        getX() + offsetX + 1,
                        getY() + offsetY + 1
                );
    }

    public void drawLines(MatrixStack stack, VampireAbilityContainer container, int offsetX, int offsetY) {
        if(parent == null)
            return;

        int colour = -1;
        int blackColour = 0xFF000000;
        if(VampireHelper.hasIncompatibleAbility(MinecraftClient.getInstance().player, ability) || !container.hasAbility(parent.ability)) {
            colour = 0xFF6C0000;
            blackColour = 0xFF0C0000;
        }
        else if(container.hasAbility(ability)) {
            colour = 0xFFFF6E11;
            blackColour = 0xFF260005;
        }

        int lineOffset = WIDTH / 2;
        int yAmount = (getY() - (parent.getY() + HEIGHT)) / 2;
        drawVerticalLine(stack, getX() + lineOffset + offsetX, getY() + offsetY, getY() + offsetY - yAmount - 1, colour);

        drawVerticalLine(stack, getX() + 1 + lineOffset + offsetX, getY() + offsetY, getY() + offsetY - yAmount - 1, blackColour);
        drawVerticalLine(stack, getX() - 1 + lineOffset + offsetX, getY() + offsetY, getY() + offsetY - yAmount - 1, blackColour);


        drawVerticalLine(stack, parent.getX() + lineOffset + offsetX, parent.getY() + HEIGHT - 1 + offsetY, parent.getY() + HEIGHT + 1 + offsetY + yAmount, colour);

        drawVerticalLine(stack, parent.getX() + 1 + lineOffset + offsetX, parent.getY() + HEIGHT - 1 + offsetY, parent.getY() + HEIGHT + 1 + offsetY + yAmount, blackColour);
        drawVerticalLine(stack, parent.getX() - 1 + lineOffset + offsetX, parent.getY() + HEIGHT - 1 + offsetY, parent.getY() + HEIGHT + 1 + offsetY + yAmount, blackColour);

        if(parent.getX() == getX())
            return;

        int horizLineOffset = parent.getX() < getX() ? 1 : 0;
        drawHorizontalLine(stack, getX() + offsetX + lineOffset, parent.getX() + offsetX + lineOffset, getY() + offsetY - yAmount, colour);
        //drawHorizontalLine(stack, getX() + offsetX + lineOffset + horizLineOffset, parent.getX() + offsetX + lineOffset, getY() + offsetY - yAmount + 1, colour);

        drawHorizontalLine(stack, getX() + 2 * (getX() > parent.getX() ? -1 : 1) + offsetX + lineOffset, parent.getX() + offsetX + lineOffset, getY() + offsetY - yAmount + 1, blackColour);
        drawHorizontalLine(stack, getX() + offsetX + lineOffset - (getX() > parent.getX() ? -1 : 1), parent.getX() + offsetX + lineOffset - 2 * (getX() > parent.getX() ? -1 : 1), getY() + offsetY - yAmount - 1, blackColour);
    }

    public boolean isMouseOver(int mouseX, int mouseY, int offsetX, int offsetY) {
        int x = getX() + offsetX;
        int y = getY() + offsetY;

        return mouseX >= x && mouseX <= x + WIDTH && mouseY >= y && mouseY <= y + HEIGHT;

    }
    
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }

    public List<VampireAbilityWidget> getChildren() {
        return children;
    }

    public boolean isOverlappingX(VampireAbilityWidget other) {
        return getX() >= other.getX() && getX() <= other.getX() + WIDTH;
    }

    public boolean isOverlappingY(VampireAbilityWidget other) {
        return getY() >= other.getY() && getY() <= other.getY() + HEIGHT;
    }

    public boolean onClick(VampireAbilitiesScreen screen, int button) {
        ClientPlayerEntity entity = MinecraftClient.getInstance().player;
        VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(entity);
        if (button == 0 && tryUnlock(vampire, entity))
            return true;
        if(button == 1 && bindTo(vampire, entity, screen))
            return true;
        return false;
    }

    private boolean bindTo(VampireComponent vampire, ClientPlayerEntity entity, VampireAbilitiesScreen screen) {
        if(!vampire.getAbilties().hasAbility(ability) || !ability.isKeybindable())
            return false;

        screen.bindingWidget = this;
        return true;
    }

    private boolean tryUnlock(VampireComponent vampire, ClientPlayerEntity entity) {
        if (vampire.getSkillPoints() < ability.getRequiredSkillPoints() || vampire.getAbilties().hasAbility(ability) || !vampire.getAbilties().hasAbility(ability.getParent())) {
            entity.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASS, SoundCategory.PLAYERS, 1.0f, 1.0f);
            return false;
        }

        for(VampireAbility other : vampire.getAbilties()) {
            if(ability.incompatibleWith(other)) {
                entity.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASS, SoundCategory.PLAYERS, 1.0f, 1.0f);
                return false;
            }
        }
        entity.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeRegistryValue(BLRegistry.VAMPIRE_ABILITIES, ability);
        buf.writeBoolean(false);
        ClientPlayNetworking.send(BLResources.SKILL_TREE_CHANNEL, buf);
        return true;
    }
}
