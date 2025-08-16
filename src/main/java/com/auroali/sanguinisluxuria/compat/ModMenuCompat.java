package com.auroali.sanguinisluxuria.compat;

import com.auroali.sanguinisluxuria.config.SLConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SLConfig.INSTANCE::generateScreen;
    }
}
