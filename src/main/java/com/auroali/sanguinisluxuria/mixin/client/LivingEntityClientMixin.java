package com.auroali.sanguinisluxuria.mixin.client;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.components.SLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityClientMixin extends Entity {
    @Shadow
    public abstract Random getRandom();

    public LivingEntityClientMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void sanguinisluxuria$mistEffects(CallbackInfo ci) {
        if (!this.getWorld().isClient || !VampireHelper.isVampire(this))
            return;

        VampireComponent vampire = VampireComponent.KEY.get(this);
        if (!vampire.isMist())
            return;

        MinecraftClient client = MinecraftClient.getInstance();
        if ((LivingEntity) (Object) this instanceof AbstractClientPlayerEntity entity
          && entity.isMainPlayer()
          && client.options.getPerspective() == Perspective.FIRST_PERSON
        ) {
            return;
        }

        Box bounds = this.getBoundingBox();
        for (int i = 0; i < 2; i++) {
            double x = bounds.minX + bounds.getXLength() * this.getRandom().nextDouble();
            double y = bounds.minY + bounds.getYLength() * this.getRandom().nextDouble();
            double z = bounds.minZ + bounds.getZLength() * this.getRandom().nextDouble();

            this.getWorld().addParticle(
              ParticleTypes.CAMPFIRE_COSY_SMOKE,
              x, y, z,
              0, 0, 0
            );
        }
    }
}
