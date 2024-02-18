package com.auroali.bloodlust;

import net.minecraft.util.Identifier;

public class BLResources {
    public static final Identifier HAS_BLOOD_ID = id("has_blood");
    public static final Identifier BLOOD_COMPONENT_ID = id("blood");
    public static final Identifier VAMPIRE_COMPONENT_ID = id("vampire");
    public static final Identifier KEYBIND_CHANNEL = id("bloodlust_keys");
    public static final Identifier ICONS = id("textures/gui/icons.png");
    public static final Identifier VAMPIRE_FOOD_ID = id("vampire_edible");

    // Predicates
    public static final Identifier VARIANT_MODEL_PREDICATE = id("variant");

    public static final Identifier MASK_ONE_ID = id("mask_1");
    public static final Identifier MASK_TWO_ID = id("mask_2");
    public static final Identifier MASK_THREE_ID = id("mask_3");

    public static Identifier id(String path) {
        return new Identifier(Bloodlust.MODID, path);
    }
}
