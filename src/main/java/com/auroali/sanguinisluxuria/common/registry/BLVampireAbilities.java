package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.abilities.*;
import com.auroali.sanguinisluxuria.common.abilities.active.BiteAbility;
import com.auroali.sanguinisluxuria.common.abilities.active.MistAbility;
import com.auroali.sanguinisluxuria.common.abilities.active.VampireTeleportAbility;
import com.auroali.sanguinisluxuria.common.abilities.passive.InfectiousAbility;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registry;

import java.util.UUID;

public class BLVampireAbilities {
    public static final VampireAbility TELEPORT = new VampireTeleportAbility()
      .condition(AbilityConditions.lacksAbility(() -> BLVampireAbilities.MIST));
    public static final VampireAbility INFECTIOUS = new InfectiousAbility();
    public static final VampireAbility BITE = new BiteAbility();
    public static final VampireAbility MIST = new MistAbility()
      .condition(AbilityConditions.lacksAbility(() -> BLVampireAbilities.TELEPORT));
    public static final VampireAbility VULNERABILITY = VampireAttributeModifierAbility
      .builder(UUID.fromString("2975c1bd-f882-4e55-abcf-e1d491cd3b91"))
      .addModifier(BLEntityAttributes.SUN_RESISTANCE, -0.75, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
      .addModifier(BLEntityAttributes.VULNERABILITY, 0.6, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
      .addModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2, EntityAttributeModifier.Operation.ADDITION)
      .addModifier(EntityAttributes.GENERIC_ATTACK_SPEED, 0.15, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
      .addModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.03, EntityAttributeModifier.Operation.ADDITION)
      .condition(AbilityConditions.lacksAbility(() -> BLVampireAbilities.RESILIENCE))
      .build();
    public static final VampireAbility RESILIENCE = VampireAttributeModifierAbility
      .builder(UUID.fromString("bbff7218-3b09-4d9e-95a7-91e0daf5f13a"))
      .addModifier(BLEntityAttributes.SUN_RESISTANCE, 0.65, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
      .addModifier(EntityAttributes.GENERIC_MAX_HEALTH, -4, EntityAttributeModifier.Operation.ADDITION)
      .addModifier(EntityAttributes.GENERIC_ATTACK_SPEED, -0.05, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
      .addModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, -0.02, EntityAttributeModifier.Operation.ADDITION)
      .condition(AbilityConditions.lacksAbility(() -> BLVampireAbilities.VULNERABILITY))
      .build();

    public static void register() {
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.TELEPORT_ID, TELEPORT);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.TRANSFER_EFFECTS_ID, INFECTIOUS);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.BITE_ID, BITE);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.MIST_ID, MIST);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.VULNERABILITY_ID, VULNERABILITY);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.RESILIENCE_ID, RESILIENCE);
    }
}
