package com.auroali.sanguinisluxuria.common.blood.effects;

import com.auroali.sanguinisluxuria.common.blood.BloodDrainEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProviderTypes;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.trunk.CherryTrunkPlacer;

public record BloodDrainIgniteEffect(IntProvider time, float chance) implements BloodDrainEffect {
    public static final Codec<BloodDrainIgniteEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      IntProvider.createValidatingCodec(0, 4096).fieldOf("time").forGetter(BloodDrainIgniteEffect::time),
      Codec.floatRange(0.f, 1.f).optionalFieldOf("chance", 1.f).forGetter(BloodDrainIgniteEffect::chance)
    ).apply(instance, BloodDrainIgniteEffect::new));

    @Override
    public void apply(LivingEntity entity) {
        if (entity.getRandom().nextFloat() < this.chance) {
            entity.setOnFireFor(this.time.get(entity.getRandom()));
        }
    }

    @Override
    public boolean canMerge(BloodDrainEffect other) {
        return false;
    }

    @Override
    public BloodDrainEffect merge(BloodDrainEffect other) {
        return null;
    }

    @Override
    public Codec<? extends BloodDrainEffect> getCodec() {
        return CODEC;
    }
}
