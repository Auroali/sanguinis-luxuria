package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class SLEntityAttributes {
    public static final UUID BLESSED_DAMAGE_UUID = UUID.fromString("514cfa77-6424-4ca6-b938-3d0b7b6e0e29");
    public static final EntityAttribute BLESSED_DAMAGE = new ClampedEntityAttribute(transFromId(SLResources.BLESSED_DAMAGE_ID), 0.0d, 0.0d, 2048.d);
    public static final EntityAttribute BLINK_COOLDOWN = new ClampedEntityAttribute(transFromId(SLResources.BLINK_COOLDOWN_ID), 250.0d, 0.0d, 2048.d);
    public static final EntityAttribute BLINK_RANGE = new ClampedEntityAttribute(transFromId(SLResources.BLINK_RANGE_ID), 12, 0, 2048.d);
    public static final EntityAttribute SUN_RESISTANCE = new ClampedEntityAttribute(transFromId(SLResources.SUN_RESISTANCE_ID), 2.d, 0.d, 1024.d).setTracked(true);
    public static final EntityAttribute VULNERABILITY = new ClampedEntityAttribute(transFromId(SLResources.VULNERABILITY_ID), 1.5d, 0.d, 4.d).setTracked(true);

    public static void register() {
        Registry.register(Registries.ATTRIBUTE, SLResources.BLESSED_DAMAGE_ID, BLESSED_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, SLResources.BLINK_COOLDOWN_ID, BLINK_COOLDOWN);
        Registry.register(Registries.ATTRIBUTE, SLResources.BLINK_RANGE_ID, BLINK_RANGE);
        Registry.register(Registries.ATTRIBUTE, SLResources.RESILIENCE_ID, SUN_RESISTANCE);
        Registry.register(Registries.ATTRIBUTE, SLResources.VULNERABILITY_ID, VULNERABILITY);
    }

    public static String transFromId(Identifier id) {
        return "generic." + id.getNamespace() + "." + id.getPath();
    }
}
