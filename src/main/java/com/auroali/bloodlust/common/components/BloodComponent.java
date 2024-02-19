package com.auroali.bloodlust.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface BloodComponent extends Component {
    int getBlood();
    int getMaxBlood();
    int addBlood(int amount);
    boolean drainBlood();
    boolean hasBlood();
}
