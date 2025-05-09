package com.auroali.sanguinisluxuria.common.blood.effects;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.blood.BloodDrainEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;

public record BloodDrainTeleportEffect(int radius, float chance) implements BloodDrainEffect {
    public static final Codec<BloodDrainTeleportEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.intRange(0, 128).fieldOf("range").forGetter(BloodDrainTeleportEffect::radius),
      Codec.floatRange(0.f, 1.f).optionalFieldOf("chance", 1.f).forGetter(BloodDrainTeleportEffect::chance)
    ).apply(instance, BloodDrainTeleportEffect::new));

    @Override
    public void apply(LivingEntity entity) {
        if (entity.getRandom().nextFloat() < this.chance)
            VampireHelper.teleportRandomly(entity, this.radius);
    }

    @Override
    public boolean canMerge(BloodDrainEffect other) {
        return other instanceof BloodDrainTeleportEffect;
    }

    @Override
    public BloodDrainEffect merge(BloodDrainEffect other) {
        BloodDrainTeleportEffect otherTeleport = (BloodDrainTeleportEffect) other;
        return new BloodDrainTeleportEffect(
          Math.max(this.radius, otherTeleport.radius),
          Math.max(this.chance, otherTeleport.chance)
        );
    }

    @Override
    public Codec<BloodDrainTeleportEffect> getCodec() {
        return CODEC;
    }
}
