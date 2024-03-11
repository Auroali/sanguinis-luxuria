package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;

import java.util.Map;

public class BLCauldronBehaviours {
    public static final Map<Item, CauldronBehavior> BLOOD_CAULDRON_BEHAVIOUR = CauldronBehavior.createMap();
    public static final CauldronBehavior BLOOD_STORING_ITEM_FILL = (state, world, pos, player, hand, stack) -> {
        if(stack.getItem() instanceof BloodStorageItem item) {
            if(BloodStorageItem.getStoredBlood(stack) >= 2) {
                BloodStorageItem.setStoredBlood(stack, BloodStorageItem.getStoredBlood(stack) - 2);
                if(item.getEmptyItem() != null && BloodStorageItem.getStoredBlood(stack) == 0) {
                    player.setStackInHand(hand, item.getEmptyItem().getDefaultStack());
                }
                world.setBlockState(pos, BLBlocks.BLOOD_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 1));
                return ActionResult.success(world.isClient);
            }
        }
        return ActionResult.FAIL;
    };
    public static final CauldronBehavior BLOOD_STORING_ITEM_DRAIN_FILL = (state, world, pos, player, hand, stack) -> {
        if(stack.getItem() instanceof BloodStorageItem item) {
            if(state.get(LeveledCauldronBlock.LEVEL) >= 1 && BloodStorageItem.getStoredBlood(stack) <= BloodStorageItem.getMaxBlood(stack) - 2) {
                BloodStorageItem.setStoredBlood(stack, BloodStorageItem.getStoredBlood(stack) + 2);

                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                return ActionResult.success(world.isClient);
            }

            int level = state.contains(LeveledCauldronBlock.LEVEL) ?  state.get(LeveledCauldronBlock.LEVEL) + 1 : 1;
            if(level > LeveledCauldronBlock.MAX_LEVEL)
                return ActionResult.FAIL;

            BloodStorageItem.setStoredBlood(stack, BloodStorageItem.getStoredBlood(stack) - 2);
            if(item.getEmptyItem() != null && BloodStorageItem.getStoredBlood(stack) == 0) {
                player.setStackInHand(hand, item.getEmptyItem().getDefaultStack());
            }
            world.setBlockState(pos, BLBlocks.BLOOD_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, level));
            return ActionResult.success(world.isClient);
        }
        return ActionResult.FAIL;
    };

    public static final CauldronBehavior FILL_GLASS_BOTTLE = (state, world, pos, player, hand, stack) -> {
        if(stack.isOf(Items.GLASS_BOTTLE)) {
            ItemStack bloodBottle = BloodStorageItem.setStoredBlood(new ItemStack(BLItems.BLOOD_BOTTLE), 2);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
            player.setStackInHand(hand, bloodBottle);
            return ActionResult.success(world.isClient);
        }
        return ActionResult.FAIL;
    };

    public static void register() {
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.put(BLItems.BLOOD_BOTTLE, BLOOD_STORING_ITEM_FILL);
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.put(BLItems.BLOOD_BAG, BLOOD_STORING_ITEM_FILL);
        BLOOD_CAULDRON_BEHAVIOUR.put(BLItems.BLOOD_BOTTLE, BLOOD_STORING_ITEM_DRAIN_FILL);
        BLOOD_CAULDRON_BEHAVIOUR.put(BLItems.BLOOD_BAG, BLOOD_STORING_ITEM_DRAIN_FILL);
        BLOOD_CAULDRON_BEHAVIOUR.put(Items.GLASS_BOTTLE, FILL_GLASS_BOTTLE);
    }
}
