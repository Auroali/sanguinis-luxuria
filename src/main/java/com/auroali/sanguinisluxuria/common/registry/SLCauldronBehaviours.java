package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.common.blocks.BloodCauldronBlock;
import com.auroali.sanguinisluxuria.common.blood.BloodConstants;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Map;

public class SLCauldronBehaviours {
    public static final Map<Item, CauldronBehavior> BLOOD_CAULDRON_BEHAVIOUR = CauldronBehavior.createMap();
    public static final CauldronBehavior BLOOD_STORING_ITEM_FILL = (state, world, pos, player, hand, stack) -> {
        if (fillCauldronFrom(state, world, pos, player, hand, stack))
            return ActionResult.success(world.isClient);
        return ActionResult.FAIL;
    };
    public static final CauldronBehavior BLOOD_STORING_ITEM_DRAIN_FILL = (state, world, pos, player, hand, stack) -> {
        if (fillFromCauldron(state, world, pos, player, hand, stack))
            return ActionResult.success(world.isClient);
        if (fillCauldronFrom(state, world, pos, player, hand, stack))
            return ActionResult.success(world.isClient);
        return ActionResult.FAIL;
    };

    public static final CauldronBehavior FILL_GLASS_BOTTLE = (state, world, pos, player, hand, stack) -> {
        if (stack.isOf(Items.GLASS_BOTTLE)) {
            ItemStack bloodBottle = BloodStorageItem.createStack(SLItems.BLOOD_BOTTLE, BloodConstants.BLOOD_PER_BOTTLE);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
            player.incrementStat(Stats.FILL_CAULDRON);
            player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, bloodBottle));
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1, 1);
            return ActionResult.success(world.isClient);
        }
        return ActionResult.FAIL;
    };

    public static void register() {
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.put(SLItems.BLOOD_BOTTLE, BLOOD_STORING_ITEM_FILL);
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.put(SLItems.BLOOD_BAG, BLOOD_STORING_ITEM_FILL);
        BLOOD_CAULDRON_BEHAVIOUR.put(SLItems.BLOOD_BOTTLE, BLOOD_STORING_ITEM_DRAIN_FILL);
        BLOOD_CAULDRON_BEHAVIOUR.put(SLItems.BLOOD_BAG, BLOOD_STORING_ITEM_DRAIN_FILL);
        BLOOD_CAULDRON_BEHAVIOUR.put(Items.GLASS_BOTTLE, FILL_GLASS_BOTTLE);
    }

    public static boolean fillFromCauldron(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
        if (BloodStorageItem.isItemFillable(stack) && BloodStorageItem.getItemCapacity(stack) >= BloodConstants.BLOOD_PER_BOTTLE) {
            ItemStack filled = stack.split(1);
            BloodStorageItem.incrementItemBlood(filled, BloodConstants.BLOOD_PER_BOTTLE);
            BloodCauldronBlock.decrementFluidLevel(state, world, pos);
            if (stack.isEmpty()) {
                player.setStackInHand(hand, filled);
            } else if (!player.getInventory().insertStack(filled)) {
                player.dropItem(filled, false);
            }
            player.incrementStat(Stats.USE_CAULDRON);
            player.incrementStat(Stats.USED.getOrCreateStat(filled.getItem()));
            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            return true;
        }
        return false;
    }

    public static boolean fillCauldronFrom(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
        if (
          BloodStorageItem.isItemDrainable(stack)
            && BloodStorageItem.getItemBlood(stack) >= BloodConstants.BLOOD_PER_BOTTLE
            && (state.isOf(Blocks.CAULDRON) || state.get(LeveledCauldronBlock.LEVEL) < 3)
        ) {
            ItemStack drained = stack.split(1);
            BloodStorageItem.decrementItemBlood(stack, BloodConstants.BLOOD_PER_BOTTLE);
            world.setBlockState(pos, state.isOf(Blocks.CAULDRON)
              ? SLBlocks.BLOOD_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 1)
              : state.with(LeveledCauldronBlock.LEVEL, state.get(LeveledCauldronBlock.LEVEL) + 1)
            );
            if (BloodStorageItem.isItemEmpty(drained)) {
                drained = BloodStorageItem.createEmptyStackFor(drained);
            }
            if (stack.isEmpty()) {
                player.setStackInHand(hand, drained);
            } else if (!player.getInventory().insertStack(drained)) {
                player.dropItem(drained, false);
            }
            player.incrementStat(Stats.FILL_CAULDRON);
            player.incrementStat(Stats.USED.getOrCreateStat(drained.getItem()));
            world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            return true;
        }
        return false;
    }
}
