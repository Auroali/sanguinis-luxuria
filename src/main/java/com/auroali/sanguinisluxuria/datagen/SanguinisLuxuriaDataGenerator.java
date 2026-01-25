package com.auroali.sanguinisluxuria.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class SanguinisLuxuriaDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(SLLangProvider::new);
        pack.addProvider(SLEntityTagsProvider::new);
        pack.addProvider(SLItemTagsProvider::new);
        pack.addProvider(SLModelProvider::new);
        pack.addProvider(SLRecipeProvider::new);
        pack.addProvider(SLBlockTagsProvider::new);
        pack.addProvider(SLBiomeTagsProvider::new);
        pack.addProvider(SLVampireAbiltyTagsProvider::new);
        pack.addProvider(SLAdvancementsProvider::new);
        pack.addProvider(SLBlockLootTableProvider::new);
        pack.addProvider(SLEntityLootTableProvider::new);
        pack.addProvider(SLDamageTypeProvider::new);
        pack.addProvider(SLDamageTagsProvider::new);
        pack.addProvider(SLEnchantmentTagsProvider::new);
        pack.addProvider(SLConversionProvider::new);
        pack.addProvider(SLBloodEffectProvider::new);
        pack.addProvider(SLStatusEffectTags::new);
        pack.addProvider(SLPatchouliBooks::new);
        pack.addProvider(SLWorldgenProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.DAMAGE_TYPE, SLDamageTypeProvider::bootstrap);
    }
}
