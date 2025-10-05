package com.auroali.sanguinisluxuria.client.render.effects;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.config.SLClientConfig;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;

public class VampireHungerEffectManager {
    private static final ManagedShaderEffect SHADER = ShaderEffectManager.getInstance().manage(SLResources.VAMPIRE_HUNGER_SHADER);
    private static final Uniform1f PERCENT = SHADER.findUniform1f("Percent");
    private static final Uniform1f RENDER_TIME = SHADER.findUniform1f("RenderTime");
    private static final int HUNGER_LIMIT = 8;
    private static final int MAX_TICKS = 20;
    private static final int MAX_TICKS_ENTITY = 40;
    private int ticks;
    private int prevTicks;
    // used for the timing of the wobble effect
    private int totalRenderTicks;
    private boolean render;

    public void tick(PlayerEntity entity) {
        if (!SLClientConfig.INSTANCE.useCustomHungerEffect) {
            this.render = false;
            this.ticks = 0;
            return;
        }
        HungerManager manager = entity.getHungerManager();
        this.totalRenderTicks++;
        this.prevTicks = this.ticks;
        if (manager.getFoodLevel() <= HUNGER_LIMIT) {
            this.render = true;
            int maxTicks = this.shouldFadeIn(entity) ? this.getMaxTicksEntity(manager.getFoodLevel()) : this.getMaxTicks(manager.getFoodLevel());
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

        float renderTick = MathHelper.lerp(tickDelta, (float) this.prevTicks, (float) this.ticks);
        RENDER_TIME.set((this.totalRenderTicks + tickDelta) / 20.f);
        PERCENT.set(MathHelper.clamp(1.f - renderTick / (float) MAX_TICKS_ENTITY, 0.3f, 1.f));
        SHADER.render(tickDelta);
    }

    public int getMaxTicks(int hunger) {
        return MAX_TICKS - 2 * hunger;
    }

    public int getMaxTicksEntity(int hunger) {
        return MAX_TICKS_ENTITY - 2 * hunger;
    }

    public boolean shouldFadeIn(PlayerEntity entity) {
        HitResult result = VampireHelper.raycastEntity(entity, entity.getRotationVector(), VampireHelper::hasBlood, 8.0d);
        return result != null
          && result.getType() == HitResult.Type.ENTITY;
    }
}
