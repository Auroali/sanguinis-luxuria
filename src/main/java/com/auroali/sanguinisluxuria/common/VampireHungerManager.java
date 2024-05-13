package com.auroali.sanguinisluxuria.common;

import net.minecraft.entity.player.PlayerEntity;

public interface VampireHungerManager {
    default void setPlayer(PlayerEntity player) {}
    default void addHunger(int food, float saturationModifier) {}
}
