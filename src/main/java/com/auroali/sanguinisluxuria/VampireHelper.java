package com.auroali.sanguinisluxuria;

import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.events.BloodStorageFillEvents;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.registry.SLAdvancementCriterion;
import com.auroali.sanguinisluxuria.common.registry.SLConversions;
import com.auroali.sanguinisluxuria.common.registry.SLStatusEffects;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import com.google.common.base.Predicates;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.ArrayList;
import java.util.List;
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
        return entity != null && VampireComponent.KEY.isProvidedBy(entity) && VampireComponent.KEY.get(entity).isVampire();
    }

    /**
     * Checks if an entity both provides a blood component and is in the has_blood tag
     *
     * @param entity the entity to check
     * @return whether the entity has blood
     */
    public static boolean hasBlood(Entity entity) {
        return entity != null && entity.getType().isIn(SLTags.Entities.HAS_BLOOD) && BloodComponent.KEY.isProvidedBy(entity);
    }

    /**
     * Checks if an entity consumes blood (vampire, bloodlust effect)
     *
     * @param entity the entity to check
     * @return if the entity consumes blood
     * @apiNote this does not guarantee that the entity is a vampire
     */
    public static boolean consumesBlood(Entity entity) {
        if (entity == null)
            return false;
        return VampireHelper.isVampire(entity) || entity instanceof LivingEntity living && living.hasStatusEffect(SLStatusEffects.BLOOD_LUST);
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
            if (stack.isIn(SLTags.Items.VAMPIRE_MASKS))
                return true;
        }

        return TrinketsApi.getTrinketComponent(entity)
          .map(c -> c.isEquipped(i -> i.isIn(SLTags.Items.VAMPIRE_MASKS)))
          .orElse(false);
    }

    /**
     * Transfers status effects from one entity to the other, clearing the effects from the original entity
     *
     * @param from the entity to transfer effects from
     * @param to   the entity to transfer effects to
     * @return the list of successfully transferred status effect instances
     */
    public static List<StatusEffectInstance> transferStatusEffects(LivingEntity from, LivingEntity to, boolean clearOriginal) {
        List<StatusEffectInstance> transferredEffects = new ArrayList<>(from.getStatusEffects().size());
        for (StatusEffectInstance instance : from.getStatusEffects()) {
            if (instance.isAmbient() || Registries.STATUS_EFFECT.getEntry(instance.getEffectType()).isIn(SLTags.StatusEffects.NON_TRANSFERABLE))
                continue;

            StatusEffectInstance toAdd = new StatusEffectInstance(
              instance.getEffectType(),
              clearOriginal ? instance.getDuration() : Math.min(instance.getDuration(), 100),
              instance.getAmplifier(),
              instance.isAmbient(),
              instance.shouldShowParticles(),
              instance.shouldShowIcon(),
              null,
              instance.getFactorCalculationData()
            );
            to.addStatusEffect(toAdd);
            transferredEffects.add(toAdd);
        }

        if (from instanceof ServerPlayerEntity player) {
            SLAdvancementCriterion.TRANSFER_EFFECTS.trigger(player, transferredEffects);
        }

        if (clearOriginal) {
            // prevent removing effects that weren't transferred
            transferredEffects.forEach(effect -> from.removeStatusEffect(effect.getEffectType()));
        }
        return transferredEffects;
    }

    /**
     * Teleports an entity to a random position near their current one. Has the same behaviour as a chorus fruit
     *
     * @param entity the entity to teleport
     */
    public static void teleportRandomly(LivingEntity entity, int radius) {
        World world = entity.getWorld();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        for (int i = 0; i < 16; ++i) {
            double newPosX = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * radius;
            double newPosY = MathHelper.clamp(
              entity.getY() + (double) (entity.getRandom().nextInt(radius) - radius / 2.f),
              world.getBottomY(),
              (world.getBottomY() + ((ServerWorld) world).getLogicalHeight() - 1)
            );
            double newPosZ = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * radius;
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

    public static HitResult raycastEntity(LivingEntity entity, Vec3d direction, Predicate<Entity> predicate, double distance) {
        Vec3d start = entity.getEyePos();

        Vec3d end = start.add(direction.x * distance, direction.y * distance, direction.z * distance);

        HitResult result = entity.getWorld().raycast(new RaycastContext(
          start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity
        ));

        Box box = entity.getBoundingBox().stretch(direction.multiply(distance)).expand(1.0, 1.0, 1.0);

        double targetDistance = distance * distance;
        if (result != null)
            targetDistance = result.getPos().squaredDistanceTo(start);

        EntityHitResult entityHitResult = ProjectileUtil.raycast(entity, start, end, box, predicate, targetDistance);
        if (entityHitResult != null) {
            double entityDistance = start.squaredDistanceTo(entityHitResult.getPos());
            if (entityDistance < targetDistance || result == null) {
                return entityHitResult;
            }
        }
        return result;
    }

    public static HitResult raycastEntity(LivingEntity entity, Vec3d direction, Predicate<Entity> predicate) {
        double reach = ReachEntityAttributes.getReachDistance(entity, 4.5d);
        return raycastEntity(entity, direction, predicate, reach);
    }

    /**
     * Attempts to perform a conversion to a vampire, with the condition that the entity must
     * have the bloodlust effect
     *
     * @param entity the entity to convert
     * @return if it was successful
     */
    public static boolean attemptConvertToVampire(LivingEntity entity) {
        // todo: maybe more conditions than just dying with bloodlust?
        if (!entity.hasStatusEffect(SLStatusEffects.BLOOD_LUST) || entity.hasStatusEffect(SLStatusEffects.BLOOD_PROTECTION))
            return false;

        boolean success = SLConversions.convertEntity(new ConversionContext(
          entity.getWorld(),
          entity,
          ConversionContext.Conversion.CONVERTING,
          convertedEntity -> {
              if (VampireHelper.isVampire(convertedEntity)) {
                  VampireComponent.KEY.maybeGet(convertedEntity).ifPresent(vampire -> vampire.setDowned(true));
                  BloodComponent.KEY.maybeGet(convertedEntity).ifPresent(blood -> blood.setBlood(0));
              }
              if (convertedEntity instanceof LivingEntity living) {
                  living.setHealth(living.getMaxHealth());
              }
          }));
        if (success && entity instanceof ServerPlayerEntity player) {
            SLAdvancementCriterion.CONVERT.trigger(player, ConversionContext.Conversion.CONVERTING);
        }
        return success;
    }
}
