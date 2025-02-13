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
    private static final Uniform1f RENDER_TIME = SHADER.findUniform1f("RenderTime");
    private static final int HUNGER_LIMIT = 6;
    private static final int MAX_TICKS = 10;
    private static final int MAX_TICKS_ENTITY = 20;
    private int ticks;
    private int maxTicks;
    private int renderTicks;
    private boolean render;

    public void tick(PlayerEntity entity) {
        HungerManager manager = entity.getHungerManager();
        this.renderTicks++;
        if (manager.getFoodLevel() <= HUNGER_LIMIT) {
            this.render = true;
            this.maxTicks = this.shouldFadeIn() ? this.getMaxTicksEntity(manager.getFoodLevel()) : this.getMaxTicks(manager.getFoodLevel());
            if (this.ticks < this.maxTicks)
                this.ticks++;
            if (this.ticks > this.maxTicks)
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

        float renderTick = this.ticks == this.maxTicks
          ? this.ticks
          : this.ticks > this.maxTicks ? this.ticks - tickDelta : this.ticks + tickDelta;
        RENDER_TIME.set((this.renderTicks + tickDelta) / 20.f);
        PERCENT.set(MathHelper.clamp(1.f - renderTick / (float) MAX_TICKS_ENTITY, 0.3f, 1.f));
        SHADER.render(tickDelta);
    }

    public int getMaxTicks(int hunger) {
        return MAX_TICKS - (int) (1.5f * hunger);
    }

    public int getMaxTicksEntity(int hunger) {
        return MAX_TICKS_ENTITY - 2 * hunger;
    }

    public boolean shouldFadeIn() {
        HitResult result = MinecraftClient.getInstance().crosshairTarget;
        return result != null
          && result.getType() == HitResult.Type.ENTITY
          && VampireHelper.hasBlood(((EntityHitResult) result).getEntity());
    }
}
