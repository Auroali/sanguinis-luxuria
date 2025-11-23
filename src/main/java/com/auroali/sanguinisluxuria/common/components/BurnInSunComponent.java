package com.auroali.sanguinisluxuria.common.components;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.enchantments.SunProtectionEnchantment;
import com.auroali.sanguinisluxuria.common.events.VampireSunEvents;
import com.auroali.sanguinisluxuria.common.registry.SLEntityAttributes;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class BurnInSunComponent implements Component, ServerTickingComponent, AutoSyncedComponent {
    public static final ComponentKey<BurnInSunComponent> KEY = ComponentRegistry.getOrCreate(SLResources.BURN_IN_SUN_COMPONENT_ID, BurnInSunComponent.class);
    private final LivingEntity holder;
    private int ticksInSun;

    public BurnInSunComponent(LivingEntity holder) {
        this.holder = holder;
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
              && VampireSunEvents.CAN_BURN.invoker().canBurn(this.holder.getWorld(), this.holder, VampireComponent.KEY.get(this.holder));
        }
        return false;
    }

    public int getMaxTicksInSun() {
        // combine sun resistance values and then convert from seconds to ticks
        int time = (int) ((this.holder.getAttributeValue(SLEntityAttributes.SUN_RESISTANCE) + SunProtectionEnchantment.calculateForEntity(this.holder)) * 20.d);
        return VampireSunEvents.MODIFY_SUN_TIME.invoker().getMaxTimeInSun(this.holder, VampireComponent.KEY.get(this.holder), time);
    }

    public int getTicksInSun() {
        return this.ticksInSun;
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player == this.holder;
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeVarInt(this.ticksInSun);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.ticksInSun = buf.readVarInt();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.ticksInSun = tag.getInt("ticksInSun");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("ticksInSun", this.ticksInSun);
    }


    @Override
    public void serverTick() {
        if (!VampireComponent.KEY.isProvidedBy(this.holder))
            return;

        if (!this.isAffectedByDaylight()) {
            if (this.ticksInSun != 0) {
                this.ticksInSun = 0;
                KEY.sync(this.holder);
            }
            return;
        }

        this.holder.addStatusEffect(new StatusEffectInstance(
          StatusEffects.WEAKNESS,
          10,
          0,
          true,
          true
        ));

        int maxTimeInSun = this.getMaxTicksInSun();
        if (this.ticksInSun < maxTimeInSun) {
            this.ticksInSun++;
            KEY.sync(this.holder);
            return;
        }

        this.holder.setOnFireFor(6);
    }
}
