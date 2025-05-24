package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.registry.SLRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SLVampireAbiltyTagsProvider extends FabricTagProvider<VampireAbility> {
    public SLVampireAbiltyTagsProvider(FabricDataOutput dataGenerator, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataGenerator, SLRegistryKeys.VAMPIRE_ABILITIES, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup args) {
    }
}
