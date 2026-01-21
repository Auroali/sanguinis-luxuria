package com.auroali.sanguinisluxuria.common.blockentities;

import net.minecraft.entity.Entity;
import net.minecraft.state.property.BooleanProperty;

public interface EntityTargetingBlockEntity<T extends Entity> {
    BooleanProperty TARGET = BooleanProperty.of("has_target");

    void setTarget(T entity);

    void clearTarget();

    T getTarget();

    boolean canTarget(Entity entity);
}
