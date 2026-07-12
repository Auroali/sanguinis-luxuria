package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainIgniteEffect;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainStatusEffect;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainTeleportEffect;
import net.minecraft.registry.Registry;

public class SLBloodDrainEffects {
    public static void init() {
        Registry.register(SLRegistries.BLOOD_DRAIN_EFFECTS, SLResources.STATUS_EFFECT_ID, BloodDrainStatusEffect.CODEC);
        Registry.register(SLRegistries.BLOOD_DRAIN_EFFECTS, SLResources.TELEPORT_ID, BloodDrainTeleportEffect.CODEC);
        Registry.register(SLRegistries.BLOOD_DRAIN_EFFECTS, SLResources.IGNITE_EFFECT_ID, BloodDrainIgniteEffect.CODEC);
    }
}
