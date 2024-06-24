package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class BLRegistryKeys {
    public static final RegistryKey<Registry<VampireAbility>> VAMPIRE_ABILITIES_KEY = RegistryKey.ofRegistry(BLResources.VAMPIRE_ABILITY_REGISTRY_ID);
}
