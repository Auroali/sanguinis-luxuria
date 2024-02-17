package com.auroali.bloodlust.common.components;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface BloodComponent extends Component {
    int getBlood();
    int getMaxBlood();
    boolean drainBlood();
    boolean hasBlood();
}
