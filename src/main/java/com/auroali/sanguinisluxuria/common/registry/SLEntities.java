package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.entities.VampireMerchant;
import com.auroali.sanguinisluxuria.common.entities.VampireIllagerEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.Heightmap;

public class SLEntities {
    public static final EntityType<VampireIllagerEntity> VAMPIRE_VILLAGER = EntityType.Builder
      .create(VampireIllagerEntity::new, SpawnGroup.MONSTER)
      .maxTrackingRange(10)
      .setDimensions(0.6F, 1.95F)
      .build(SLResources.VAMPIRE_ILLAGER.toString());
    public static final EntityType<VampireMerchant> VAMPIRE_MERCHANT = EntityType.Builder
      .create(VampireMerchant::new, SpawnGroup.MONSTER)
      .maxTrackingRange(10)
      .setDimensions(0.6F, 1.95F)
      .build(SLResources.VAMPIRE_MERCHANT.toString());


    public static void register() {
        Registry.register(Registries.ENTITY_TYPE, SLResources.VAMPIRE_ILLAGER, VAMPIRE_VILLAGER);
        Registry.register(Registries.ENTITY_TYPE, SLResources.VAMPIRE_MERCHANT, VAMPIRE_MERCHANT);

        registerAttributes();
        registerSpawns();
    }

    public static void registerAttributes() {
        FabricDefaultAttributeRegistry
          .register(VAMPIRE_VILLAGER, VampireIllagerEntity.createHostileAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 30)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7)
            .add(EntityAttributes.GENERIC_ARMOR, 2)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.52)
          );
        FabricDefaultAttributeRegistry
          .register(VAMPIRE_MERCHANT, MobEntity.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 25)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3)
            .add(EntityAttributes.GENERIC_ARMOR, 2)
          );
    }

    public static void registerSpawns() {
        SpawnRestriction.register(VAMPIRE_VILLAGER, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        BiomeModifications.addSpawn(b -> b.getBiomeRegistryEntry().isIn(SLTags.Biomes.VAMPIRE_VILLAGER_SPAWN), SpawnGroup.MONSTER, VAMPIRE_VILLAGER, 13, 1, 1);
    }
}
