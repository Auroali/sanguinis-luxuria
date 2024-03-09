package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.blockentities.PedestalBlockEntity;
import com.auroali.sanguinisluxuria.common.blockentities.SkillUpgraderBlockEntity;
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
