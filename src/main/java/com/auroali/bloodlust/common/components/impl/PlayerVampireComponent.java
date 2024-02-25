package com.auroali.bloodlust.common.components.impl;

import com.auroali.bloodlust.VampireHelper;
import com.auroali.bloodlust.common.abilities.VampireAbility;
import com.auroali.bloodlust.common.abilities.VampireAbilityContainer;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.BloodComponent;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.auroali.bloodlust.common.items.BloodStorageItem;
import com.auroali.bloodlust.common.registry.*;
import com.auroali.bloodlust.config.BLConfig;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Set;
import java.util.UUID;

public class PlayerVampireComponent implements VampireComponent {
    private static final EntityAttributeModifier SPEED_ATTRIBUTE = new EntityAttributeModifier(
            UUID.fromString("a2440a9d-964a-4a84-beac-3c56917cc9fd"),
            "bloodlust.vampire_speed",
            0.02,
            EntityAttributeModifier.Operation.ADDITION
    );

    private final VampireAbilityContainer abilities = new VampireAbilityContainer();
    private final PlayerEntity holder;
    private boolean isVampire;
    private LivingEntity target;
    private int bloodDrainTimer;
    private int timeInSun;
    private int skillPoints;


    public PlayerVampireComponent(PlayerEntity holder) {
        this.holder = holder;
    }

    @Override
    public boolean isVampire() {
        return isVampire;
    }

    @Override
    public void setIsVampire(boolean isVampire) {
        this.isVampire = isVampire;
        BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
        if(!isVampire)
            removeModifiers();
    }

    @Override
    public void drainBloodFrom(LivingEntity entity) {
        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(entity);
        if(!blood.hasBlood() || !blood.drainBlood(holder))
            return;

        // damage the vampire and cancel filling up hunger if the target has blood protection
        if(entity.hasStatusEffect(BLStatusEffects.BLOOD_PROTECTION)) {
            holder.damage(BLDamageSources.blessedWater(entity), BLConfig.INSTANCE.blessedWaterDamage);
            return;
        }

        if(entity.getType().isIn(BLTags.Entities.GOOD_BLOOD))
            holder.getHungerManager().add(2, 0.05f);
        else
            holder.getHungerManager().add(1, 0);

        if(entity.getType().isIn(BLTags.Entities.TOXIC_BLOOD))
            addToxicBloodEffects();

        if(!VampireHelper.isVampire(entity) && entity.hasStatusEffect(StatusEffects.WEAKNESS))
            addBloodSickness(target);

        if(entity.world instanceof ServerWorld serverWorld && entity instanceof VillagerEntity villager) {
            serverWorld.handleInteraction(EntityInteraction.VILLAGER_HURT, holder, villager);
            if(holder.getRandom().nextDouble() > 0.5f)
                entity.wakeUp();
        }
    }

    private void addBloodSickness(LivingEntity target) {
        int level = target.hasStatusEffect(BLStatusEffects.BLOOD_SICKNESS)
                ? target.getStatusEffect(BLStatusEffects.BLOOD_SICKNESS).getAmplifier() + 1
                : 0;

        target.addStatusEffect(new StatusEffectInstance(BLStatusEffects.BLOOD_SICKNESS, 3600, level));
    }

