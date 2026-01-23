package com.auroali.sanguinisluxuria.common.abilities.active;

import com.auroali.sanguinisluxuria.common.abilities.EntitySyncableVampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.abilities.passive.InfectiousAbility;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.google.common.collect.ImmutableMap;
import dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;

public class MistAbility extends VampireAbility implements EntitySyncableVampireAbility<LivingEntity> {
    public static final EntityAttributeModifier STEP_HEIGHT_MODIFIER = new EntityAttributeModifier(
      UUID.fromString("7a948f03-6f3c-48a6-a0fc-e72a24d0e9dc"),
      "mist.step_height",
      0.6d,
      EntityAttributeModifier.Operation.ADDITION
    );
    public static final EntityAttributeModifier SPEED_MODIFIER = new EntityAttributeModifier(
      UUID.fromString("e3fda4fc-4890-40da-bae6-2169a291d6b8"),
      "mist.movement_speed",
      0.2d,
      EntityAttributeModifier.Operation.MULTIPLY_BASE
    );
    public static final EntityAttributeModifier ARMOR_TOUGHNESS_MODIFIER = new EntityAttributeModifier(
      UUID.fromString("3f9f87bc-eb91-4052-8bf8-3f2c8d2cbb80"),
      "mist.armor",
      -1.d,
      EntityAttributeModifier.Operation.MULTIPLY_TOTAL
    );
    public static final EntityAttributeModifier ATTACK_DAMANGE_MODIFIER = new EntityAttributeModifier(
      UUID.fromString("2ad8aa87-b38e-4eea-85fc-09bbc8e5d271"),
      "mist.damage",
      -0.25d,
      EntityAttributeModifier.Operation.MULTIPLY_TOTAL
    );

    private static final Map<EntityAttribute, EntityAttributeModifier> MODIFIERS = ImmutableMap
      .<EntityAttribute, EntityAttributeModifier>builder()
      .put(StepHeightEntityAttributeMain.STEP_HEIGHT, STEP_HEIGHT_MODIFIER)
      .put(EntityAttributes.GENERIC_MOVEMENT_SPEED, SPEED_MODIFIER)
      .put(EntityAttributes.GENERIC_ARMOR, ARMOR_TOUGHNESS_MODIFIER)
      .put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, ARMOR_TOUGHNESS_MODIFIER)
      .put(EntityAttributes.GENERIC_ATTACK_DAMAGE, ATTACK_DAMANGE_MODIFIER)
      .build();

    @Override
    public void activate(LivingEntity entity, VampireComponent component) {
        VampireAbilityContainer.AbilityEntry entry = component.getAbilityContainer().get(this);
        if (!component.isMist() && entry.isOnCooldown())
            return;

        boolean isMist = !component.isMist();
        // set the cooldown first to avoid a double sync
        this.setCooldown(entry, isMist);
        component.setMist(isMist);
        if (isMist) {
            this.applyModifiers(entity);
            this.sync(entity, entity);
        } else {
            this.removeModifiers(entity);
        }
    }

    void setCooldown(VampireAbilityContainer.AbilityEntry entry, boolean isMist) {
        if (isMist)
            entry.setCooldown(200);
            // acts as a timer for the ability
        else entry.setCooldown(3600);
    }

    @Override
    public void onCooldownEnd(LivingEntity entity, VampireComponent component, VampireAbilityContainer container) {
        if (component.isMist()) {
            this.setCooldown(container.get(this), false);
            component.setMist(false);
            this.removeModifiers(entity);
        }
    }

    @Override
    public void handle(LivingEntity entity, LivingEntity data) {
        World world = entity.getWorld();
        Random random = entity.getRandom();
        Box boundingBox = entity.getBoundingBox();
        int numParticles = (int) (20 * entity.getBoundingBox().getAverageSideLength());
        for (int i = 0; i < numParticles; i++) {
            double x = boundingBox.minX + (random.nextFloat() * boundingBox.getXLength());
            double y = boundingBox.minY + (random.nextFloat() * boundingBox.getYLength());
            double z = boundingBox.minZ + (random.nextFloat() * boundingBox.getZLength());
            double velocityX = random.nextGaussian() * 0.05d;
            double velocityY = random.nextGaussian() * 0.05d;
            double velocityZ = random.nextGaussian() * 0.05d;
            world.addParticle(ParticleTypes.SMOKE, x, y, z, velocityX, velocityY, velocityZ);
        }
    }

    @Override
    public void onAbilityRemoved(LivingEntity entity, VampireComponent vampire) {
        super.onAbilityRemoved(entity, vampire);
        vampire.setMist(false);
        this.removeModifiers(entity);
    }

    private void applyModifiers(LivingEntity entity) {
        MODIFIERS.forEach((attrib, mod) -> {
            EntityAttributeInstance instance = entity.getAttributeInstance(attrib);
            if (instance != null && !instance.hasModifier(mod)) {
                instance.addPersistentModifier(mod);
            }
        });
    }

    private void removeModifiers(LivingEntity entity) {
        MODIFIERS.forEach((attrib, mod) -> {
            EntityAttributeInstance instance = entity.getAttributeInstance(attrib);
            if (instance != null && instance.hasModifier(mod)) {
                instance.removeModifier(mod);
            }
        });
    }

    public static void transferInfectiousEffects(LivingEntity entity) {
        World world = entity.getWorld();
        world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), entity.getBoundingBox(), LivingEntity::isAlive)
          .forEach(target -> {
              InfectiousAbility.transferStatusEffects(entity, target, false);
          });
    }
}
