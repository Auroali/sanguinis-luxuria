package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class SLDamageTypeProvider extends FabricDynamicRegistryProvider {
    public SLDamageTypeProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(dataOutput, registriesFuture);
    }

    @Override
    public String getName() {
        return "Damage Types";
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.DAMAGE_TYPE));
    }

    public static void bootstrap(Registerable<DamageType> registerable) {
        registerable.register(SLResources.BITE_DAMAGE_KEY, new DamageType("sanguinisluxuria.bite", 0.3f));
        registerable.register(SLResources.BLESSED_WATER_DAMAGE_KEY, new DamageType("sanguinisluxuria.blessed_water", 0.1f));
        registerable.register(SLResources.PIERCING_DAMAGE_KEY, new DamageType("sanguinisluxuria.blink_piercing", 0.3f));
        registerable.register(SLResources.BLOOD_DRAIN_DAMAGE_KEY, new DamageType("sanguinisluxuria.blood_drain", 0.3f));
    }
}
