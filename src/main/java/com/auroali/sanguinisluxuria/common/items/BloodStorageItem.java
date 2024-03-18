package com.auroali.sanguinisluxuria.common.items;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.registry.BLItems;
import com.auroali.sanguinisluxuria.common.registry.BLSounds;
import com.auroali.sanguinisluxuria.common.registry.BLStatusEffects;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class BloodStorageItem extends Item {
    final int maxBlood;
    Item emptyItem = null;

    public BloodStorageItem(Settings settings, int maxBlood) {
        super(settings);
        this.maxBlood = maxBlood;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if(this.emptyItem == null)
            super.appendStacks(group, stacks);
        if(isIn(group)) {
            ItemStack stack = new ItemStack(this);
            setStoredBlood(stack, getMaxBlood());
            stacks.add(stack);
        }
    }

    public static boolean isHoldingBloodFillableItem(LivingEntity entity) {
        if(entity == null)
            return false;

        return !getBloodStorageItemInHand(entity, Hand.OFF_HAND).isEmpty() || !getBloodStorageItemInHand(entity, Hand.MAIN_HAND).isEmpty();
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        if(!world.isClient) {
            int bloodToAdd = Math.min(2, getStoredBlood(stack));
            if(bloodToAdd == 0)
                return stack;

            if(!VampireHelper.isVampire(user)) {
                applyNonVampireEffects(user);
                // i know i could use a food component but this seems like it gives more control
                if(user instanceof ServerPlayerEntity e && e.getRandom().nextInt(2) == 0)
                    e.getHungerManager().add(1, 0);
            } else if(VampireHelper.isVampire(user))
                bloodToAdd = BLEntityComponents.BLOOD_COMPONENT.get(user).addBlood(bloodToAdd);

            if(!(user instanceof PlayerEntity entity && entity.isCreative()))
                setStoredBlood(stack, getStoredBlood(stack) - bloodToAdd);
            if(getStoredBlood(stack) == 0 && emptyItem != null)
                return new ItemStack(emptyItem, stack.getCount());
        }


        return stack;
    }

    public static void applyNonVampireEffects(LivingEntity user) {
        if(user.hasStatusEffect(BLStatusEffects.BLOOD_PROTECTION) || user.world.isClient)
            return;

        user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 2));
        if(user.getRandom().nextInt(2) == 0)
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 120, 0));
        if(user.getRandom().nextInt(4) == 0)
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));

        int bloodSicknessLevel = user.getStatusEffect(BLStatusEffects.BLOOD_SICKNESS) == null ? 0 : user.getStatusEffect(BLStatusEffects.BLOOD_SICKNESS).getAmplifier() + 1;
        user.addStatusEffect(new StatusEffectInstance(BLStatusEffects.BLOOD_SICKNESS, 3600, bloodSicknessLevel));
    }

    /**
     * The item to replace this one with when it runs out of blood
     * @param item the item to use
     * @see BLItems#BLOOD_BOTTLE
     */
    public BloodStorageItem emptyItem(Item item) {
        this.emptyItem = item;
        return this;
    }

    /**
     * The model predicate to use with ModelPredicateProviderRegistry
     * @see net.minecraft.client.item.ModelPredicateProviderRegistry
     */
    @SuppressWarnings("unused")
    public static float modelPredicate(ItemStack stack, ClientWorld world, LivingEntity entity, int seed) {
        int storedBlood = getStoredBlood(stack);
        int maxBlood = getMaxBlood(stack);
        return storedBlood / (float) maxBlood;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(getStoredBlood(user.getStackInHand(hand)) == 0)
            return TypedActionResult.pass(user.getStackInHand(hand));
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    /**
     * Sets the amount of blood stored in a given stack
     * @param stack the blood storage item stack
     * @param blood the amount of blood
     */
    public static ItemStack setStoredBlood(ItemStack stack, int blood) {
        stack.getOrCreateNbt().putInt("StoredBlood", blood);
        return stack;
    }

    /**
     * Gets the maximum amount of blood this item can store
     * @return the maximum amount of blood
     */
    public int getMaxBlood() {
        return maxBlood;
    }

    /**
     * Gets the maximum amount of blood this item can store
     * @param stack the blood storing item
     * @return the maximum amount of blood
     */
    public static int getMaxBlood(ItemStack stack) {
        if(stack.getItem() instanceof BloodStorageItem item)
            return item.getMaxBlood();
        return 0;
    }

    /**
     * Gets the amount of blood stored in a stack
     * @param stack the item stack
     * @return the amount of blood stored in the stack
     */
    public static int getStoredBlood(ItemStack stack) {
        return stack.getOrCreateNbt().getInt("StoredBlood");
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 40;
    }

    @Override
    public SoundEvent getDrinkSound() {
        return BLSounds.DRAIN_BLOOD;
    }

    private static ItemStack getBloodStorageItemInHand(LivingEntity entity, Hand hand) {
        ItemStack stack = entity.getStackInHand(hand);
        if(stack.isOf(Items.GLASS_BOTTLE))
            return new ItemStack(BLItems.BLOOD_BOTTLE);
        return stack.getItem() instanceof BloodStorageItem ? stack : ItemStack.EMPTY;
    }

    /**
     * Tries to fill a valid blood-storing item in an entity's hand
     * @param entity the entity holding the item
     * @param amount the amount of blood to add
     * @return if the entity was both holding a valid item and the item could successfully be filled by amount
     */
    public static boolean tryAddBloodToItemInHand(LivingEntity entity, int amount) {
        ItemStack stack = getBloodStorageItemInHand(entity, Hand.OFF_HAND);
        Hand hand = Hand.OFF_HAND;
        if(!getBloodStorageItemInHand(entity, Hand.MAIN_HAND).isEmpty()) {
            stack = getBloodStorageItemInHand(entity, Hand.MAIN_HAND);
            hand = Hand.MAIN_HAND;
        }

        if(stack.isEmpty() || getStoredBlood(stack) + amount > getMaxBlood(stack))
            return false;

        setStoredBlood(stack, getStoredBlood(stack) + amount);

        Item originalHeldItem = entity.getStackInHand(hand).getItem();

        if(stack != entity.getStackInHand(hand))
            entity.getStackInHand(hand).decrement(1);

        if(stack == entity.getStackInHand(hand) || entity.getStackInHand(hand).isEmpty())
            entity.setStackInHand(hand, stack);
        else if(!(entity instanceof PlayerEntity e && e.getInventory().insertStack(stack))) {
            if (entity instanceof PlayerEntity player)
                player.dropItem(stack, false);
            else entity.dropStack(stack);
        }

        if(entity instanceof PlayerEntity player)
            player.getItemCooldownManager().set(originalHeldItem, 10);

        return true;
    }

    /**
     * @return the item to replace this one with when emptied
     */
    public Item getEmptyItem() {
        return this.emptyItem;
    }
}
