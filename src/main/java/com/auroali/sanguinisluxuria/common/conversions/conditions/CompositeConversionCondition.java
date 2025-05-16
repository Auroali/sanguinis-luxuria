package com.auroali.sanguinisluxuria.common.conversions.conditions;

import com.auroali.sanguinisluxuria.common.conversions.EntityConversionCondition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Abstract class that represents a composite condition (one made up of multiple other conditions)
 *
 * @see AndConversionCondition
 * @see OrConversionCondition
 */
public abstract class CompositeConversionCondition implements EntityConversionCondition {
    protected final List<EntityConversionCondition> conditions;

    public CompositeConversionCondition(List<EntityConversionCondition> conditions) {
        this.conditions = conditions;
    }

    public static <T extends CompositeConversionCondition> Codec<T> codec(Function<List<EntityConversionCondition>, T> constructor) {
        return RecordCodecBuilder.create(instance -> instance.ap(
          constructor, EntityConversionCondition.LIST_CODEC.fieldOf("conditions").forGetter(composite -> composite.conditions)
        ));
    }

    public static CompositeConversionCondition or(EntityConversionCondition... conditions) {
        return new OrConversionCondition(Arrays.asList(conditions));
    }

    public static CompositeConversionCondition and(EntityConversionCondition... conditions) {
        return new AndConversionCondition(Arrays.asList(conditions));
    }

    @FunctionalInterface
    public interface Factory<T extends CompositeConversionCondition> {
        T create(List<EntityConversionCondition> conditions);
    }
}
