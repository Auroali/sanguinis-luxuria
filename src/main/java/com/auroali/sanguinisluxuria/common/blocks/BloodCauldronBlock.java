package com.auroali.sanguinisluxuria.common.blocks;

import com.auroali.sanguinisluxuria.common.registry.BLCauldronBehaviours;
import net.minecraft.block.LeveledCauldronBlock;

public class BloodCauldronBlock extends LeveledCauldronBlock {
    public BloodCauldronBlock(Settings settings) {
        super(settings, precipitation -> false, BLCauldronBehaviours.BLOOD_CAULDRON_BEHAVIOUR);
    }


}
