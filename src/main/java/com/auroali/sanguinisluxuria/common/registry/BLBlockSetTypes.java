package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.WoodType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;

public class BLBlockSetTypes {
    public static final BlockSetType SILVER = new BlockSetTypeBuilder()
      //"sanguinisluxuria:silver",
      .openableByHand(false)
      .soundGroup(BlockSoundGroup.NETHERITE)
      .doorCloseSound(SoundEvents.BLOCK_IRON_DOOR_CLOSE)
      .doorOpenSound(SoundEvents.BLOCK_IRON_DOOR_OPEN)
      .trapdoorCloseSound(SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE)
      .trapdoorOpenSound(SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN)
      .pressurePlateClickOffSound(SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF)
      .pressurePlateClickOnSound(SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON)
      .buttonClickOffSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF)
      .buttonClickOnSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON)
      .register(BLResources.SILVER_BLOCK_SET);
    public static final BlockSetType DECAYED_WOOD = BlockSetTypeBuilder.copyOf(BlockSetType.CRIMSON).register(BLResources.DECAYED_WOOD_BLOCK_SET);
    public static final WoodType DECAYED_WOOD_TYPE = WoodTypeBuilder.copyOf(WoodType.CRIMSON).register(BLResources.DECAYED_WOOD_BLOCK_SET, DECAYED_WOOD);
}
