package com.auroali.sanguinisluxuria.common.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public interface AllowBloodDrainEvent {
    Event<AllowBloodDrainEvent> EVENT = EventFactory.createArrayBacked(AllowBloodDrainEvent.class, callbacks -> (entity, target) -> {
        for(AllowBloodDrainEvent event : callbacks) {
            if(!event.allowBloodDrain(entity, target))
                return false;
        }
        return true;
    });
    boolean allowBloodDrain(LivingEntity entity, LivingEntity target);
}
