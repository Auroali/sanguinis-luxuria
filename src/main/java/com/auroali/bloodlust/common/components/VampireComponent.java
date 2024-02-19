package com.auroali.bloodlust.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;

public interface VampireComponent extends Component, AutoSyncedComponent, ServerTickingComponent {
    int BLOOD_TIMER_LENGTH = 25;

    boolean isVampire();
    void setIsVampire(boolean isVampire);
    void drainBloodFrom(LivingEntity entity);

    void tryStartSuckingBlood();

    void stopSuckingBlood();
    int getBloodDrainTimer();
}
