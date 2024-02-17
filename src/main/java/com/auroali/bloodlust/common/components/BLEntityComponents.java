package com.auroali.bloodlust.common.components;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.common.components.impl.EntityBloodComponent;
import com.auroali.bloodlust.common.components.impl.PlayerBloodComponent;
import com.auroali.bloodlust.common.components.impl.PlayerVampireComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.LivingEntity;

public class BLEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<BloodComponent> BLOOD_COMPONENT = ComponentRegistry.getOrCreate(BLResources.BLOOD_COMPONENT_ID, BloodComponent.class);
    public static final ComponentKey<VampireComponent> VAMPIRE_COMPONENT = ComponentRegistry.getOrCreate(BLResources.VAMPIRE_COMPONENT_ID, VampireComponent.class);
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // we don't need to copy the player component, as it stores no actual data
        registry.registerForPlayers(BLOOD_COMPONENT, PlayerBloodComponent::new, RespawnCopyStrategy.NEVER_COPY);
        registry.registerForPlayers(VAMPIRE_COMPONENT, PlayerVampireComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.beginRegistration(LivingEntity.class, BLOOD_COMPONENT)
                .impl(EntityBloodComponent.class)
                .end(EntityBloodComponent::new);

    }
}
