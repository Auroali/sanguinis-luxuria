package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainIgniteEffect;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainTeleportEffect;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import com.auroali.sanguinisluxuria.datagen.builders.BloodDrainEffectBuilder;
import com.auroali.sanguinisluxuria.datagen.generators.SanguinisLuxuriaBloodEffectsProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.minecraft.entity.EntityType;
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

        BloodDrainEffectBuilder.create(EntityType.WITCH)
          .statusEffect(StatusEffects.WEAKNESS, 0.025f)
          .statusEffect(StatusEffects.SLOWNESS, 0.025f)
          .offerTo(exporter, BLResources.id("witch"));

        this.generateCompat(exporter);
    }

    private void generateCompat(Consumer<BloodDrainEffectBuilder.Provider> exporter) {
        BloodDrainEffectBuilder.createOptional(new Identifier("spectrum", "kindling"))
          .effect(new BloodDrainIgniteEffect(8, 0.4f))
          .offerTo(withConditions(exporter, DefaultResourceConditions.allModsLoaded("spectrum")), BLResources.id("compat/spectrum/kindling_effects"));

    }
}
