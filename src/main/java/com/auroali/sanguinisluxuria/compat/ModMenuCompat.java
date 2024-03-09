package com.auroali.sanguinisluxuria.compat;

import com.auroali.sanguinisluxuria.config.BLConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return BLConfig.INSTANCE::generateScreen;
    }
}
