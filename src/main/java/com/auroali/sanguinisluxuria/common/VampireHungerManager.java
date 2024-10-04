package com.auroali.sanguinisluxuria.common;

import net.minecraft.entity.player.PlayerEntity;

public interface VampireHungerManager {
    default void sanguinisluxuria$setPlayer(PlayerEntity player) {
    }

    default void sanguinisluxuria$addHunger(int food, float saturationModifier) {
    }
}