    private void addToxicBloodEffects() {
        holder.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 300, 3));
        holder.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 0));
        if(holder.getRandom().nextDouble() > 0.75)
            holder.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        isVampire = tag.getBoolean("IsVampire");
        timeInSun = tag.getInt("TimeInSun");
        skillPoints = tag.getInt("SkillPoints");
        abilities.load(tag);
        BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("IsVampire", isVampire);
        tag.putInt("TimeInSun", timeInSun);
        tag.putInt("SkillPoints", skillPoints);
        abilities.save(tag);
    }

    @Override
    public void serverTick() {
        if(!isVampire)
            return;

        abilities.tick(holder, this);

        tickSunEffects();
        tickBloodEffects();

        if(target != null) {
            tickBloodDrain();
        }
    }

    private void removeModifiers() {
        AttributeContainer attributes = holder.getAttributes();
        if(attributes.getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).hasModifier(SPEED_ATTRIBUTE))
            attributes.getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).removeModifier(SPEED_ATTRIBUTE);
    }

    private void tickBloodEffects() {
        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(holder);

        if(blood.getBlood() < 6)
            holder.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.WEAKNESS,
                    4,
                    0,
                    true,
                    true
            ));

        if(blood.getBlood() > 4 && !holder.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).hasModifier(SPEED_ATTRIBUTE)) {
            holder.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
                    .addTemporaryModifier(SPEED_ATTRIBUTE);
        } else if(blood.getBlood() <= 4 && holder.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).hasModifier(SPEED_ATTRIBUTE)) {
            holder.getAttributes().getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).removeModifier(SPEED_ATTRIBUTE);
        }
    }

    private void tickSunEffects() {
        if(!isAffectedByDaylight()) {
            if(timeInSun > 0) {
                timeInSun = 0;
                BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
            }
            return;
        }

        if(timeInSun >= getMaxTimeInSun() / 2)
            holder.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.WEAKNESS,
                    4,
                    0,
                    true,
                    true
            ));


        if(timeInSun < getMaxTimeInSun()) {
            timeInSun++;
            BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
            return;
        }

        holder.setOnFireFor(8);
    }

    private void tickBloodDrain() {
        updateTarget();
        if(target == null) {
            bloodDrainTimer = 0;
            BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
            return;
        }

        bloodDrainTimer++;

        target.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SLOWNESS,
                2,
                4,
                true,
                false,
                false
        ));

        if(bloodDrainTimer % 4 == 0)
            holder.world.playSound(
                    null,
                    holder.getX(),
                    holder.getY(),
                    holder.getZ(),
                    BLSounds.DRAIN_BLOOD,
                    SoundCategory.PLAYERS,
                    0.5f,
                    0.85f
            );
        if(bloodDrainTimer >= BLOOD_TIMER_LENGTH) {
            drainBloodFrom(target);
            bloodDrainTimer = 0;
        }

        BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
    }

    // from MobEntity
    private boolean isAffectedByDaylight() {
        if (holder.world.isDay() && !holder.world.isClient) {
            float f = holder.getBrightnessAtEyes();
            BlockPos blockPos = new BlockPos(holder.getX(), holder.getEyeY(), holder.getZ());
            boolean bl = holder.isWet() || holder.inPowderSnow || holder.wasInPowderSnow;
            return f > 0.5F
                    && !bl
                    && holder.world.isSkyVisible(blockPos);
        }

        return false;
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeBoolean(isVampire);
        buf.writeInt(bloodDrainTimer);
        buf.writeInt(timeInSun);
        buf.writeInt(skillPoints);
        abilities.writePacket(buf);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        isVampire = buf.readBoolean();
        bloodDrainTimer = buf.readInt();
        timeInSun = buf.readInt();
        skillPoints = buf.readInt();
        abilities.readPacket(buf);
    }

    @Override
    public void tryStartSuckingBlood() {
        if(canDrainBlood() && target == null) {
            updateTarget();
            if(target == null)
                tryToFillStorage();
        }
    }

    private void tryToFillStorage() {
        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(holder);
        if(blood.getBlood() == 0)
            return;

        if(BloodStorageItem.tryAddBloodToItemInHand(holder, 1))
            blood.drainBlood();
    }

    @Override
    public void stopSuckingBlood() {
        target = null;
        bloodDrainTimer = 0;
        BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
    }

    private boolean canDrainBlood() {
        return !VampireHelper.isMasked(holder);
    }

    @Override
    public int getBloodDrainTimer() {
        return bloodDrainTimer;
    }

    @Override
    public int getMaxTimeInSun() {
        int maxTime = 40;
        ItemStack helmet = holder.getEquippedStack(EquipmentSlot.HEAD);
        if(helmet.isEmpty())
            return maxTime;

        if(helmet.isIn(BLTags.Items.SUN_BLOCKING_HELMETS))
            maxTime *= 4;

        // todo: add enchantment to protect from the sun?
        return maxTime;
    }

    @Override
    public int getTimeInSun() {
        return timeInSun;
    }

    @Override
    public VampireAbilityContainer getAbilties() {
        return abilities;
    }

    @Override
    public int getSkillPoints() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void unlockAbility(VampireAbility ability) {
        getAbilties().addAbility(ability);
        BLEntityComponents.VAMPIRE_COMPONENT.sync(holder);
    }

    private void updateTarget() {
        HitResult result = getTarget();
        if(!canDrainBlood() || result.getType() != HitResult.Type.ENTITY) {
            target = null;
            bloodDrainTimer = 0;
            return;
        }

        LivingEntity entity = ((EntityHitResult) result).getEntity() instanceof LivingEntity living ? living : null;

        if(entity == null
                || !entity.getType().isIn(BLTags.Entities.HAS_BLOOD)
                || !BLEntityComponents.BLOOD_COMPONENT.get(entity).hasBlood()
                || BLEntityComponents.BLOOD_COMPONENT.get(entity).getBlood() == 0
        ) {
            target = null;
            bloodDrainTimer = 0;
            return;
        }

        target = entity;
    }

    private HitResult getTarget() {
        double reachDistance = ReachEntityAttributes.getAttackRange(holder, 3.0);
        Vec3d start = holder.getEyePos();
        Vec3d end = start.add(holder.getRotationVector().multiply(reachDistance));

        HitResult result = holder.world.raycast(new RaycastContext(
                start, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, holder
        ));

        Vec3d vec3d2 = holder.getRotationVec(1.0F);
        Vec3d vec3d3 = start.add(vec3d2.x * reachDistance, vec3d2.y * reachDistance, vec3d2.z * reachDistance);

        Box box = holder.getBoundingBox().stretch(vec3d2.multiply(reachDistance)).expand(1.0, 1.0, 1.0);

        double d = reachDistance * reachDistance;
        if(result != null)
            d = result.getPos().squaredDistanceTo(start);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(holder, start, vec3d3, box, entity -> !entity.isSpectator() && entity.canHit(), d);
        if(entityHitResult != null) {
            double g = start.squaredDistanceTo(entityHitResult.getPos());
            if (g < d || result == null) {
                return entityHitResult;
            }
        }
        return result;
    }
}
