package com.auroali.bloodlust.components;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.components.impl.EntityBloodComponent;
import com.auroali.bloodlust.components.impl.PlayerBloodComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.LivingEntity;

public class BLEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<BloodComponent> BLOOD_COMPONENT = ComponentRegistry.getOrCreate(BLResources.BLOOD_COMPONENT_ID, BloodComponent.class);
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // we don't need to copy the player component, as it stores no actual data
        registry.registerForPlayers(BLOOD_COMPONENT, PlayerBloodComponent::new, RespawnCopyStrategy.NEVER_COPY);
        registry.registerFor(LivingEntity.class, BLOOD_COMPONENT, EntityBloodComponent::new);

    }
}
