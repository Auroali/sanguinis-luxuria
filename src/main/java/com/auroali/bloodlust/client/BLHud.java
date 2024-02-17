package com.auroali.bloodlust.client;

import com.auroali.bloodlust.BloodlustClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class BLHud {
    public static void render(MatrixStack stack, float deltaTick) {
        if(BloodlustClient.targetEntity == null)
            return;

        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        double percent = (double) BloodlustClient.suckBloodTimer / BloodlustClient.BLOOD_TIMER_LENGTH;
        int x2 = width / 2 - 10 + (int) (percent * 20);
        DrawableHelper.fill(stack, width / 2 - 10, height / 2 - 2, width / 2 + 10, height / 2 + 2, 0xFFA40000);
        DrawableHelper.fill(stack, width / 2 - 10, height / 2 - 2, x2, height / 2 + 2, 0xFFDF0000);
    }
}
