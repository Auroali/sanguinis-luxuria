package com.auroali.bloodlust.common.registry;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.common.abilities.VampireAbility;
import com.auroali.bloodlust.common.abilities.VampireAttributeModifierAbility;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class BLVampireAbilities {
    public static final VampireAbility HEALTH_1 = new VampireAttributeModifierAbility(
            EntityAttributes.GENERIC_MAX_HEALTH,
            new EntityAttributeModifier(
                    UUID.fromString("0970971f-a4e1-41cf-8566-72686979a161"),
                    "bloodlust.vampire_health_1",
                    2,
                    EntityAttributeModifier.Operation.ADDITION
            )
    );

    public static void register() {
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.VAMPIRE_HEALTH_1_ID, HEALTH_1);
    }
}
