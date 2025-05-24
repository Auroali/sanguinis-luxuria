package com.auroali.sanguinisluxuria.common.components;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.components.impl.EntityBloodComponent;
import com.auroali.sanguinisluxuria.common.components.impl.EntityVampireComponent;
import com.auroali.sanguinisluxuria.common.components.impl.PlayerBloodComponent;
import com.auroali.sanguinisluxuria.common.components.impl.PlayerVampireComponent;
import com.auroali.sanguinisluxuria.common.entities.VampireMerchant;
import com.auroali.sanguinisluxuria.common.entities.VampireVillagerEntity;
import com.auroali.sanguinisluxuria.common.registry.SLVampireAbilities;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.TridentEntity;

public class SLEntityComponents implements EntityComponentInitializer {


    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // we don't need to copy the player component, as it stores no actual data
        registry.registerForPlayers(BloodComponent.KEY, PlayerBloodComponent::new, RespawnCopyStrategy.NEVER_COPY);
        registry.registerForPlayers(VampireComponent.KEY, PlayerVampireComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(BloodDrainComponent.KEY, BloodDrainComponent::new, RespawnCopyStrategy.NEVER_COPY);
        registry.beginRegistration(LivingEntity.class, BloodComponent.KEY)
          .impl(EntityBloodComponent.class)
          .end(EntityBloodComponent::new);
        registry.registerFor(TridentEntity.class, BloodTransferComponent.KEY, BloodTransferComponent::new);
        registry.registerFor(VampireVillagerEntity.class, VampireComponent.KEY, e -> new EntityVampireComponent<>(e, SLVampireAbilities.TELEPORT));
        registry.registerFor(VampireMerchant.class, VampireComponent.KEY, e -> new EntityVampireComponent<>(e, SLVampireAbilities.MIST));
    }
}
