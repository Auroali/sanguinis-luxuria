package com.auroali.sanguinisluxuria.common.items;

import com.auroali.sanguinisluxuria.SLResources;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public interface BloodStorageItem {
    String BLOOD_KEY = "Blood";
    String CURRENT_BLOOD_KEY = "CurrentBlood";
    String MAX_BLOOD_KEY = "MaxBlood";

    default void setBlood(ItemStack stack, int blood) {
        getOrCreateBloodTag(stack)
          .putInt(CURRENT_BLOOD_KEY, Math.min(blood, this.getMaxBlood(stack)));
    }

    default int getBlood(ItemStack stack) {
        if (!stack.hasNbt() || !stack.getNbt().contains(BLOOD_KEY, NbtElement.COMPOUND_TYPE))
            return 0;

        return stack.getSubNbt(BLOOD_KEY).getInt(CURRENT_BLOOD_KEY);
    }

    default int getMaxBlood(ItemStack stack) {
        if (!stack.hasNbt() || !stack.getNbt().contains(BLOOD_KEY, NbtElement.COMPOUND_TYPE))
            return stack.getItem() instanceof BloodStorageItem item ? item.getDefaultMaxBlood() : 0;

        return stack.getSubNbt(BLOOD_KEY).getInt(MAX_BLOOD_KEY);
    }

    default ItemStack createEmptyItem(ItemStack stack) {
        return stack;
    }

    default boolean canFill() {
        return true;
    }

    default boolean canDrain() {
        return true;
    }

    int getDefaultMaxBlood();

    static NbtCompound getOrCreateBloodTag(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(BLOOD_KEY, NbtElement.COMPOUND_TYPE))
            return stack.getSubNbt(BLOOD_KEY);

        NbtCompound bloodTag = stack.getOrCreateSubNbt(BLOOD_KEY);
        bloodTag.putInt(CURRENT_BLOOD_KEY, 0);
        bloodTag.putInt(MAX_BLOOD_KEY, stack.getItem() instanceof BloodStorageItem item ? item.getDefaultMaxBlood() : 0);
        if (stack.hasNbt() && stack.getNbt().contains("StoredBlood", NbtElement.INT_TYPE) && stack.getNbt().getInt("StoredBlood") > 0) {
            bloodTag.putInt(CURRENT_BLOOD_KEY, bloodTag.getInt("StoredBlood"));
        }
        return bloodTag;
    }

    /**
     * Gets the amount of blood in a given item
     *
     * @param stack the stack to check
     * @return the amount of blood the item has
     */
    static int getItemBlood(ItemStack stack) {
        if (stack.getItem() instanceof BloodStorageItem item)
            return item.getBlood(stack);
        return 0;
    }

    /**
     * Gets the maximum amount of blood in a given item
     *
     * @param stack the stack to check
     * @return the maximum amount of blood the item can hold
     */
    static int getItemMaxBlood(ItemStack stack) {
        if (stack.getItem() instanceof BloodStorageItem item)
            return item.getMaxBlood(stack);
        return 0;
    }

    /**
     * Sets the amount of blood in a given item
     *
     * @param stack the stack to modify
     * @param blood the new amount of blood
     * @return the stack, for chaining/inlining
     */
    static ItemStack setItemBlood(ItemStack stack, int blood) {
        if (stack.getItem() instanceof BloodStorageItem item)
            item.setBlood(stack, blood);
        return stack;
    }

    /**
     * Decrements the amount of blood stored in a given item by the specified amount
     *
     * @param stack  the stack to remove blood from
     * @param amount the amount to remove
     * @return if the amount was successfully decremented
     */
    static boolean decrementItemBlood(ItemStack stack, int amount) {
        if (amount == 0)
            return false;

        if (!stack.hasNbt() || !stack.getNbt().contains(BLOOD_KEY, NbtElement.COMPOUND_TYPE))
            getOrCreateBloodTag(stack);

        int blood = getItemBlood(stack);
        if (blood < amount)
            return false;

        setItemBlood(stack, blood - amount);
        return true;
    }

    /**
     * Increments the amount of blood stored in a given item by the specified amount
     *
     * @param stack  the stack to add blood to
     * @param amount the amount to add
     * @return if the amount was successfully incremented
     */
    static boolean incrementItemBlood(ItemStack stack, int amount) {
        if (amount == 0)
            return false;

        if (!stack.hasNbt() || !stack.getNbt().contains(BLOOD_KEY, NbtElement.COMPOUND_TYPE))
            getOrCreateBloodTag(stack);

        int blood = getItemBlood(stack);
        int maxBlood = getItemMaxBlood(stack);
        if (blood + amount > maxBlood) {
            return false;
        }

        setItemBlood(stack, blood + amount);
        return true;
    }

    /**
     * Creates an item stack, with the specified amount of blood for the given item
     *
     * @param item  the item to use for the stack
     * @param blood the amount of blood to set the item to store
     * @return the new stack
     */
    static ItemStack createStack(Item item, int blood) {
        ItemStack stack = new ItemStack(item);
        return setItemBlood(stack, blood);
    }

    /**
     * Creates an item stack, with the maximum amount of blood for the given item
     *
     * @param item the item to use for the stack
     * @return the new stack
     */
    static ItemStack createStack(Item item) {
        ItemStack stack = new ItemStack(item);
        if (item instanceof BloodStorageItem bloodStorage)
            return setItemBlood(new ItemStack(item), bloodStorage.getDefaultMaxBlood());
        return stack;
    }

    /**
     * Creates the empty stack for a given blood storing item
     *
     * @param stack the blood storing item stack
     * @return the empty stack, or the original if there is no specific empty item stack
     */
    static ItemStack createEmptyStackFor(ItemStack stack) {
        return stack.getItem() instanceof BloodStorageItem item ? item.createEmptyItem(stack) : stack;
    }

    /**
     * Checks if an item is fillable
     *
     * @param stack the blood storing item stack
     * @return if the item can be filled
     */
    static boolean isItemFillable(ItemStack stack) {
        if (stack.getItem() instanceof BloodStorageItem item)
            return item.canFill();
        return false;
    }

    /**
     * Checks if an item is drainable
     *
     * @param stack the blood storing item stack
     * @return if the item can be drained
     */
    static boolean isItemDrainable(ItemStack stack) {
        if (stack.getItem() instanceof BloodStorageItem item)
            return item.canDrain();
        return false;
    }

    /**
     * Returns the amount of blood the item can still hold
     *
     * @param stack the blood storing item stack
     * @return the amount of blood the item can still hold
     */
    static int getItemCapacity(ItemStack stack) {
        if (getItemMaxBlood(stack) == 0)
            return 0;

        // technically the max blood can be less than the stored blood due
        // to nbt editing, so make sure the capacity is never negative
        // to avoid potential weird issues
        return Math.max(getItemMaxBlood(stack) - getItemBlood(stack), 0);
    }

    /**
     * Checks if an item is at max capacity
     *
     * @param stack the blood storing item stack
     * @return if the item cannot hold any more blood
     */
    static boolean isItemFull(ItemStack stack) {
        if (getItemMaxBlood(stack) == 0)
            return true;

        return getItemBlood(stack) >= getItemMaxBlood(stack);
    }

    /**
     * Checks if an item is empty
     *
     * @param stack the blood storing item stack
     * @return if the item has no stored blood
     */
    static boolean isItemEmpty(ItemStack stack) {
        return getItemBlood(stack) == 0;
    }

    static void registerModelPredicate(Item item) {
        if (item instanceof BloodStorageItem bloodStorage) {
            ModelPredicateProviderRegistry.register(SLResources.BLOOD_STORAGE_ITEM_MODEL_PREDICATE, (stack, world, entity, seed) -> {
                int blood = bloodStorage.getBlood(stack);
                int maxBlood = bloodStorage.getMaxBlood(stack);
                if (maxBlood == 0)
                    return 0.f;
                return (float) blood / maxBlood;
            });
        }
    }
}
