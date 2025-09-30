package com.auroali.sanguinisluxuria.common.rituals;

import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.UUID;

public record ActiveRitualData(Ritual ritual, UUID initiator, UUID target) {
    public static final String RITUAL_KEY = "ritual";
    public static final String INITIATOR_KEY = "initiator";
    public static final String TARGET_KEY = "target";
    public static final int TIME_TO_COMPLETE = 300;

    public static void writeNbt(NbtCompound compound, ActiveRitualData data) {
        if (data == null)
            return;
        Ritual.RITUAL_CODEC.encodeStart(NbtOps.INSTANCE, data.ritual())
          .resultOrPartial(SanguinisLuxuria.LOGGER::error)
          .ifPresent(element -> {
              compound.putUuid(INITIATOR_KEY, data.initiator);
              compound.putUuid(TARGET_KEY, data.target);
              compound.put(RITUAL_KEY, element);
          });
    }

    public static ActiveRitualData readNbt(NbtCompound compound) {
        if (!compound.contains(RITUAL_KEY))
            return null;
        return Ritual.RITUAL_CODEC.parse(NbtOps.INSTANCE, compound.get(RITUAL_KEY))
          .resultOrPartial(SanguinisLuxuria.LOGGER::error)
          .map(ritual -> {
              UUID initiator = compound.getUuid(INITIATOR_KEY);
              UUID target = compound.getUuid(TARGET_KEY);
              return new ActiveRitualData(ritual, initiator, target);
          })
          .orElse(null);
    }

    public LivingEntity resolveTarget(World world) {
        if (this.target != null && world instanceof ServerWorld serverWorld) {
            Entity entity = serverWorld.getEntity(this.target);
            if (entity instanceof LivingEntity living)
                return living;
        }
        return null;
    }

    public LivingEntity resolveInitiator(World world) {
        if (this.initiator != null && world instanceof ServerWorld serverWorld) {
            Entity entity = serverWorld.getEntity(this.initiator);
            if (entity instanceof LivingEntity living)
                return living;
        }
        return null;
    }
}
