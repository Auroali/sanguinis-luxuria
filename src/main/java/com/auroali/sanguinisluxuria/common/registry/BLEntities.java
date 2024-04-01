package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.entities.VampireVillagerEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.registry.Registry;

public class BLEntities {
    public static final EntityType<VampireVillagerEntity> VAMPIRE_VILLAGER = EntityType.Builder
            .create(VampireVillagerEntity::new, SpawnGroup.MONSTER)
            .maxTrackingRange(10)
            .setDimensions(0.6F, 1.95F)
            .build(BLResources.VAMPIRE_VILLAGER.toString());

    public static void register() {
        Registry.register(Registry.ENTITY_TYPE, BLResources.VAMPIRE_VILLAGER, VAMPIRE_VILLAGER);

        registerAttributes();
        registerSpawns();
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry
                .register(VAMPIRE_VILLAGER, VampireVillagerEntity.createMobAttributes()
                        .add(EntityAttributes.GENERIC_MAX_HEALTH, 30)
                        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3)
                        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.52)
                );
    }

    public static void registerSpawns() {
        BiomeModifications.addSpawn(b -> b.getBiomeRegistryEntry().isIn(BLTags.Biomes.VAMPIRE_VILLAGER_SPAWN), SpawnGroup.MONSTER, VAMPIRE_VILLAGER, 3, 1, 1);
    }
}
