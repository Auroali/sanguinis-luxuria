package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.DamageTypeTags;

import java.util.concurrent.CompletableFuture;

public class SLDamageTagsProvider extends FabricTagProvider<DamageType> {

    public SLDamageTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_INVULNERABILITY)
          .addOptional(SLResources.BLOOD_DRAIN_DAMAGE_KEY);
        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR)
          .addOptional(SLResources.BLESSED_WATER_DAMAGE_KEY);
        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_EFFECTS)
          .addOptional(SLResources.BLESSED_WATER_DAMAGE_KEY);
        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ENCHANTMENTS)
          .addOptional(SLResources.BLESSED_WATER_DAMAGE_KEY);
        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_RESISTANCE)
          .addOptional(SLResources.BLESSED_WATER_DAMAGE_KEY);
        this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR)
          .addOptional(SLResources.BITE_DAMAGE_KEY);
        this.getOrCreateTagBuilder(DamageTypeTags.WITCH_RESISTANT_TO)
          .addOptional(SLResources.BLESSED_WATER_DAMAGE_KEY);
        this.getOrCreateTagBuilder(SLTags.DamageTypes.VAMPIRES_WEAK_TO)
          .forceAddTag(DamageTypeTags.WITCH_RESISTANT_TO)
          .forceAddTag(DamageTypeTags.IS_FIRE)
          .addOptional(SLResources.BLESSED_WATER_DAMAGE_KEY);
    }
}
