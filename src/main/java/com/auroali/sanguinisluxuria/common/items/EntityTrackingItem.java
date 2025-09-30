package com.auroali.sanguinisluxuria.common.items;

import com.auroali.sanguinisluxuria.common.blockentities.AltarBlockEntity;
import com.auroali.sanguinisluxuria.common.blocks.AltarBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

import java.util.UUID;

public interface EntityTrackingItem {
    String TRACKED_ENTITY = "TrackedEntity";
    String UUID_KEY = "Uuid";
    String NAME_KEY = "Name";

    default UUID getEntityUuid(ItemStack stack) {
        if (!stack.hasNbt() || !stack.getNbt().contains(TRACKED_ENTITY, NbtElement.COMPOUND_TYPE))
            return null;

        NbtCompound trackedTag = stack.getSubNbt(TRACKED_ENTITY);
        if (!trackedTag.containsUuid(UUID_KEY))
            return null;

        return trackedTag.getUuid(UUID_KEY);
    }

    default Text getEntityName(ItemStack stack) {
        if (!stack.hasNbt() || !stack.getNbt().contains(TRACKED_ENTITY, NbtElement.COMPOUND_TYPE))
            return null;

        NbtCompound trackedTag = stack.getSubNbt(TRACKED_ENTITY);
        if (!trackedTag.contains(NAME_KEY, NbtElement.STRING_TYPE))
            return null;

        return Text.Serializer.fromJson(trackedTag.getString(NAME_KEY));
    }

    default void setTrackedEntity(ItemStack stack, UUID uuid, Text name) {
        NbtCompound trackedTag = getOrCreateEntityTag(stack);
        if (uuid == null && name == null) {
            trackedTag.remove(UUID_KEY);
            trackedTag.remove(NAME_KEY);
            return;
        }
        trackedTag.putUuid(UUID_KEY, uuid);
        trackedTag.putString(NAME_KEY, Text.Serializer.toJson(name));
    }

    static NbtCompound getOrCreateEntityTag(ItemStack stack) {
        return stack.getOrCreateSubNbt(TRACKED_ENTITY);
    }

    static UUID getEntity(ItemStack stack) {
        if (stack.getItem() instanceof EntityTrackingItem item)
            return item.getEntityUuid(stack);
        return null;
    }

    static void setEntity(ItemStack stack, Entity entity) {
        if (stack.getItem() instanceof EntityTrackingItem item) {
            item.setTrackedEntity(stack, entity.getUuid(), entity.getName());
        }
    }

    static void clearEntity(ItemStack stack) {
        if (stack.getItem() instanceof EntityTrackingItem item) {
            item.setTrackedEntity(stack, null, null);
        }
    }

    static boolean canTrackEntity(ItemStack stack) {
        return stack.getItem() instanceof EntityTrackingItem;
    }

    static Entity getEntity(ItemStack stack, World world) {
        if (world instanceof ServerWorld serverWorld && stack.getItem() instanceof EntityTrackingItem item) {
            UUID uuid = item.getEntityUuid(stack);
            return uuid != null ? serverWorld.getEntity(uuid) : null;
        }
        return null;
    }

    static boolean hasEntity(ItemStack stack) {
        return getEntity(stack) != null;
    }

    static ActionResult setAltarTarget(PlayerEntity entity, World world, Hand hand, BlockHitResult hitResult) {
        if (!entity.isSneaking())
            return ActionResult.PASS;


        ItemStack stack = entity.getStackInHand(hand);
        if (!hasEntity(stack))
            return ActionResult.PASS;

        Entity target = getEntity(stack, world);
        if (world.getBlockEntity(hitResult.getBlockPos()) instanceof AltarBlockEntity altar) {
            BlockState state = world.getBlockState(hitResult.getBlockPos());
            if (state.get(AltarBlock.ACTIVE))
                return ActionResult.FAIL;

            if (world.isClient)
                return ActionResult.SUCCESS;

            if (!(target instanceof LivingEntity))
                return ActionResult.FAIL;

            altar.setNextTarget((LivingEntity) target);
            clearEntity(stack);
            return ActionResult.CONSUME;
        }

        return ActionResult.PASS;
    }
}
