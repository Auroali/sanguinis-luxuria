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
import java.util.Optional;

/**
 * Condition that operates off of the Conversion Context.
 * Allows specifying either a conversion type (converting/deconverting),
 * an entity predicate, or both
 */
public record ConversionContextCondition(Optional<ConversionContext.Conversion> conversion,
                                         Optional<EntityPredicate> predicate) implements EntityConversionCondition {
    private static final Codec<EntityPredicate> PREDICATE_CODEC = Codecs.JSON_ELEMENT.xmap(
      EntityPredicate::fromJson,
      EntityPredicate::toJson
    );
    public static final Codec<ConversionContextCondition> CODEC = RecordCodecBuilder.<ConversionContextCondition>create(instance -> instance.group(
        ConversionContext.Conversion.CODEC.optionalFieldOf("conversion").forGetter(ConversionContextCondition::conversion),
        PREDICATE_CODEC.optionalFieldOf("predicate").forGetter(ConversionContextCondition::predicate)
      ).apply(instance, ConversionContextCondition::new))
      .flatXmap(
        condition -> condition.conversion().isEmpty() && condition.predicate().isEmpty()
          ? DataResult.error(() -> "Expected either a conversion or predicate field")
          : DataResult.success(condition),
        DataResult::success
      );

    @Override
    public boolean test(ConversionContext context) {
        boolean result = true;
        if (this.conversion.isPresent())
            result = this.conversion.get() == context.conversion();
        if (this.predicate.isPresent() && context.world() instanceof ServerWorld world)
            result = result && this.predicate.get().test(world, context.entity().getPos(), context.entity());
        return result;
    }

    @Override
    public Codec<ConversionContextCondition> getCodec() {
        return CODEC;
    }

    public static ConversionContextCondition converting() {
        return new ConversionContextCondition(Optional.of(ConversionContext.Conversion.CONVERTING), Optional.empty());
    }

    public static ConversionContextCondition deconverting() {
        return new ConversionContextCondition(Optional.of(ConversionContext.Conversion.DECONVERTING), Optional.empty());
    }

    public static ConversionContextCondition predicate(EntityPredicate predicate) {
        return new ConversionContextCondition(Optional.empty(), Optional.of(predicate));
    }

    public static ConversionContextConditionBuilder builder() {
        return new ConversionContextConditionBuilder();
    }

    public static class ConversionContextConditionBuilder {
        private ConversionContext.Conversion conversion;
        private EntityPredicate predicate;

        protected ConversionContextConditionBuilder() {
            this.conversion = null;
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
            if (this.conversion == null && this.predicate == EntityPredicate.ANY)
                throw new IllegalStateException("ConversionContextCondition cannot be empty");
            return new ConversionContextCondition(
              this.conversion == null ? Optional.empty() : Optional.of(this.conversion),
              this.predicate == EntityPredicate.ANY ? Optional.empty() : Optional.of(this.predicate)
            );
        }
    }
}
