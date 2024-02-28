package com.auroali.bloodlust;

import net.minecraft.util.Identifier;

public class BLResources {
    public static final Identifier HAS_BLOOD_ID = id("has_blood");
    public static final Identifier GOOD_BLOOD_ID = id("good_blood");
    public static final Identifier TOXIC_BLOOD_ID = id("toxic_blood");
    public static final Identifier BLOOD_COMPONENT_ID = id("blood");
    public static final Identifier VAMPIRE_COMPONENT_ID = id("vampire");
    public static final Identifier KEYBIND_CHANNEL = id("bloodlust_keys");
    public static final Identifier ICONS = id("textures/gui/icons.png");
    public static final Identifier VAMPIRE_FOOD_ID = id("vampire_edible");
    public static final Identifier MASK_ONE_ID = id("mask_1");
    public static final Identifier MASK_TWO_ID = id("mask_2");
    public static final Identifier MASK_THREE_ID = id("mask_3");
    public static final Identifier BLOOD_BAG_ID = id("blood_bag");
    public static final Identifier BLOOD_DRAIN_SOUND = id("drain_blood");
    public static final Identifier BLOOD_STORAGE_ITEM_MODEL_PREDICATE = id("blood_storage_item_fill");
    public static final Identifier VAMPIRES_GET_HUNGER_FROM_ID = id("vampires_get_hunger_from");
    public static final Identifier SUN_BLOCKING_HELMETS = id("sun_blocking_helmets");
    public static final Identifier BLOOD_BOTTLE_ID = id("blood_bottle");
    public static final Identifier ITEM_GROUP_ID = id("bloodlust");
    public static final Identifier BLOOD_SICKNESS_ID = id("blood_sickness");
    public static final Identifier BLOOD_SPLATTER_ID = id("blood_splatter");
    public static final Identifier CAN_DROP_BLOOD = id("can_drop_blood");
    public static final Identifier BLOOD_SPLATTER_REPLACEABLE = id("blood_splatter_replaceable");
    public static final Identifier BLESSED_WATER_ID = id("blessed_water");
    public static final Identifier BLOOD_PROTECTION_ID = id("blessed_blood");
    public static final Identifier VAMPIRE_MASKS_ID = id("vampire_masks");
    public static final Identifier VAMPIRE_ABILITY_REGISTRY_ID = id("vampire_ability");
    public static final Identifier VAMPIRE_HEALTH_1_ID = id("health_1");
    public static final Identifier VAMPIRE_ABILITY_ARGUMENT_ID = id("ability_argument");
    public static final Identifier VAMPIRE_HEALTH_2_ID = id("health_2");
    public static final Identifier VAMPIRE_STRENGTH_1_ID = id("strength_1");
    public static final Identifier SKILL_TREE_CHANNEL = id("skill_trees");
    public static final Identifier ABILITIES_SCREEN = id("textures/gui/abilities.png");
    public static final Identifier ABILITIES_BG = id("textures/gui/abilities_bg.png");
    public static final Identifier ABILITY_KEY_CHANNEL = id("ability_keys");
    public static final Identifier TELEPORT_ID = id("teleport");
    public static final Identifier TELEPORT_COOLDOWN_1_ID = id("teleport_cooldown_1");
    public static final Identifier TELEPORT_COOLDOWN_2_ID = id("teleport_cooldown_2");
    public static final Identifier TELEPORT_RANGE_1_ID = id("teleport_range_1");
    public static final Identifier TELEPORT_RANGE_2_ID = id("teleport_range_2");
    public static final Identifier TELEPORT_RANGE_TAG_ID = id("teleport_range_increase");
    public static final Identifier VAMPIRE_STRENGTH_2_ID = id("vampire_strength_2");
    public static final Identifier BECOME_VAMPIRE_CRITERION_ID = id("become_vampire");
    // todo: give this a better name
    public static final Identifier SKILL_UPGRADER_ID = id("skill_upgrader");

    public static Identifier id(String path) {
        return new Identifier(Bloodlust.MODID, path);
    }
}
