package com.auroali.sanguinisluxuria.client.render.effects;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.VampireHelper;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;

public class VampireHungerEffectManager {
    private static final ManagedShaderEffect SHADER = ShaderEffectManager.getInstance().manage(BLResources.VAMPIRE_HUNGER_SHADER);
    private static final Uniform1f PERCENT = SHADER.findUniform1f("Percent");
    private static final int HUNGER_LIMIT = 6;
    private static final int MAX_TICKS = 10;
    private static final int MAX_TICKS_ENTITY = 20;
    private int ticks;
    private boolean render;

    public void tick(PlayerEntity entity) {
        HungerManager manager = entity.getHungerManager();
        if (manager.getFoodLevel() <= HUNGER_LIMIT) {
            this.render = true;
            int maxTicks = this.shouldFadeIn() ? MAX_TICKS_ENTITY : MAX_TICKS;
            if (this.ticks < maxTicks)
                this.ticks++;
            if (this.ticks > maxTicks)
                this.ticks--;
            return;
        }
        if (this.ticks > 0)
            this.ticks--;
        this.render = false;
    }

    public void render(float tickDelta) {
        if (!this.render && this.ticks == 0)
            return;

        PERCENT.set(MathHelper.clamp(1.f - (this.ticks + tickDelta) / (float) MAX_TICKS, 0.2f, 1.f));
        SHADER.render(tickDelta);
    }

    public boolean shouldFadeIn() {
        HitResult result = MinecraftClient.getInstance().crosshairTarget;
        return result != null && result.getType() == HitResult.Type.ENTITY && VampireHelper.hasBlood(((EntityHitResult) result).getEntity());
    }
}
