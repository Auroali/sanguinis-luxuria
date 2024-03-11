package com.auroali.sanguinisluxuria.common.blocks;

import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.registry.BLCauldronBehaviours;
import com.auroali.sanguinisluxuria.common.registry.BLItems;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;

import java.util.Map;
import java.util.Properties;

public class BloodCauldronBlock extends LeveledCauldronBlock {
    public BloodCauldronBlock(Settings settings) {
        super(settings, precipitation -> false, BLCauldronBehaviours.BLOOD_CAULDRON_BEHAVIOUR);
    }


}
