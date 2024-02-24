package com.auroali.bloodlust.common.items;

import com.auroali.bloodlust.VampireHelper;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.registry.BLItems;
import com.auroali.bloodlust.common.registry.BLSounds;
import com.auroali.bloodlust.common.registry.BLStatusEffects;
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

        ItemStack stack = ItemStack.EMPTY;
        if(entity.getOffHandStack().getItem() instanceof BloodStorageItem || entity.getOffHandStack().isOf(Items.GLASS_BOTTLE)) {
            stack = entity.getOffHandStack();
        }
        if(entity.getMainHandStack().getItem() instanceof BloodStorageItem || entity.getMainHandStack().isOf(Items.GLASS_BOTTLE)) {
            stack = entity.getMainHandStack();
        }
        return !stack.isEmpty();
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

            if(!VampireHelper.isVampire(user))
                applyNonVampireEffects(user);
            if(VampireHelper.isVampire(user))
                bloodToAdd = BLEntityComponents.BLOOD_COMPONENT.get(user).addBlood(bloodToAdd);

            if(!(user instanceof PlayerEntity entity && entity.isCreative()))
                setStoredBlood(stack, getStoredBlood(stack) - bloodToAdd);
            if(getStoredBlood(stack) == 0 && emptyItem != null)
                return new ItemStack(emptyItem, stack.getCount());
        }


        return stack;
    }

    private void applyNonVampireEffects(LivingEntity user) {
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 2));
        if(user.getRandom().nextDouble() > 0.9)
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));

        int bloodSicknessLevel = user.getStatusEffect(BLStatusEffects.BLOOD_SICKNESS) == null ? 0 : user.getStatusEffect(BLStatusEffects.BLOOD_SICKNESS).getAmplifier() + 1;
        user.addStatusEffect(new StatusEffectInstance(BLStatusEffects.BLOOD_SICKNESS, 3600, bloodSicknessLevel));
    }

    public BloodStorageItem emptyItem(Item item) {
        this.emptyItem = item;
        return this;
    }

    public float modelPredicate(ItemStack stack, ClientWorld world, LivingEntity entity, int seed) {
        int storedBlood = getStoredBlood(stack);
        if(storedBlood == 0)
            return -1;
        return storedBlood / (float) maxBlood;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(getStoredBlood(user.getStackInHand(hand)) == 0)
            return TypedActionResult.pass(user.getStackInHand(hand));
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    public void setStoredBlood(ItemStack stack, int blood) {
        stack.getOrCreateNbt().putInt("StoredBlood", blood);
    }

    public int getMaxBlood() {
        return maxBlood;
    }

    public int getStoredBlood(ItemStack stack) {
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

    public static boolean tryAddBloodToItemInHand(LivingEntity entity, int amount) {
        ItemStack stack = ItemStack.EMPTY;
        Hand hand = Hand.MAIN_HAND;
        BloodStorageItem storage = null;
        if(entity.getOffHandStack().getItem() instanceof BloodStorageItem item) {
            stack = entity.getOffHandStack();
            storage = item;
        } else if(entity.getOffHandStack().isOf(Items.GLASS_BOTTLE)) {
            hand = Hand.OFF_HAND;
            stack = new ItemStack(BLItems.BLOOD_BOTTLE);
            storage = BLItems.BLOOD_BAG;
        }
        if(entity.getMainHandStack().getItem() instanceof BloodStorageItem item) {
            stack = entity.getMainHandStack();
            storage = item;
        } else if(entity.getMainHandStack().isOf(Items.GLASS_BOTTLE)) {
            hand = Hand.MAIN_HAND;
            stack = new ItemStack(BLItems.BLOOD_BOTTLE);
            storage = BLItems.BLOOD_BAG;
        }

        if(stack.isEmpty() || storage.getStoredBlood(stack) + amount > storage.getMaxBlood())
            return false;

        if(entity instanceof PlayerEntity player && player.getItemCooldownManager().isCoolingDown(stack.getItem()))
            return false;

        storage.setStoredBlood(stack, storage.getStoredBlood(stack) + amount);

        entity.setStackInHand(hand, stack);

        if(entity instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(storage, 5);
        }
        return true;
    }
}
