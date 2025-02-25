package com.auroali.sanguinisluxuria.client.render.effects;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.VampireHelper;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;

public class VampireHungerEffectManager {
    private static final ManagedShaderEffect SHADER = ShaderEffectManager.getInstance().manage(BLResources.VAMPIRE_HUNGER_SHADER);
    private static final Uniform1f PERCENT = SHADER.findUniform1f("Percent");
    private static final Uniform1f RENDER_TIME = SHADER.findUniform1f("RenderTime");
    private static final int HUNGER_LIMIT = 8;
    private static final int MAX_TICKS = 20;
    private static final int MAX_TICKS_ENTITY = 40;
    private int ticks;
    private int maxTicks;
    private int renderTicks;
    private boolean render;

    public void tick(PlayerEntity entity) {
        HungerManager manager = entity.getHungerManager();
        this.renderTicks++;
        if (manager.getFoodLevel() <= HUNGER_LIMIT) {
            this.render = true;
            this.maxTicks = this.shouldFadeIn(entity) ? this.getMaxTicksEntity(manager.getFoodLevel()) : this.getMaxTicks(manager.getFoodLevel());
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
        return MAX_TICKS - 3 * hunger;
    }

    public int getMaxTicksEntity(int hunger) {
        return MAX_TICKS_ENTITY - 3 * hunger;
    }

    public boolean shouldFadeIn(PlayerEntity entity) {
        HitResult result = VampireHelper.raycastEntity(entity, entity.getRotationVector(), VampireHelper::hasBlood, 8.0d);
        return result != null
          && result.getType() == HitResult.Type.ENTITY;
    }
}
