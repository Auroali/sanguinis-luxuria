package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.fluid.BloodFluid;
import net.minecraft.util.registry.Registry;

public class BLFluids {
    public static BloodFluid BLOOD_STILL = new BloodFluid.BloodFluidStill();
    public static BloodFluid BLOOD_FLOWING = new BloodFluid.BloodFluidFlowing();

    public static void register() {
        Registry.register(Registry.FLUID, BLResources.BLOOD_STILL, BLOOD_STILL);
        Registry.register(Registry.FLUID, BLResources.BLOOD_FLOWING, BLOOD_FLOWING);
    }
}
