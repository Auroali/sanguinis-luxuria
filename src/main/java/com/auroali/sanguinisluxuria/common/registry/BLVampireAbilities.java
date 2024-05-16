package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.abilities.*;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class BLVampireAbilities {
    public static final VampireAbility HEALTH_1 = new VampireAttributeModifierAbility(
            () -> PotionUtil.setPotion( new ItemStack(Items.POTION), Potions.HEALING),
            null,
            EntityAttributes.GENERIC_MAX_HEALTH,
            new EntityAttributeModifier(
                    UUID.fromString("0970971f-a4e1-41cf-8566-72686979a161"),
                    "sanguinisluxuria.vampire_health",
                    2,
                    EntityAttributeModifier.Operation.ADDITION
            ),
            4
    );

    public static final VampireAbility HEALTH_2 = new VampireAttributeModifierAbility(
            () -> PotionUtil.setPotion( new ItemStack(Items.POTION), Potions.HEALING),
            HEALTH_1,
            EntityAttributes.GENERIC_MAX_HEALTH,
            new EntityAttributeModifier(
                    UUID.fromString("a3b13d9b-fc8d-4a02-881b-134c04b41f65"),
                    "sanguinisluxuria.vampire_health",
                    2,
                    EntityAttributeModifier.Operation.ADDITION
            ),
            4
    );

    public static final VampireAbility VAMPIRE_STRENGTH_1 = new VampireAttributeModifierAbility(
            () -> PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH),
            null,
            EntityAttributes.GENERIC_ATTACK_DAMAGE,
            new EntityAttributeModifier(
                    UUID.fromString("a3b13d9b-fc8d-4a02-881b-134c04b41f65"),
                    "sanguinisluxuria.vampire_strength",
                    1,
                    EntityAttributeModifier.Operation.ADDITION
            ),
            6
    );

    public static final VampireAbility VAMPIRE_STRENGTH_2 = new VampireAttributeModifierAbility(
            () -> PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH),
            VAMPIRE_STRENGTH_1,
            EntityAttributes.GENERIC_ATTACK_DAMAGE,
            new EntityAttributeModifier(
                    UUID.fromString("33682391-db06-4c6e-8674-770fa4051870"),
                    "sanguinisluxuria.vampire_strength",
                    1,
                    EntityAttributeModifier.Operation.ADDITION
            ),
            6
    );

    public static final VampireTeleportAbility TELEPORT = new VampireTeleportAbility(
            () -> new ItemStack(Items.ENDER_PEARL),
            null
    );

    public static final VampireAbility TELEPORT_RANGE_1 = new VampireAbility(() -> new ItemStack(Items.ENDER_PEARL), TELEPORT)
            .incompatible(() -> BLVampireAbilities.TELEPORT_COOLDOWN_1);

    public static final VampireAbility TELEPORT_RANGE_2 = new VampireAbility(() -> new ItemStack(Items.ENDER_PEARL), TELEPORT_RANGE_1);

    public static final VampireAbility TELEPORT_COOLDOWN_1 = new VampireAbility(() -> new ItemStack(Items.ENDER_PEARL), TELEPORT)
            .incompatible(() -> BLVampireAbilities.TELEPORT_RANGE_1);

    public static final VampireAbility TELEPORT_COOLDOWN_2 = new VampireAbility(() -> new ItemStack(Items.ENDER_PEARL), TELEPORT_COOLDOWN_1);

    public static final VampireAbility MORE_BLOOD = new VampireAbility(
            () -> BloodStorageItem.setStoredBlood(new ItemStack(BLItems.BLOOD_BOTTLE), BLItems.BLOOD_BOTTLE.getMaxBlood()),
            HEALTH_1
    ).incompatible(() -> BLVampireAbilities.BITE);

    public static final InfectiousAbility TRANSFER_EFFECTS = new InfectiousAbility(
            () -> PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.POISON),
            HEALTH_2
    );

    public static final VampireAbility BITE = new BiteAbility(
            () -> new ItemStack(Items.BONE),
            VAMPIRE_STRENGTH_2
    ).incompatible(() -> BLVampireAbilities.MORE_BLOOD);

    public static final VampireAbility SUN_PROTECTION = new VampireAbility(
            () -> new ItemStack(Items.LEATHER_HELMET),
            HEALTH_2
    );
    public static final VampireAbility DAMAGE_REDUCTION = new VampireAbility(
            () -> new ItemStack(Items.IRON_CHESTPLATE),
            VAMPIRE_STRENGTH_2
    );

    public static final VampireAbility DOWNED_RESISTANCE = new VampireAbility(
            () -> new ItemStack(Items.SHIELD),
            DAMAGE_REDUCTION
    );

    public static void register() {
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.VAMPIRE_HEALTH_1_ID, HEALTH_1);
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.VAMPIRE_HEALTH_2_ID, HEALTH_2);
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.VAMPIRE_STRENGTH_1_ID, VAMPIRE_STRENGTH_1);
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.VAMPIRE_STRENGTH_2_ID, VAMPIRE_STRENGTH_2);
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.TELEPORT_ID, TELEPORT);
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.TELEPORT_RANGE_1_ID, TELEPORT_RANGE_1);
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.TELEPORT_RANGE_2_ID, TELEPORT_RANGE_2);
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.TELEPORT_COOLDOWN_1_ID, TELEPORT_COOLDOWN_1);
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.TELEPORT_COOLDOWN_2_ID, TELEPORT_COOLDOWN_2);
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.MORE_BLOOD_ID, MORE_BLOOD);
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.TRANSFER_EFFECTS_ID, TRANSFER_EFFECTS);
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.BITE_ID, BITE);
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.SUN_PROTECTION_ID, SUN_PROTECTION);
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.DAMAGE_REDUCTION_ID, DAMAGE_REDUCTION);
        Registry.register(BLRegistry.VAMPIRE_ABILITIES, BLResources.DOWNED_RESISTANCE_ID, DOWNED_RESISTANCE);
    }
}
