package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.Collections;
import java.util.List;

public class SLTags {
    public static class Blocks {

        //public static final TagKey<Block> BLOOD_SPLATTER_REPLACEABLE = TagKey.of(Registry.BLOCK_KEY, BLResources.BLOOD_SPLATTER_REPLACEABLE);;
        public static final TagKey<Block> DECAYED_LOGS = TagKey.of(RegistryKeys.BLOCK, SLResources.DECAYED_LOGS_ID);
        public static final TagKey<Block> HUNGRY_DECAYED_LOGS = TagKey.of(RegistryKeys.BLOCK, SLResources.HUNGRY_DECAYED_LOGS_ID);
        public static final TagKey<Block> SILVER_BLOCKS = TagKey.of(RegistryKeys.BLOCK, new Identifier("c", "silver_blocks"));
        public static final TagKey<Block> RAW_SILVER_BLOCKS = TagKey.of(RegistryKeys.BLOCK, new Identifier("c", "raw_silver_blocks"));
        public static final TagKey<Block> SILVER_ORES = TagKey.of(RegistryKeys.BLOCK, new Identifier("c", "silver_ores"));
        public static final TagKey<Block> NO_MIST_COLLISION = TagKey.of(RegistryKeys.BLOCK, SLResources.NO_MIST_COLLISION_ID);
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> HAS_BLOOD = TagKey.of(RegistryKeys.ENTITY_TYPE, SLResources.HAS_BLOOD_ID);
        public static final TagKey<EntityType<?>> GOOD_BLOOD = TagKey.of(RegistryKeys.ENTITY_TYPE, SLResources.GOOD_BLOOD_ID);
        public static final TagKey<EntityType<?>> TOXIC_BLOOD = TagKey.of(RegistryKeys.ENTITY_TYPE, SLResources.TOXIC_BLOOD_ID);
        public static final TagKey<EntityType<?>> TELEPORTS_ON_DRAIN = TagKey.of(RegistryKeys.ENTITY_TYPE, SLResources.TELEPORTS_ON_DRAIN_ID);
        public static final TagKey<EntityType<?>> IMMUNE_TO_BLOOD_LOSS = TagKey.of(RegistryKeys.ENTITY_TYPE, SLResources.IMMUNE_TO_BLOOD_LOSS_ID);
    }

    public static class Items {
        public static final TagKey<Item> VAMPIRES_GET_HUNGER_FROM = TagKey.of(RegistryKeys.ITEM, SLResources.VAMPIRES_GET_HUNGER_FROM_ID);
        public static final TagKey<Item> FACE_TRINKETS = TagKey.of(RegistryKeys.ITEM, new Identifier("trinkets", "head/face"));
        public static final TagKey<Item> NECKLACE_TRINKETS = TagKey.of(RegistryKeys.ITEM, new Identifier("trinkets", "chest/necklace"));
        public static final TagKey<Item> SUN_BLOCKING_HELMETS = TagKey.of(RegistryKeys.ITEM, SLResources.SUN_BLOCKING_HELMETS_ID);
        public static final TagKey<Item> VAMPIRE_MASKS = TagKey.of(RegistryKeys.ITEM, SLResources.VAMPIRE_MASKS_ID);
        public static final TagKey<Item> SILVER_INGOTS = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "silver_ingots"));
        public static final TagKey<Item> DECAYED_LOGS = TagKey.of(RegistryKeys.ITEM, SLResources.DECAYED_LOGS_ID);
        public static final TagKey<Item> HUNGRY_DECAYED_LOGS = TagKey.of(RegistryKeys.ITEM, SLResources.HUNGRY_DECAYED_LOGS_ID);
        public static final TagKey<Item> SILVER_BLOCKS = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "silver_blocks"));
        public static final TagKey<Item> RAW_SILVER_BLOCKS = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "raw_silver_blocks"));
        public static final TagKey<Item> SILVER_ORES = TagKey.of(RegistryKeys.ITEM, new Identifier("c", "silver_ores"));
        public static final TagKey<Item> BLOOD_STORING_BOTTLES = TagKey.of(RegistryKeys.ITEM, SLResources.BLOOD_STORING_BOTTLES_ID);
    }

    public static class StatusEffects {
        public static final TagKey<StatusEffect> NON_TRANSFERABLE = TagKey.of(RegistryKeys.STATUS_EFFECT, SLResources.NON_TRANSFERABLE_ID);
    }

    public static class DamageTypes {
        public static final TagKey<DamageType> VAMPIRES_WEAK_TO = TagKey.of(RegistryKeys.DAMAGE_TYPE, SLResources.VAMPIRES_WEAK_TO_ID);
    }

    public static class Biomes {
        public static final TagKey<Biome> VAMPIRE_VILLAGER_SPAWN = TagKey.of(RegistryKeys.BIOME, SLResources.VAMPIRE_VILLAGER_SPAWN_ID);
    }

    public static class Enchantments {
        public static final TagKey<Enchantment> VAMPIRE_MERCHANT_OFFERS = TagKey.of(RegistryKeys.ENCHANTMENT, SLResources.VAMPIRE_MERCHANT_OFFERS_ID);
    }


    public static <T> List<T> getAllEntriesInTag(TagKey<T> tag, Registry<T> registry) {
        return registry.getTagCreatingWrapper()
          .getOptional(tag)
          .map(named -> named.stream().map(RegistryEntry::value).toList())
          .orElse(Collections.emptyList());
    }
}
