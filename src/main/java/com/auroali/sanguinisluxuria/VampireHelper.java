package com.auroali.sanguinisluxuria;

import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.events.BloodStorageFillEvents;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.registry.BLAdvancementCriterion;
import com.auroali.sanguinisluxuria.common.registry.BLStatusEffects;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import com.google.common.base.Predicates;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class VampireHelper {
    /**
     * Checks whether a living entity is a vampire
     *
     * @param entity the entity to check
     * @return whether the entity is a vampire
     */
    public static boolean isVampire(Entity entity) {
        return entity != null && BLEntityComponents.VAMPIRE_COMPONENT.isProvidedBy(entity) && BLEntityComponents.VAMPIRE_COMPONENT.get(entity).isVampire();
    }

    /**
     * Checks if an entity both provides a blood component and is in the has_blood tag
     *
     * @param entity the entity to check
     * @return whether the entity has blood
     */
    public static boolean hasBlood(Entity entity) {
        return entity != null && entity.getType().isIn(BLTags.Entities.HAS_BLOOD) && BLEntityComponents.BLOOD_COMPONENT.isProvidedBy(entity);
    }

    /**
     * Checks if an entity consumes blood (vampire, blood lust effect)
     *
     * @param entity the entity to check
     * @return if the entity consumes blood
     * @apiNote this does not guarantee that the entity is a vampire
     */
    public static boolean consumesBlood(Entity entity) {
        if (entity == null)
            return false;
        return VampireHelper.isVampire(entity) || entity instanceof LivingEntity living && living.hasStatusEffect(BLStatusEffects.BLOOD_LUST);
    }

    /**
     * Checks whether an entity is both a vampire and wearing a carved mask
     *
     * @param entity the entity to check
     * @return whether the entity is both a vampire and wearing a carved mask
     */
    public static boolean isMasked(LivingEntity entity) {
        if (!isVampire(entity))
            return false;

        for (ItemStack stack : entity.getArmorItems()) {
            if (stack.isIn(BLTags.Items.VAMPIRE_MASKS))
                return true;
        }

        return TrinketsApi.getTrinketComponent(entity)
          .map(c -> c.isEquipped(i -> i.isIn(BLTags.Items.VAMPIRE_MASKS)))
          .orElse(false);
    }

    /**
     * Increments the level of blood sickness on an entity. If the entity does not currently have blood sickness, this will add it
     *
     * @param entity the entity to increment the blood sickness level of
     */
    public static void incrementBloodSickness(LivingEntity entity) {
        int level = entity.hasStatusEffect(BLStatusEffects.BLOOD_SICKNESS) ? entity.getStatusEffect(BLStatusEffects.BLOOD_SICKNESS).getAmplifier() + 1 : 0;
        entity.addStatusEffect(new StatusEffectInstance(BLStatusEffects.BLOOD_SICKNESS, 3600, level));
    }

    /**
     * Transfers status effects from one entity to the other, clearing the effects from the original entity
     *
     * @param from the entity to transfer effects from
     * @param to   the entity to transfer effects to
     */
    public static void transferStatusEffects(LivingEntity from, LivingEntity to) {
        for (StatusEffectInstance instance : from.getStatusEffects()) {
            if (instance.isAmbient() || Registries.STATUS_EFFECT.getEntry(instance.getEffectType()).isIn(BLTags.StatusEffects.NON_TRANSFERABLE))
                continue;

            to.addStatusEffect(instance);
        }

        if (from instanceof ServerPlayerEntity player) {
            BLAdvancementCriterion.TRANSFER_EFFECTS.trigger(player, player.getStatusEffects().size());
        }

        from.clearStatusEffects();
    }

    /**
     * Teleports an entity to a random position near their current one. Has the same behaviour as a chorus fruit
     *
     * @param entity the entity to teleport
     */
    public static void teleportRandomly(LivingEntity entity) {
        World world = entity.getWorld();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        for (int i = 0; i < 16; ++i) {
            double newPosX = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * 16.0;
            double newPosY = MathHelper.clamp(
              entity.getY() + (double) (entity.getRandom().nextInt(16) - 8),
              world.getBottomY(),
              (world.getBottomY() + ((ServerWorld) world).getLogicalHeight() - 1)
            );
            double newPosZ = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * 16.0;
            if (entity.hasVehicle()) {
                entity.stopRiding();
            }

            Vec3d pos = entity.getPos();
            if (entity.teleport(newPosX, newPosY, newPosZ, true)) {
                world.emitGameEvent(GameEvent.TELEPORT, pos, GameEvent.Emitter.of(entity));
                SoundEvent soundEvent = SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                world.playSound(null, x, y, z, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                entity.playSound(soundEvent, 1.0F, 1.0F);
                break;
            }
        }
    }

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
        ItemStack stack = getItemInHand(entity, Hand.MAIN_HAND, s -> s.getItem() instanceof BloodStorageItem || BloodStorageFillEvents.ALLOW_ITEM.invoker().allowItem(entity, s));
        Hand hand = getHandForStack(entity, stack);

        ItemStack resultStack = stack;
        if (!(resultStack.getItem() instanceof BloodStorageItem)) {
            resultStack = BloodStorageFillEvents.TRANSFORM_STACK.invoker().createFrom(entity, resultStack);
        }

        int amountToFill = Math.min(amount, BloodStorageItem.getItemCapacity(resultStack));

        if (amountToFill == 0 || !BloodStorageItem.isItemFillable(resultStack) || !BloodStorageItem.incrementItemBlood(resultStack, amountToFill))
            return 0;

        if (consumer != null)
            consumer.accept(resultStack);

        if (stack == resultStack)
            return amountToFill;

        stack.decrement(1);

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

    /**
     * Applies an attribute modifier to an entity if the entity
     * has a certain amount of blood. Otherwise, remove
     * the modifier
     *
     * @param entity         the target entity
     * @param attribute      the attribute to apply the modifier to
     * @param modifier       the modifier to apply
     * @param blood          the entity's blood component
     * @param bloodPredicate the predicate for testing the blood component
     */
    public static void applyModifierFromBlood(LivingEntity entity, EntityAttribute attribute, EntityAttributeModifier modifier, BloodComponent blood, Predicate<BloodComponent> bloodPredicate) {
        AttributeContainer attributes = entity.getAttributes();
        EntityAttributeInstance instance = attributes.getCustomInstance(attribute);
        if (instance != null)
            applyModifierFromBlood(instance, modifier, blood, bloodPredicate);
    }

    /**
     * Applies an attribute modifier to an attribute instance if the entity
     * has a certain amount of blood. Otherwise, remove
     * the modifier
     *
     * @param instance       the attribute instance to apply the modifier to
     * @param modifier       the modifier to apply
     * @param blood          the entity's blood component
     * @param bloodPredicate the predicate for testing the blood component
     */
    public static void applyModifierFromBlood(EntityAttributeInstance instance, EntityAttributeModifier modifier, BloodComponent blood, Predicate<BloodComponent> bloodPredicate) {
        if (instance.hasModifier(modifier) && !bloodPredicate.test(blood))
            instance.removeModifier(modifier);
        else if (!instance.hasModifier(modifier) && bloodPredicate.test(blood))
            instance.addTemporaryModifier(modifier);
    }
}
