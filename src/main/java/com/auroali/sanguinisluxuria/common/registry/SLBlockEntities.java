package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.blockentities.AltarBlockEntity;
import com.auroali.sanguinisluxuria.common.blockentities.PedestalBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.List;

public class SLBlockEntities {
    public static final List<Block> SIGNS = List.of(SLBlocks.DECAYED_SIGN, SLBlocks.DECAYED_WALL_SIGN);
    public static final List<Block> HANGING_SIGNS = List.of(SLBlocks.DECAYED_HANGING_SIGN, SLBlocks.DECAYED_WALL_HANGING_SIGN);

    public static final BlockEntityType<AltarBlockEntity> ALTAR = BlockEntityType.Builder.create(
      AltarBlockEntity::new, SLBlocks.ALTAR
    ).build(null);
    public static final BlockEntityType<PedestalBlockEntity> PEDESTAL = BlockEntityType.Builder.create(
      PedestalBlockEntity::new, SLBlocks.PEDESTAL
    ).build(null);

    public static void register() {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, SLResources.ALTAR_ID, ALTAR);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, SLResources.PEDESTAL_ID, PEDESTAL);
    }
}
