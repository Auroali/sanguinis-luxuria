package com.auroali.sanguinisluxuria.common.conversions.conditions;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionCondition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;

import java.util.Objects;

/**
 * Condition that operates off of the Conversion Context.
 * Allows specifying either a conversion type (converting/deconverting),
 * an entity predicate, or both
 */
public record ConversionContextCondition(ConversionContext.Conversion conversion,
                                         EntityPredicate predicate) implements EntityConversionCondition {
    private static final Codec<EntityPredicate> PREDICATE_CODEC = Codecs.JSON_ELEMENT.xmap(
      EntityPredicate::fromJson,
      EntityPredicate::toJson
    );
    public static final Codec<ConversionContextCondition> CODEC = RecordCodecBuilder.<ConversionContextCondition>create(instance -> instance.group(
        ConversionContext.Conversion.CODEC.optionalFieldOf("conversion", ConversionContext.Conversion.NONE).forGetter(ConversionContextCondition::conversion),
        PREDICATE_CODEC.optionalFieldOf("predicate", EntityPredicate.ANY).forGetter(ConversionContextCondition::predicate)
      ).apply(instance, ConversionContextCondition::new))
      .flatXmap(
        condition -> condition.conversion() == ConversionContext.Conversion.NONE && condition.predicate() == EntityPredicate.ANY
          ? DataResult.error(() -> "Expected either a conversion or predicate field")
          : DataResult.success(condition),
        DataResult::success
      );

    @Override
    public boolean test(ConversionContext context) {
        boolean result = true;
        if (this.conversion != ConversionContext.Conversion.NONE)
            result = this.conversion == context.conversion();
        if (this.predicate != EntityPredicate.ANY && context.world() instanceof ServerWorld world)
            result = result && this.predicate.test(world, context.entity().getPos(), context.entity());
        return result;
    }

    @Override
    public Codec<ConversionContextCondition> getCodec() {
        return CODEC;
    }

    public static ConversionContextCondition converting() {
        return new ConversionContextCondition(ConversionContext.Conversion.CONVERTING, EntityPredicate.ANY);
    }

    public static ConversionContextCondition deconverting() {
        return new ConversionContextCondition(ConversionContext.Conversion.DECONVERTING, EntityPredicate.ANY);
    }

    public static ConversionContextCondition predicate(EntityPredicate predicate) {
        return new ConversionContextCondition(ConversionContext.Conversion.NONE, predicate);
    }

    public static ConversionContextConditionBuilder builder() {
        return new ConversionContextConditionBuilder();
    }

    public static class ConversionContextConditionBuilder {
        private ConversionContext.Conversion conversion;
        private EntityPredicate predicate;

        protected ConversionContextConditionBuilder() {
            this.conversion = ConversionContext.Conversion.NONE;
            this.predicate = EntityPredicate.ANY;
        }

        public ConversionContextConditionBuilder conversion(ConversionContext.Conversion conversion) {
            Objects.requireNonNull(conversion);
            this.conversion = conversion;
            return this;
        }

        public ConversionContextConditionBuilder predicate(EntityPredicate predicate) {
            Objects.requireNonNull(predicate);
            this.predicate = predicate;
            return this;
        }

        public ConversionContextCondition build() {
            if (this.conversion == ConversionContext.Conversion.NONE && this.predicate == EntityPredicate.ANY)
                throw new IllegalStateException("ConversionContextCondition cannot be empty");
            return new ConversionContextCondition(this.conversion, this.predicate);
        }
    }
}
