package com.auroali.sanguinisluxuria.common.components;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.VampireHungerManager;
import com.auroali.sanguinisluxuria.common.abilities.SyncableVampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.abilities.passive.InfectiousAbility;
import com.auroali.sanguinisluxuria.common.events.BloodEvents;
import com.auroali.sanguinisluxuria.common.registry.*;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.event.GameEvent;

public interface VampireComponent extends Component, AutoSyncedComponent, ServerTickingComponent {
    ComponentKey<VampireComponent> KEY = ComponentRegistry.getOrCreate(SLResources.VAMPIRE_COMPONENT_ID, VampireComponent.class);

    /**
     * @return if the holding entity is a vampire
     */
    boolean isVampire();

    /**
     * Set the vampire state of the holding entity
     *
     * @param vampire the new vampire state
     * @apiNote this will not have an effect on all vampire components,
     * as some have a fixed state (such as {@link com.auroali.sanguinisluxuria.common.components.impl.EntityVampireComponent})
     */
    void setVampire(boolean vampire);

    /**
     * @return if the holding entity is currently misted
     */
    boolean isMist();

    /**
     * Sets the mist state of the holding entity
     *
     * @param mist the new mist state
     */
    void setMist(boolean mist);

    /**
     * @return if the holding entity is downed
     */
    boolean isDowned();

    /**
     * Sets the downed state of the holding entity
     *
     * @param downed the new downed state
     */
    void setDowned(boolean downed);

    /**
     * Returns the {@link VampireAbilityContainer} containing all of this component's abilities
     *
     * @return this component's ability container
     */
    VampireAbilityContainer getAbilityContainer();

    /**
     * Calculates the amount of damage taken by a vampire for a given damage source
     *
     * @param amount the initial amount of damage
     * @param source the damage source
     * @return the amount of damage that should be taken by the vampire
     * @see SLEntityAttributes#VULNERABILITY
     */
    static float calculateDamage(float amount, float vulnerability, DamageSource source) {
        if (source.isIn(SLTags.DamageTypes.VAMPIRES_WEAK_TO))
            return amount * vulnerability;

        return amount;
    }

    /**
     * Checks if a particular damage source is effective against vampires
     *
     * @param source the damage source
     * @return whether the source is effective and damage should be increased
     */
    static boolean isEffectiveAgainstVampires(DamageSource source) {
        if (source.isIn(SLTags.DamageTypes.VAMPIRES_WEAK_TO))
            return true;

        if (source.getAttacker() instanceof LivingEntity entity && entity.getAttributeValue(SLEntityAttributes.BLESSED_DAMAGE) > 0) {
            return true;
        }

        if (source.getAttacker() instanceof LivingEntity entity) {
            ItemStack stack = entity.getMainHandStack();
            int level = EnchantmentHelper.getLevel(Enchantments.SMITE, stack);
            return level > 0;
        }

        return false;
    }

    static void handleBloodDrain(VampireComponent vampire, LivingEntity target, LivingEntity vampireEntity) {
        BloodComponent blood = BloodComponent.KEY.get(target);
        // if the target doesn't have blood or cannot be drained, we can't fill hunger
        if (!VampireHelper.hasBlood(target) || !BloodEvents.ALLOW_BLOOD_DRAIN.invoker().allowBloodDrain(vampireEntity, target) || !blood.drainBlood(1, vampireEntity))
            return;

        // damage the vampire and cancel filling up hunger if the target has blood protection
        if (target.hasStatusEffect(SLStatusEffects.BLOOD_PROTECTION)) {
            vampireEntity.damage(SLDamageSources.blessedWater(target), 2.f);
            vampireEntity.setOnFireFor(12);
            return;
        }


        // handle differences between adding blood to the player and regular entities
        // (such as saturation)
        if (vampireEntity instanceof PlayerEntity player)
            ((VampireHungerManager) player.getHungerManager()).sanguinisluxuria$addHunger(1, 0.25f);
        else BloodComponent.KEY.get(vampireEntity).addBlood(1);

        BloodEvents.BLOOD_DRAINED.invoker().onBloodDrained(vampireEntity, target, 1);

        if (vampire instanceof EntityTrackingDrainer tracker)
            tracker.setLastDrained(target);

        // reset the downed state
        vampire.setDowned(false);

        vampireEntity.getWorld().emitGameEvent(vampireEntity, GameEvent.DRINK, vampireEntity.getPos());

        // if the potion transfer ability is unlocked, transfer potion effects to the target
        if (vampire.getAbilityContainer().has(SLVampireAbilities.INFECTIOUS)) {
            SyncableVampireAbility.syncAbility(
              target,
              SLVampireAbilities.INFECTIOUS,
              InfectiousAbility.InfectiousData.create(target, VampireHelper.transferStatusEffects(vampireEntity, target))
            );
        }

        SLBloodDrainEffects.applyTo(vampireEntity, target);

        // allow conversion of entities with weakness
        if (!VampireHelper.isVampire(target) && target.hasStatusEffect(StatusEffects.WEAKNESS)) {
            if (vampireEntity instanceof ServerPlayerEntity player)
                SLAdvancementCriterion.INFECT_ENTITY.trigger(player);
            VampireHelper.incrementBloodSickness(target);
        }

        // villagers have a 50% chance to wake up when having their blood drained
        // it also adds negative reputation to the player
        if (target.getWorld() instanceof ServerWorld serverWorld && target instanceof VillagerEntity villager) {
            serverWorld.handleInteraction(EntityInteraction.VILLAGER_HURT, vampireEntity, villager);
        }

        if (vampireEntity.getRandom().nextDouble() > 0.5f)
            target.wakeUp();
    }
}
