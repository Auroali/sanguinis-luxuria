package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainIgniteEffect;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainStatusEffect;
import com.auroali.sanguinisluxuria.common.blood.effects.BloodDrainTeleportEffect;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import com.auroali.sanguinisluxuria.datagen.builders.BloodDrainEffectBuilder;
import com.auroali.sanguinisluxuria.datagen.generators.SanguinisLuxuriaBloodEffectsProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.resource.conditions.v1.DefaultResourceConditions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;


public class SLBloodEffectProvider extends SanguinisLuxuriaBloodEffectsProvider {
    public SLBloodEffectProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generateEffects(BloodEffectExporter exporter) {
        exporter.offer(
          BloodDrainEffectBuilder
            .builder(SLTags.Entities.TOXIC_BLOOD)
            .effect(
              new BloodDrainStatusEffect(StatusEffects.HUNGER, 300, 0, 0.9f),
              new BloodDrainStatusEffect(StatusEffects.WEAKNESS, 100, 0, 0.73f)
            ),
          SLResources.id("toxic_blood")
        );

        exporter.offer(
          BloodDrainEffectBuilder
            .builder(SLTags.Entities.TELEPORTS_ON_DRAIN)
            .effect(new BloodDrainTeleportEffect(16, 0.4f)),
          SLResources.id("teleport_on_drain")
        );

        exporter.offer(
          BloodDrainEffectBuilder
            .builder(EntityType.WITCH)
            .effect(
              new BloodDrainStatusEffect(StatusEffects.WEAKNESS, 300, 0, 0.05f),
              new BloodDrainStatusEffect(StatusEffects.SLOWNESS, 300, 0, 0.05f)
            )
        );

        this.generateCompat(exporter);
    }

    private void generateCompat(BloodEffectExporter exporter) {
        this.withConditions(exporter, DefaultResourceConditions.allModsLoaded("spectrum"))
          .offer(
            BloodDrainEffectBuilder.builder(new Identifier("spectrum", "kindling"))
              .effect(new BloodDrainIgniteEffect(ConstantIntProvider.create(8), 0.4f))
          );
        this.withConditions(exporter, DefaultResourceConditions.allModsLoaded("yttr"))
          .offer(BloodDrainEffectBuilder.builder(new Identifier("yttr", "deer"))
            .effect(new BloodDrainStatusEffect(StatusEffects.UNLUCK, 600, 0, 0.9f))
          );
    }
}
