package com.auroali.sanguinisluxuria.common.conversions.transformers;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionCondition;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionTransformer;
import com.auroali.sanguinisluxuria.common.conversions.conditions.ConversionContextCondition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

/**
 * Transformer that only runs the provided transformer if some condition succeeds
 */
public class ConditionalTransformer implements EntityConversionTransformer {
    public static final Codec<ConditionalTransformer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      EntityConversionTransformer.CODEC.fieldOf("transformer").forGetter(t -> t.transformer),
      EntityConversionCondition.CODEC.fieldOf("condition").forGetter(t -> t.condition)
    ).apply(instance, ConditionalTransformer::new));

    private final EntityConversionTransformer transformer;
    private final EntityConversionCondition condition;

    public ConditionalTransformer(EntityConversionTransformer transformer, EntityConversionCondition condition) {
        this.transformer = transformer;
        this.condition = condition;
    }

    @Override
    public void apply(ConversionContext context, NbtCompound nbtIn, NbtCompound nbtOut) {
        if (this.condition.test(context))
            this.transformer.apply(context, nbtIn, nbtOut);
    }

    @Override
    public Codec<ConditionalTransformer> getCodec() {
        return CODEC;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        return obj instanceof ConditionalTransformer other
          && other.transformer.equals(this.transformer)
          && other.condition.equals(this.condition);
    }

    @Override
    public int hashCode() {
        return 31 * this.condition.hashCode() + this.transformer.hashCode();
    }

    public static ConditionalTransformer biome(EntityConversionTransformer transformer, RegistryKey<Biome> biome) {
        return new ConditionalTransformer(
          transformer,
          ConversionContextCondition.predicate(EntityPredicate.Builder
            .create()
            .location(LocationPredicate.biome(biome))
            .build()
          )
        );
    }
}
