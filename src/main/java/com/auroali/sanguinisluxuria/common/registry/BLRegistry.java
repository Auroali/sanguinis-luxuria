package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;

public class BLRegistry {
    public static final RegistryKey<Registry<VampireAbility>> VAMPIRE_ABILITIES_KEY = RegistryKey.ofRegistry(BLResources.VAMPIRE_ABILITY_REGISTRY_ID);
    public static final Registry<VampireAbility> VAMPIRE_ABILITIES = FabricRegistryBuilder.from(
            new SimpleRegistry<>(
                    VAMPIRE_ABILITIES_KEY,
                    Lifecycle.stable(),
                    true
            )
    ).buildAndRegister();

    // called to cause the class the load
    @SuppressWarnings("unused")
    public static void init() {
    }
}
