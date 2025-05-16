package com.auroali.sanguinisluxuria.common.conversions.conditions;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionCondition;
import com.mojang.serialization.Codec;

import java.util.List;

/**
 * Composite condition that only succeeds if at least one of the conditions that make up this one succeed
 */
public class OrConversionCondition extends CompositeConversionCondition {
    public static final Codec<OrConversionCondition> CODEC = codec(OrConversionCondition::new);

    public OrConversionCondition(List<EntityConversionCondition> conditions) {
        super(conditions);
    }

    @Override
    public boolean test(ConversionContext context) {
        for (EntityConversionCondition condition : this.conditions) {
            if (condition.test(context))
                return true;
        }
        return false;
    }

    @Override
    public Codec<OrConversionCondition> getCodec() {
        return CODEC;
    }
}
