package com.auroali.sanguinisluxuria.common;

import net.minecraft.entity.player.PlayerEntity;

public interface VampireHungerManager {
    void setPlayer(PlayerEntity player);
    void addHunger(int food, float saturationModifier);
}
