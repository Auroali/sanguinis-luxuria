package com.auroali.sanguinisluxuria.common.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public class BloodEvents {
    public static final Event<AllowBloodDrainEvent> ALLOW_BLOOD_DRAIN = EventFactory.createArrayBacked(AllowBloodDrainEvent.class, callbacks -> (entity, target) -> {
        for(AllowBloodDrainEvent event : callbacks) {
            if(!event.allowBloodDrain(entity, target))
                return false;
        }
        return true;
    });

    public static final Event<BloodDrainEvent> BLOOD_DRAINED = EventFactory.createArrayBacked(BloodDrainEvent.class, callbacks -> (entity, target, amount) -> {
        for(BloodDrainEvent event : callbacks) {
            event.onBloodDrained(entity, target, amount);
        }
    });

    @FunctionalInterface
    public interface BloodDrainEvent {
        void onBloodDrained(LivingEntity entity, LivingEntity target, int amount);
    }
    @FunctionalInterface
    public interface AllowBloodDrainEvent {
        boolean allowBloodDrain(LivingEntity entity, LivingEntity target);
    }
}
