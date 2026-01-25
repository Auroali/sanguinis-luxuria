package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class SLDamageTagsProvider extends FabricTagProvider<DamageType> {

    public SLDamageTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_INVULNERABILITY)
          .add(SLResources.BLOOD_DRAIN_DAMAGE_KEY);

        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR)
          .add(
            SLResources.BLOOD_DRAIN_DAMAGE_KEY,
            SLResources.BLESSED_WATER_DAMAGE_KEY,
            SLResources.BITE_DAMAGE_KEY
          );

        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_EFFECTS)
          .add(
            SLResources.BLOOD_DRAIN_DAMAGE_KEY,
            SLResources.BLESSED_WATER_DAMAGE_KEY
          );

        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ENCHANTMENTS)
          .add(
            SLResources.BLOOD_DRAIN_DAMAGE_KEY,
            SLResources.BITE_DAMAGE_KEY,
            SLResources.BLESSED_WATER_DAMAGE_KEY
          );

        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_RESISTANCE)
          .add(
            SLResources.BLOOD_DRAIN_DAMAGE_KEY,
            SLResources.BLESSED_WATER_DAMAGE_KEY
          );

        this.getOrCreateTagBuilder(DamageTypeTags.WITCH_RESISTANT_TO)
          .add(SLResources.BLESSED_WATER_DAMAGE_KEY);

        this.getOrCreateTagBuilder(SLTags.DamageTypes.VAMPIRES_WEAK_TO)
          .forceAddTag(DamageTypeTags.WITCH_RESISTANT_TO)
          .forceAddTag(DamageTypeTags.IS_FIRE)
          .add(SLResources.BLESSED_WATER_DAMAGE_KEY)
          .addOptional(new Identifier("malum", "voodoo"))
          .addOptional(new Identifier("spectrum", "kindling_cough"))
          .addOptional(new Identifier("spectrum", "dragonrot"))
          .addOptional(new Identifier("spectrum", "incandescence"));

        this.getOrCreateTagBuilder(SLTags.DamageTypes.CAN_KILL_VAMPIRES)
          .forceAddTag(DamageTypeTags.BYPASSES_INVULNERABILITY)
          .addTag(SLTags.DamageTypes.VAMPIRES_WEAK_TO)
          .add(
            SLResources.BITE_DAMAGE_KEY,
            SLResources.PIERCING_DAMAGE_KEY
          )
          .addOptional(new Identifier("spectrum", "deadly_poison"))
          .addOptional(new Identifier("spectrum", "midnight_solution"))
          .addOptional(new Identifier("spectrum", "primordial_fire"))
          .addOptional(new Identifier("spectrum", "sleep"))
          .addOptional(new Identifier("hexcasting", "overcast"));
    }
}
