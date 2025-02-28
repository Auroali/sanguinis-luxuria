package com.auroali.sanguinisluxuria.common.conversions.conditions;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionCondition;
import com.auroali.sanguinisluxuria.common.registry.BLConversions;

import java.util.List;

/**
 * Composite condition that only succeeds if all conditions that make up this one succeed
 */
public class AndConversionCondition extends CompositeConversionCondition {
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
    public Serializer<?> getSerializer() {
        return BLConversions.AND_CONDITION;
    }
}
