package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.SLBlocks;
import net.minecraft.data.family.BlockFamilies;
import net.minecraft.data.family.BlockFamily;

public class SLBlockFamilies {
    public static final BlockFamily DECAYED_WOOD_FAMILY = BlockFamilies.register(SLBlocks.DECAYED_PLANKS)
      .sign(SLBlocks.DECAYED_SIGN, SLBlocks.DECAYED_WALL_SIGN)
      .pressurePlate(SLBlocks.DECAYED_PRESSURE_PLATE)
      .fence(SLBlocks.DECAYED_FENCE)
      .fenceGate(SLBlocks.DECAYED_FENCE_GATE)
      .slab(SLBlocks.DECAYED_SLAB)
      .stairs(SLBlocks.DECAYED_STAIRS)
      .button(SLBlocks.DECAYED_BUTTON)
      .door(SLBlocks.DECAYED_DOOR)
      .trapdoor(SLBlocks.DECAYED_TRAPDOOR)
      .build();
}
