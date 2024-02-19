package com.auroali.bloodlust.common.items;

import com.auroali.bloodlust.VampireHelper;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.registry.BLSounds;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class BloodStorageItem extends Item {
    final int maxBlood;

    public BloodStorageItem(Settings settings, int maxBlood) {
        super(settings);
        this.maxBlood = maxBlood;
    }

    public static boolean isHoldingBloodFillableItem(LivingEntity entity) {
        ItemStack stack = ItemStack.EMPTY;
        if(entity.getOffHandStack().getItem() instanceof BloodStorageItem) {
            stack = entity.getOffHandStack();
        }
        if(entity.getMainHandStack().getItem() instanceof BloodStorageItem) {
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
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 2));
            if(VampireHelper.isVampire(user))
                bloodToAdd = BLEntityComponents.BLOOD_COMPONENT.get(user).addBlood(bloodToAdd);

            setStoredBlood(stack, getStoredBlood(stack) - bloodToAdd);
        }


        return stack;
    }

    public float modelPredicate(ItemStack stack, ClientWorld world, LivingEntity entity, int seed) {
        int storedBlood = getStoredBlood(stack);
        if(storedBlood == 0)
            return 0;
        return Math.max(storedBlood / (float) maxBlood, 0.3f);
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
        BloodStorageItem storage = null;
        if(entity.getOffHandStack().getItem() instanceof BloodStorageItem item) {
            stack = entity.getOffHandStack();
            storage = item;
        }
        if(entity.getMainHandStack().getItem() instanceof BloodStorageItem item) {
            stack = entity.getMainHandStack();
            storage = item;
        }

        if(stack.isEmpty() || storage.getStoredBlood(stack) + amount >= storage.getMaxBlood())
            return false;

        if(entity instanceof PlayerEntity player && player.getItemCooldownManager().isCoolingDown(stack.getItem()))
            return false;

        storage.setStoredBlood(stack, storage.getStoredBlood(stack) + amount);
        if(entity instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(storage, 5);
        }
        return true;
    }
}
