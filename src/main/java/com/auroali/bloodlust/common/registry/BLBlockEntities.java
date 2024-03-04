package com.auroali.bloodlust.common.registry;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.common.blockentities.PedestalBlockEntity;
import com.auroali.bloodlust.common.blockentities.SkillUpgraderBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class BLBlockEntities {
    public static final BlockEntityType<SkillUpgraderBlockEntity> SKILL_UPGRADER = BlockEntityType.Builder.create(
            SkillUpgraderBlockEntity::new, BLBlocks.SKILL_UPGRADER
    ).build(null);
    public static final BlockEntityType<PedestalBlockEntity> PEDESTAL = BlockEntityType.Builder.create(
            PedestalBlockEntity::new, BLBlocks.PEDESTAL
    ).build(null);

    public static void register() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, BLResources.SKILL_UPGRADER_ID, SKILL_UPGRADER);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, BLResources.PEDESTAL_ID, PEDESTAL);
    }
}
