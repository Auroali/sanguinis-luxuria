package com.auroali.bloodlust;

import net.minecraft.util.Identifier;

public class BLResources {
    public static final Identifier HAS_BLOOD_ID = id("has_blood");
    public static final Identifier BLOOD_COMPONENT_ID = id("blood");
    public static final Identifier VAMPIRE_COMPONENT_ID = id("vampire");

    public static Identifier id(String path) {
        return new Identifier(Bloodlust.MODID, path);
    }
}
