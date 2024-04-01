package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class BLTags {
    public static class Blocks {

        public static final TagKey<Block> BLOOD_SPLATTER_REPLACEABLE = TagKey.of(Registry.BLOCK_KEY, BLResources.BLOOD_SPLATTER_REPLACEABLE);;
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> HAS_BLOOD = TagKey.of(Registry.ENTITY_TYPE_KEY, BLResources.HAS_BLOOD_ID);
        public static final TagKey<EntityType<?>> GOOD_BLOOD = TagKey.of(Registry.ENTITY_TYPE_KEY, BLResources.GOOD_BLOOD_ID);
        public static final TagKey<EntityType<?>> TOXIC_BLOOD = TagKey.of(Registry.ENTITY_TYPE_KEY, BLResources.TOXIC_BLOOD_ID);
        public static final TagKey<EntityType<?>> CAN_DROP_BLOOD = TagKey.of(Registry.ENTITY_TYPE_KEY, BLResources.CAN_DROP_BLOOD);;
        public static final TagKey<EntityType<?>> TELEPORTS_ON_DRAIN = TagKey.of(Registry.ENTITY_TYPE_KEY, BLResources.TELEPORTS_ON_DRAIN_ID);;
    }

    public static class Items {
        public static final TagKey<Item> VAMPIRES_GET_HUNGER_FROM = TagKey.of(Registry.ITEM_KEY, BLResources.VAMPIRES_GET_HUNGER_FROM_ID);
        public static final TagKey<Item> FACE_TRINKETS = TagKey.of(Registry.ITEM_KEY, new Identifier("trinkets", "head/face"));
        public static final TagKey<Item> SUN_BLOCKING_HELMETS = TagKey.of(Registry.ITEM_KEY, BLResources.SUN_BLOCKING_HELMETS);
        public static final TagKey<Item> VAMPIRE_MASKS = TagKey.of(Registry.ITEM_KEY, BLResources.VAMPIRE_MASKS_ID);
    }
    public static class VampireAbilities {
        public static final TagKey<VampireAbility> TELEPORT_RANGE = TagKey.of(BLRegistry.VAMPIRE_ABILITIES_KEY, BLResources.TELEPORT_RANGE_TAG_ID);
    }

    public static class Biomes {
        public static final TagKey<Biome> VAMPIRE_VILLAGER_SPAWN = TagKey.of(Registry.BIOME_KEY, BLResources.VAMPIRE_VILLAGER_SPAWN);
    }
}
