package com.auroali.sanguinisluxuria.client.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class AltarParticle extends SpriteBillboardParticle {
    protected AltarParticle(ClientWorld clientWorld, double d, double e, double f, Random random) {
        super(clientWorld, d, e, f);
        this.ascending = false;
        this.gravityStrength = -0.4f;
        this.velocityMultiplier = 0.8f + (float) (0.05 * random.nextGaussian());
        this.collidesWithWorld = false;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_LIT;
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider sprites) {
            this.sprites = sprites;
        }


        @Override
        public @Nullable Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            AltarParticle particle = new AltarParticle(world, x, y, z, world.getRandom());
            particle.setVelocity(velocityX, velocityY, velocityZ);
            particle.setSprite(this.sprites.getSprite(world.getRandom()));
            particle.setMaxAge(16 + world.getRandom().nextInt(16));
            return particle;
        }
    }
}
