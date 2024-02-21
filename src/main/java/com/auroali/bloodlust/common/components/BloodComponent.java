package com.auroali.bloodlust.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.LivingEntity;

public interface BloodComponent extends Component {
    int getBlood();
    int getMaxBlood();
    int addBlood(int amount);
    void setBlood(int amount);
    boolean drainBlood(LivingEntity drainer);
    boolean drainBlood();
    boolean hasBlood();
}
