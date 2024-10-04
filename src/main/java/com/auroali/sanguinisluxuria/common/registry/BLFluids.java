package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.fluid.BloodFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BLFluids {
    public static final BloodFluid BLOOD = new BloodFluid();
    public static final BloodFluid.AttributeHandler BLOOD_ATTRIBUTE_HANDLER = new BloodFluid.AttributeHandler();

    public static void register() {
        Registry.register(Registries.FLUID, BLResources.BLOOD_STILL, BLOOD);
    }
}
