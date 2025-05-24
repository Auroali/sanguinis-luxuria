package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.fluid.BloodFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class SLFluids {
    public static final BloodFluid BLOOD = new BloodFluid();
    public static final BloodFluid.AttributeHandler BLOOD_ATTRIBUTE_HANDLER = new BloodFluid.AttributeHandler();

    public static void register() {
        Registry.register(Registries.FLUID, SLResources.BLOOD_STILL, BLOOD);
    }
}
