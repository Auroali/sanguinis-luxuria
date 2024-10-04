package com.auroali.sanguinisluxuria.common.events;

import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public interface AllowVampireChangeEvent {
    Event<AllowVampireChangeEvent> EVENT = EventFactory.createArrayBacked(AllowVampireChangeEvent.class, callbacks -> (entity, vampire, isVampire) -> {
        for (AllowVampireChangeEvent event : callbacks) {
            if (!event.onChanged(entity, vampire, isVampire))
                return false;
        }
        return true;
    });

    boolean onChanged(LivingEntity entity, VampireComponent vampire, boolean isVampire);
}
