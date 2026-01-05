package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.datagen.generators.SanguinusLuxuriaRegistryCodecProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.entity.damage.DamageEffects;
import net.minecraft.entity.damage.DamageScaling;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DeathMessageType;
import net.minecraft.registry.RegistryKeys;

public class SLDamageTypeProvider extends SanguinusLuxuriaRegistryCodecProvider<DamageType> {
    public SLDamageTypeProvider(FabricDataOutput dataOutput) {
        super(dataOutput, RegistryKeys.DAMAGE_TYPE, DamageType.CODEC);
    }

    @Override
    protected void generate(Exporter<DamageType> exporter) {
        exporter.export(
          this.builder("sanguinisluxuria.bite")
            .exhaustion(0.3f)
            .build(),
          SLResources.BITE_DAMAGE_KEY
        );

        exporter.export(
          this.builder("sanguinisluxuria.blessed_water")
            .exhaustion(0.1f)
            .build(),
          SLResources.BLESSED_WATER_DAMAGE_KEY
        );

        exporter.export(
          this.builder("sanguinisluxuria.blink_piercing")
            .exhaustion(0.1f)
            .build(),
          SLResources.PIERCING_DAMAGE_KEY
        );

        exporter.export(
          this.builder("sanguinisluxuria.blood_drain")
            .exhaustion(0.1f)
            .build(),
          SLResources.BLOOD_DRAIN_DAMAGE_KEY
        );
    }

    private DamageTypeBuilder builder(String msgId) {
        return new DamageTypeBuilder(msgId);
    }

    private static class DamageTypeBuilder {
        private final String msgId;
        private DamageScaling scaling;
        private float exhaustion;
        private DamageEffects effects;
        private DeathMessageType deathMessageType;

        public DamageTypeBuilder(String msgId) {
            this.msgId = msgId;
            this.scaling = DamageScaling.NEVER;
            this.exhaustion = 0.f;
            this.effects = DamageEffects.HURT;
            this.deathMessageType = DeathMessageType.DEFAULT;
        }

        public DamageTypeBuilder scaling(DamageScaling scaling) {
            this.scaling = scaling;
            return this;
        }

        public DamageTypeBuilder exhaustion(float exhaustion) {
            this.exhaustion = exhaustion;
            return this;
        }

        public DamageTypeBuilder effects(DamageEffects effects) {
            this.effects = effects;
            return this;
        }

        public DamageTypeBuilder deathMessageType(DeathMessageType deathMessageType) {
            this.deathMessageType = deathMessageType;
            return this;
        }

        public DamageType build() {
            return new DamageType(
              this.msgId,
              this.scaling,
              this.exhaustion,
              this.effects,
              this.deathMessageType
            );
        }
    }
}
