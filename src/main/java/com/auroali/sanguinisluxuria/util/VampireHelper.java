package com.auroali.sanguinisluxuria.util;

import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.registry.SLAdvancementCriterion;
import com.auroali.sanguinisluxuria.common.registry.SLConversions;
import com.auroali.sanguinisluxuria.common.registry.SLStatusEffects;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

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
