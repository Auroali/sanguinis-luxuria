package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class BLEntityAttributes {
    public static final UUID BLESSED_DAMAGE_UUID = UUID.fromString("514cfa77-6424-4ca6-b938-3d0b7b6e0e29");
    public static final EntityAttribute BLESSED_DAMAGE = new ClampedEntityAttribute(transFromId(BLResources.BLESSED_DAMAGE_ID), 0.0f, 0.0, 2048);

    public static void register() {
        Registry.register(Registry.ATTRIBUTE, BLResources.BLESSED_DAMAGE_ID, BLESSED_DAMAGE);

    }

    public static String transFromId(Identifier id) {
        return "generic." + id.getNamespace() + "." + id.getPath();
    }
}
