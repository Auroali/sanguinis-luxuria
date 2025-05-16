package com.auroali.sanguinisluxuria.common.conversions.conditions;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionCondition;
import com.mojang.serialization.Codec;

import java.util.List;

/**
 * Composite condition that only succeeds if all conditions that make up this one succeed
 */
public class AndConversionCondition extends CompositeConversionCondition {
    public static final Codec<AndConversionCondition> CODEC = codec(AndConversionCondition::new);

    public AndConversionCondition(List<EntityConversionCondition> conditions) {
        super(conditions);
    }

    @Override
    public boolean test(ConversionContext context) {
        for (EntityConversionCondition condition : this.conditions) {
            if (!condition.test(context))
                return false;
        }
        return true;
    }

    @Override
    public Codec<AndConversionCondition> getCodec() {
        return CODEC;
    }
}
