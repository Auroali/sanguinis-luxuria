package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainIgniteEffect;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainStatusEffect;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainTeleportEffect;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import com.auroali.sanguinisluxuria.datagen.builders.BloodDrainEffectBuilder;
import com.auroali.sanguinisluxuria.datagen.generators.SanguinisLuxuriaBloodEffectsProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class BLBloodEffectProvider extends SanguinisLuxuriaBloodEffectsProvider {
    public BLBloodEffectProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generateEffects(Consumer<BloodDrainEffectBuilder.Provider> exporter) {
        BloodDrainEffectBuilder.create(BLTags.Entities.TOXIC_BLOOD)
          .statusEffect(StatusEffects.HUNGER, 0.9f)
          .statusEffect(StatusEffects.WEAKNESS, 100, 0.73f)
          .offerTo(exporter, BLResources.id("toxic_blood"));

        BloodDrainEffectBuilder.create(BLTags.Entities.TELEPORTS_ON_DRAIN)
          .effect(new BloodDrainTeleportEffect(16, 0.8f))
          .offerTo(exporter, BLResources.id("teleport_on_drain"));

        BloodDrainEffectBuilder.createOptional(new Identifier("spectrum", "kindling"))
          .effect(new BloodDrainIgniteEffect(8, 0.4f))
          .offerTo(exporter, BLResources.id("compat/spectrum/kindling_effects"));
    }
}
