package com.auroali.bloodlust.common.registry;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.common.abilities.VampireAbility;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

public class BLRegistry {
    public static final RegistryKey<Registry<VampireAbility>> VAMPIRE_ABILITIES_KEY = RegistryKey.ofRegistry(BLResources.VAMPIRE_ABILITY_REGISTRY_ID);
    public static final Registry<VampireAbility> VAMPIRE_ABILITIES = FabricRegistryBuilder.from(
            new SimpleRegistry<>(
                    VAMPIRE_ABILITIES_KEY,
                    Lifecycle.stable(),
                    VampireAbility::getRegistryEntry
            )
    ).buildAndRegister();

    // called to cause the class the load
    @SuppressWarnings("unused")
    public static void init() {}
}
