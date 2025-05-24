package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.SLEnchantments;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SLEnchantmentTagsProvider extends FabricTagProvider<Enchantment> {
    public SLEnchantmentTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ENCHANTMENT, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(SLTags.Enchantments.VAMPIRE_MERCHANT_OFFERS)
          .add(
            SLEnchantments.BLOOD_DRAIN,
            SLEnchantments.SERRATED,
            SLEnchantments.SUN_PROTECTION
          );
    }
}
