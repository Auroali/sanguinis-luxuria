package com.auroali.sanguinisluxuria.util;

import com.auroali.sanguinisluxuria.common.events.BloodStorageFillEvents;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.google.common.base.Predicates;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ItemUtil {
    /**
     * Gets the item in an entity's hand
     *
     * @param entity        the entity to get the item from
     * @param initialHand   the hand to look in first
     * @param itemPredicate the predicate for the item
     * @return the item stack, or {@link net.minecraft.item.ItemStack#EMPTY} if nothing was found
     */
    public static ItemStack getItemInHand(LivingEntity entity, Hand initialHand, Predicate<ItemStack> itemPredicate) {
        // check the stack in the initialHand
        ItemStack stack = initialHand == Hand.MAIN_HAND ? entity.getMainHandStack() : entity.getOffHandStack();
        if (!stack.isEmpty() && itemPredicate.test(stack))
            return stack;

        // the first one didn't match, so check the other hand
        stack = initialHand == Hand.MAIN_HAND ? entity.getOffHandStack() : entity.getMainHandStack();
        if (!stack.isEmpty() && itemPredicate.test(stack))
            return stack;

        // no matches, must not be holding a valid item
        return ItemStack.EMPTY;
    }

    /**
     * Gets the item in an entity's hand
     *
     * @param entity      the entity to get the item from
     * @param initialHand the hand to look in first
     * @return the item stack, or {@link net.minecraft.item.ItemStack#EMPTY} if nothing was found
     * @see VampireHelper#getItemInHand(LivingEntity, Hand, Predicate)
     */
    public static ItemStack getItemInHand(LivingEntity entity, Hand initialHand) {
        return getItemInHand(entity, initialHand, Predicates.alwaysTrue());
    }

    /**
     * Returns the hand a given stack is held in.
     * Assumes that the input stack is actually being held
     *
     * @param entity the entity holding the item
     * @param stack  the stack to check
     * @return the hand the stack is held in
     */
    public static Hand getHandForStack(LivingEntity entity, ItemStack stack) {
        return stack.isEmpty() || stack == entity.getMainHandStack() ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }

    /**
     * Attempts to fill a held blood storage item
     *
     * @param entity the entity holding the item
     * @param amount the amount to try and fill
     * @return the amount of blood successfully filled
     */
    public static int fillHeldBloodStorage(LivingEntity entity, int amount) {
        return fillHeldBloodStorage(entity, amount, null);
    }

    /**
     * Attempts to fill a held blood storage item
     *
     * @param entity   the entity holding the item
     * @param amount   the amount to try and fill
     * @param consumer the consumer to call once the item is filled
     * @return the amount of blood successfully filled
     */
    public static int fillHeldBloodStorage(LivingEntity entity, int amount, Consumer<ItemStack> consumer) {
        ItemStack stack = getItemInHand(entity, Hand.MAIN_HAND, s -> BloodStorageFillEvents.ALLOW_ITEM.invoker().allowItem(entity, s));
        Hand hand = getHandForStack(entity, stack);

        return fillHeldBloodStorage(entity, stack, hand, amount, consumer);
    }

    /**
     * Attempts to fill a held blood storage item
     *
     * @param entity   the entity holding the item
     * @param amount   the amount to try and fill
     * @param consumer the consumer to call once the item is filled
     * @return the amount of blood successfully filled
     */
    public static int fillHeldBloodStorage(LivingEntity entity, ItemStack stack, Hand hand, int amount, Consumer<ItemStack> consumer) {
        ItemStack original = stack.copy();
        ItemStack resultStack = stack.split(1);
        if (!(resultStack.getItem() instanceof BloodStorageItem)) {
            resultStack = BloodStorageFillEvents.TRANSFORM_STACK.invoker().createFrom(entity, resultStack);
        }

        int amountToFill = Math.min(amount, BloodStorageItem.getItemCapacity(resultStack));

        if (amountToFill == 0 || !BloodStorageItem.isItemFillable(resultStack) || !BloodStorageItem.incrementItemBlood(resultStack, amountToFill)) {
            entity.setStackInHand(hand, original);
            return 0;
        }

        if (consumer != null)
            consumer.accept(resultStack);

        if (stack.isEmpty()) {
            entity.setStackInHand(hand, resultStack);
            return amountToFill;
        }

        if (entity instanceof PlayerEntity player) {
            if (!player.getInventory().insertStack(resultStack))
                player.dropItem(resultStack, true);
            return amountToFill;
        }

        entity.dropStack(resultStack);
        return amountToFill;
    }
}
