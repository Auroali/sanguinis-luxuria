package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.BLTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class BLStatusEffectTags extends FabricTagProvider<StatusEffect> {
    public BLStatusEffectTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.STATUS_EFFECT, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(BLTags.StatusEffects.NON_TRANSFERABLE)
          .addOptional(new Identifier("spectrum", "somnolence"))
          .addOptional(new Identifier("spectrum", "eternal_slumber"))
          .addOptional(new Identifier("spectrum", "fatal_slumber"))
          .addOptional(new Identifier("spectrum", "divinity"));
    }
}
