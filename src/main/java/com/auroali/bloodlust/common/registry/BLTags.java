package com.auroali.bloodlust.common.registry;

import com.auroali.bloodlust.BLResources;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BLTags {
    public static class Entities {
        public static final TagKey<EntityType<?>> HAS_BLOOD = TagKey.of(Registry.ENTITY_TYPE_KEY, BLResources.HAS_BLOOD_ID);
        public static final TagKey<EntityType<?>> GOOD_BLOOD = TagKey.of(Registry.ENTITY_TYPE_KEY, BLResources.GOOD_BLOOD_ID);
        public static final TagKey<EntityType<?>> TOXIC_BLOOD = TagKey.of(Registry.ENTITY_TYPE_KEY, BLResources.TOXIC_BLOOD_ID);
    }

    public static class Items {
        public static final TagKey<Item> VAMPIRE_EDIBLE = TagKey.of(Registry.ITEM_KEY, BLResources.VAMPIRE_FOOD_ID);
        public static final TagKey<Item> VAMPIRES_GET_HUNGER_FROM = TagKey.of(Registry.ITEM_KEY, BLResources.VAMPIRES_GET_HUNGER_FROM_ID);
        public static final TagKey<Item> FACE_TRINKETS = TagKey.of(Registry.ITEM_KEY, new Identifier("trinkets", "head/face"));
    }
}
