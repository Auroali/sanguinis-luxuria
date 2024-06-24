package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class BLTags {
    public static class Blocks {

        //public static final TagKey<Block> BLOOD_SPLATTER_REPLACEABLE = TagKey.of(Registry.BLOCK_KEY, BLResources.BLOOD_SPLATTER_REPLACEABLE);;
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> HAS_BLOOD = TagKey.of(RegistryKeys.ENTITY_TYPE, BLResources.HAS_BLOOD_ID);
        public static final TagKey<EntityType<?>> GOOD_BLOOD = TagKey.of(RegistryKeys.ENTITY_TYPE, BLResources.GOOD_BLOOD_ID);
        public static final TagKey<EntityType<?>> TOXIC_BLOOD = TagKey.of(RegistryKeys.ENTITY_TYPE, BLResources.TOXIC_BLOOD_ID);
        public static final TagKey<EntityType<?>> CAN_DROP_BLOOD = TagKey.of(RegistryKeys.ENTITY_TYPE, BLResources.CAN_DROP_BLOOD);;
        public static final TagKey<EntityType<?>> TELEPORTS_ON_DRAIN = TagKey.of(RegistryKeys.ENTITY_TYPE, BLResources.TELEPORTS_ON_DRAIN_ID);;
    }

    public static class Items {
        public static final TagKey<Item> VAMPIRES_GET_HUNGER_FROM = TagKey.of(RegistryKeys.ITEM, BLResources.VAMPIRES_GET_HUNGER_FROM_ID);
        public static final TagKey<Item> FACE_TRINKETS = TagKey.of(RegistryKeys.ITEM, new Identifier("trinkets", "head/face"));
        public static final TagKey<Item> NECKLACE_TRINKETS = TagKey.of(RegistryKeys.ITEM, new Identifier("trinkets", "chest/necklace"));
        public static final TagKey<Item> SUN_BLOCKING_HELMETS = TagKey.of(RegistryKeys.ITEM, BLResources.SUN_BLOCKING_HELMETS);
        public static final TagKey<Item> VAMPIRE_MASKS = TagKey.of(RegistryKeys.ITEM, BLResources.VAMPIRE_MASKS_ID);
        public static final TagKey<Item> SILVER_INGOTS = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "silver_ingots"));
    }
    public static class VampireAbilities {
        public static final TagKey<VampireAbility> TELEPORT_RANGE = TagKey.of(BLRegistries.VAMPIRE_ABILITIES_KEY, BLResources.TELEPORT_RANGE_TAG_ID);
    }

    public static class Biomes {
        public static final TagKey<Biome> VAMPIRE_VILLAGER_SPAWN = TagKey.of(RegistryKeys.BIOME, BLResources.VAMPIRE_VILLAGER_SPAWN);
    }
}
