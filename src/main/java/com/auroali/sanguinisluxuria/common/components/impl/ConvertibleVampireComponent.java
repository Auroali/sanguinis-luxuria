package com.auroali.sanguinisluxuria.common.components.impl;

import com.auroali.sanguinisluxuria.Bloodlust;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.BLSounds;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import dev.onyxstudios.cca.api.v3.component.ComponentFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Component designed to handle conversion between two entities upon turning into a vampire <br>
 * An example of this would be Villagers turning into Vampire Villagers
 */
public class ConvertibleVampireComponent<T extends LivingEntity> implements VampireComponent {
    EntityType<T> conversionType;
    Consumer<T> conversionHandler;
    LivingEntity holder;

    public ConvertibleVampireComponent(LivingEntity holder, EntityType<T> conversionType, Consumer<T> conversionHandler) {
        this.holder = holder;
        this.conversionType = conversionType;
        this.conversionHandler = conversionHandler;
    }

    public ConvertibleVampireComponent(LivingEntity holder, EntityType<T> conversionType) {
        this(holder, conversionType, e -> {});
    }

    /**
     * Creates a factory for a ConvertibleVampireComponent of the specified type
     * @param type the entity type to convert to
     * @return the factory
     * @param <T> the entity class
     */
    public static <U extends LivingEntity, T extends LivingEntity> ComponentFactory<U, ? extends VampireComponent> create(EntityType<T> type) {
        return e -> new ConvertibleVampireComponent<>(e, type);
    }

    /**
     * Creates a factory for a ConvertibleVampireComponent of the specified type
     * @param type the entity type to convert to
     * @param conversionHandler a consumer that accepts the newly created entity upon conversion, right before it spawns in.
     *                          Can be used for handling custom conversion logic, such as copying certain properties over to the new entity
     * @return the factory
     * @param <T> the entity class
     */
    public static <T extends LivingEntity> Function<LivingEntity, ConvertibleVampireComponent<T>> create(EntityType<T> type, Consumer<T> conversionHandler) {
        return e -> new ConvertibleVampireComponent<>(e, type, conversionHandler);
    }

    @Override
    public boolean isVampire() {
        return false;
    }

    @Override
    public void setIsVampire(boolean isVampire) {
        if(!isVampire)
            return;
        T entity = conversionType.create(holder.world);
        if(entity == null) {
            Bloodlust.LOGGER.error("Could not perform conversion for entity {}!", Registry.ENTITY_TYPE.getId(holder.getType()));
            return;
        }
        entity.setPosition(holder.getX(), holder.getY(), holder.getZ());
        entity.setYaw(holder.getYaw());
        entity.setPitch(holder.getPitch());
        conversionHandler.accept(entity);
        holder.world.spawnEntity(entity);
        if(holder.getType().isIn(BLTags.Entities.HAS_BLOOD) && conversionType.isIn(BLTags.Entities.HAS_BLOOD)) {
            BloodComponent component = BLEntityComponents.BLOOD_COMPONENT.get(holder);
            BloodComponent newBlood = BLEntityComponents.BLOOD_COMPONENT.get(entity);
            // we do this as otherwise the values we set will be overridden
            if(newBlood instanceof EntityBloodComponent c)
                c.initializeBloodValues();
            newBlood.setBlood(Math.min(newBlood.getMaxBlood(), component.getBlood()));
        }

        entity.setCustomName(holder.getCustomName());

        holder.world.playSound(null, holder.getX(), holder.getY(), holder.getZ(), BLSounds.VAMPIRE_CONVERT, holder.getSoundCategory(), 1.0f, 1.0f);
        holder.remove(Entity.RemovalReason.DISCARDED);
    }

    @Override
    public void drainBloodFrom(LivingEntity entity) {}

    @Override
    public void tryStartSuckingBlood() {}

    @Override
    public void stopSuckingBlood() {}

    @Override
    public int getBloodDrainTimer() {
        return 0;
    }

    @Override
    public int getMaxTimeInSun() {
        return 0;
    }

    @Override
    public int getTimeInSun() {
        return 0;
    }

    @Override
    public VampireAbilityContainer getAbilties() {
        return new VampireAbilityContainer();
    }

    @Override
    public int getSkillPoints() {
        return 0;
    }

    @Override
    public void setSkillPoints(int points) {}

    @Override
    public void setLevel(int level) {}

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public void unlockAbility(VampireAbility ability) {}

    @Override
    public boolean isDown() {
        return false;
    }

    @Override
    public void setDowned(boolean down) {}

    @Override
    public void serverTick() {}

    @Override
    public void readFromNbt(NbtCompound tag) {}

    @Override
    public void writeToNbt(NbtCompound tag) {}
}
