package com.auroali.sanguinisluxuria.common.components.impl;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.enchantments.SunProtectionEnchantment;
import com.auroali.sanguinisluxuria.common.events.VampireSunEvents;
import com.auroali.sanguinisluxuria.common.network.ConditionalPacketWriter;
import com.auroali.sanguinisluxuria.common.registry.SLEntityAttributes;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class PlayerVampireComponent implements VampireComponent {
    private static final ConditionalPacketWriter<SyncFlags, PlayerVampireComponent> PACKET_WRITER = ConditionalPacketWriter
      .builder(SyncFlags.class, PlayerVampireComponent.class)
      .section(SyncFlags.STATE,
        (buf, component) -> {
            buf.writeBoolean(component.isVampire);
            buf.writeBoolean(component.isMist);
            buf.writeBoolean(component.isDowned);
        },
        (buf, component) -> {
            component.isVampire = buf.readBoolean();
            component.isMist = buf.readBoolean();
            component.isDowned = buf.readBoolean();
        }
      )
      .section(SyncFlags.SUN,
        (buf, component) -> buf.writeVarInt(component.sunTicks),
        (buf, component) -> component.sunTicks = buf.readVarInt()
      )
      .section(SyncFlags.ABILITIES,
        (buf, component) -> component.container.writePacket(buf),
        (buf, component) -> component.container.readPacket(buf)
      )
      .build();
    private static final EntityAttributeModifier SPEED_ATTRIBUTE = new EntityAttributeModifier(
      UUID.fromString("a2440a9d-964a-4a84-beac-3c56917cc9fd"),
      "bloodlust.vampire_speed",
      0.02,
      EntityAttributeModifier.Operation.ADDITION
    );
    private static final EntityAttributeModifier HEALTH_ATTRIBUTE = new EntityAttributeModifier(
      UUID.fromString("92d94e2c-1582-44f4-8563-c8341af52efb"),
      "bloodlust.vampire_health",
      4,
      EntityAttributeModifier.Operation.ADDITION
    );

    private final PlayerEntity holder;
    private final ConditionalPacketWriter<SyncFlags, PlayerVampireComponent>.State state;
    private boolean isVampire;
    private final VampireAbilityContainer container;
    private boolean isDowned;
    private boolean isMist;
    private int sunTicks;

    public PlayerVampireComponent(PlayerEntity holder) {
        this.holder = holder;
        this.state = PACKET_WRITER.createFullState(ConditionalPacketWriter.WriteBehaviour.ALL_ON_EMPTY);
        this.container = new VampireAbilityContainer(() -> this.state.update(SyncFlags.ABILITIES));
    }

    @Override
    public boolean isVampire() {
        return this.isVampire;
    }

    @Override
    public void setVampire(boolean isVampire) {
        this.isVampire = isVampire;
        if (!isVampire) {
            this.removeModifiers();
            this.sunTicks = 0;
            this.isDowned = false;
            for (VampireAbility a : this.container.abilities()) {
                a.onUnVampire(this.holder, this);
            }
        }
        this.state.update(SyncFlags.STATE);
        VampireComponent.KEY.sync(this.holder);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.isVampire = tag.getBoolean("IsVampire");
        this.sunTicks = tag.getInt("TimeInSun");
        this.isDowned = tag.getBoolean("IsDowned");
        this.isMist = tag.getBoolean("IsMist");
        this.container.readNbt(tag);
        this.state.updateAll();
        VampireComponent.KEY.sync(this.holder);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("IsVampire", this.isVampire);
        tag.putInt("TimeInSun", this.sunTicks);
        tag.putBoolean("IsDowned", this.isDowned);
        tag.putBoolean("IsMist", this.isMist);
        this.container.writeNbt(tag);
    }

    @Override
    public void serverTick() {
        if (!this.isVampire)
            return;

        this.container.tick(this.holder, this);

        this.tickSunEffects();
        this.tickBloodEffects();

        if (this.isDowned) {
            this.holder.addStatusEffect(new StatusEffectInstance(
              StatusEffects.WEAKNESS,
              4,
              9,
              true,
              false,
              false
            ));
        }

        if (this.state.isSet())
            VampireComponent.KEY.sync(this.holder);
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        this.state.write(buf, this);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.state.read(buf, this);
    }


    @Override
    public VampireAbilityContainer getAbilityContainer() {
        return this.container;
    }

    @Override
    public boolean isDowned() {
        return this.isDowned;
    }

    @Override
    public void setDowned(boolean down) {
        this.isDowned = down;
        this.isMist = false;
        this.state.update(SyncFlags.STATE);
        VampireComponent.KEY.sync(this.holder);
    }

    @Override
    public boolean isMist() {
        return this.isMist;
    }

    @Override
    public void setMist(boolean isMist) {
        this.isMist = isMist;
        this.state.update(SyncFlags.STATE);
        VampireComponent.KEY.sync(this.holder);
    }

    private void removeModifiers() {
        AttributeContainer attributes = this.holder.getAttributes();
        EntityAttributeInstance speedInstance = attributes.getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (speedInstance != null && speedInstance.hasModifier(SPEED_ATTRIBUTE))
            speedInstance.removeModifier(SPEED_ATTRIBUTE);
    }

    private void tickBloodEffects() {
        BloodComponent blood = BloodComponent.KEY.get(this.holder);

        if (blood.getBlood() < 6)
            this.holder.addStatusEffect(new StatusEffectInstance(
              StatusEffects.WEAKNESS,
              4,
              0,
              true,
              true
            ));

        VampireHelper.applyModifierFromBlood(this.holder, EntityAttributes.GENERIC_MOVEMENT_SPEED, SPEED_ATTRIBUTE, blood, b -> b.getBlood() >= 4);
        VampireHelper.applyModifierFromBlood(this.holder, EntityAttributes.GENERIC_MAX_HEALTH, HEALTH_ATTRIBUTE, blood, b -> b.getBlood() >= 2);
    }

    private void tickSunEffects() {
        if (!this.isAffectedByDaylight()) {
            if (this.sunTicks > 0) {
                this.sunTicks = 0;
                this.state.update(SyncFlags.SUN);
            }
            return;
        }

        if (this.sunTicks >= 1)
            this.holder.addStatusEffect(new StatusEffectInstance(
              StatusEffects.WEAKNESS,
              10,
              0,
              true,
              true
            ));


        if (this.sunTicks < this.getMaxTimeInSun()) {
            this.sunTicks++;
            this.state.update(SyncFlags.SUN);
            return;
        }

        this.holder.setOnFireFor(6);
    }

    // from MobEntity
    private boolean isAffectedByDaylight() {
        if (this.holder.getWorld().isDay() && !this.holder.getWorld().isClient) {
            float f = this.holder.getBrightnessAtEyes();
            BlockPos blockPos = BlockPos.ofFloored(this.holder.getX(), this.holder.getEyeY(), this.holder.getZ());
            boolean bl = this.holder.isWet() || this.holder.inPowderSnow || this.holder.wasInPowderSnow;
            return f > 0.5F
              && !bl
              && this.holder.getWorld().isSkyVisible(blockPos)
              && VampireSunEvents.CAN_BURN.invoker().canBurn(this.holder.getWorld(), this.holder, this);
        }
        return false;
    }

    public int getMaxTimeInSun() {
        // combine sun resistance values and then convert from seconds to ticks
        int time = (int) ((this.holder.getAttributeValue(SLEntityAttributes.SUN_RESISTANCE) + SunProtectionEnchantment.calculateForEntity(this.holder)) * 20.d);
        return VampireSunEvents.MODIFY_SUN_TIME.invoker().getMaxTimeInSun(this.holder, this, time);
    }

    public int getTimeInSun() {
        return this.sunTicks;
    }

    private enum SyncFlags {
        STATE,
        SUN,
        ABILITIES,
    }
}
