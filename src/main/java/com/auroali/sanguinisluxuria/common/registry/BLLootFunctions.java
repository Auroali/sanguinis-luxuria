package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.loot.SetBloodLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BLLootFunctions {
    public static final LootFunctionType SET_BLOOD = new LootFunctionType(new SetBloodLootFunction.Serializer());

    public static void register() {
        Registry.register(Registries.LOOT_FUNCTION_TYPE, BLResources.SET_BLOOD_ID, SET_BLOOD);
    }
}
