package com.auroali.sanguinisluxuria.common.conversions.conditions;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionCondition;
import com.auroali.sanguinisluxuria.util.VampireHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Condition that succeeds if the target entity's current vampire status matches the specified vampire status
 * <br> i.e. if the target entity is a vampire and the isVampire field is true, this condition succeeds
 */
public record VampireConversionCondition(boolean isVampire) implements EntityConversionCondition {
    public static Codec<VampireConversionCondition> CODEC = RecordCodecBuilder.create(instance -> instance
      .ap(VampireConversionCondition::new, Codec.BOOL.fieldOf("isVampire").forGetter(VampireConversionCondition::isVampire))
    );

    @Override
    public boolean test(ConversionContext context) {
        return this.isVampire == VampireHelper.isVampire(context.entity());
    }

    @Override
    public Codec<VampireConversionCondition> getCodec() {
        return CODEC;
    }

    @Override
    public int hashCode() {
        return this.isVampire ? 1231 : 1237;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        return o instanceof VampireConversionCondition other
          && other.isVampire == this.isVampire;
    }

    public static VampireConversionCondition vampire() {
        return new VampireConversionCondition(true);
    }

    public static VampireConversionCondition nonVampire() {
        return new VampireConversionCondition(false);
    }
}
