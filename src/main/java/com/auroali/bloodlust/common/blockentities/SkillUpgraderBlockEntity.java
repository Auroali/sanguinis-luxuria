package com.auroali.bloodlust.common.blockentities;

import com.auroali.bloodlust.common.registry.BLBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class SkillUpgraderBlockEntity extends BlockEntity {
    public SkillUpgraderBlockEntity(BlockPos pos, BlockState state) {
        super(BLBlockEntities.SKILL_UPGRADER, pos, state);
    }
}
