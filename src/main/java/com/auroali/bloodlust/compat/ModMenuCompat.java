package com.auroali.bloodlust.compat;

import com.auroali.bloodlust.config.BLConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return BLConfig.INSTANCE::generateScreen;
    }
}
