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
import net.minecraft.registry.Registry;

public class BLVampireAbilities {
    public static final VampireAbility HEALTH_1 = VampireAttributeModifierAbility
            .builder(() -> PotionUtil.setPotion( new ItemStack(Items.POTION), Potions.HEALING))
            .addModifier(EntityAttributes.GENERIC_MAX_HEALTH, "0970971f-a4e1-41cf-8566-72686979a161", 2, EntityAttributeModifier.Operation.ADDITION, 4)
            .build();

    public static final VampireAbility HEALTH_2 = VampireAttributeModifierAbility
            .builder(() -> PotionUtil.setPotion( new ItemStack(Items.POTION), Potions.HEALING), HEALTH_1)
            .addModifier(EntityAttributes.GENERIC_MAX_HEALTH, "a3b13d9b-fc8d-4a02-881b-134c04b41f65", 2, EntityAttributeModifier.Operation.ADDITION, 4)
            .build();

    public static final VampireAbility VAMPIRE_STRENGTH_1 = VampireAttributeModifierAbility
            .builder(() -> PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH))
            .addModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "f9efedb8-499f-4738-8414-748348b052f2", 1, EntityAttributeModifier.Operation.ADDITION, 6)
            .build();

    public static final VampireAbility VAMPIRE_STRENGTH_2 = VampireAttributeModifierAbility
            .builder(() -> PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH), VAMPIRE_STRENGTH_1)
            .addModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "33682391-db06-4c6e-8674-770fa4051870", 1, EntityAttributeModifier.Operation.ADDITION, 6)
            .build();

    public static final VampireTeleportAbility TELEPORT = new VampireTeleportAbility(
            () -> new ItemStack(Items.ENDER_PEARL),
            null
    );

    public static final VampireAbility TELEPORT_RANGE_1 = VampireAttributeModifierAbility
            .builder(() -> new ItemStack(Items.ENDER_PEARL), TELEPORT)
            .addModifier(BLEntityAttributes.BLINK_RANGE, "ba1d25c8-5d2f-4cce-852e-684d5c8a09ac", 4, EntityAttributeModifier.Operation.ADDITION)
            .build()
            .incompatible(() -> BLVampireAbilities.TELEPORT_COOLDOWN_1);

    public static final VampireAbility TELEPORT_RANGE_2 = VampireAttributeModifierAbility
            .builder(() -> new ItemStack(Items.ENDER_PEARL), TELEPORT_RANGE_1)
            .addModifier(BLEntityAttributes.BLINK_RANGE, "e31eaabb-1ea1-4329-988e-3bf1f50e96e1", 4, EntityAttributeModifier.Operation.ADDITION)
            .build()
            .incompatible(() -> BLVampireAbilities.TELEPORT_COOLDOWN_1);
    public static final VampireAbility TELEPORT_COOLDOWN_1 = VampireAttributeModifierAbility
            .builder(() -> new ItemStack(Items.ENDER_PEARL), TELEPORT)
            .addModifier(BLEntityAttributes.BLINK_COOLDOWN, "d4158125-d88c-4473-9e70-006f36cab7f9", -75, EntityAttributeModifier.Operation.ADDITION)
            .build()
            .incompatible(() -> BLVampireAbilities.TELEPORT_RANGE_1);

    public static final VampireAbility TELEPORT_COOLDOWN_2 = VampireAttributeModifierAbility
            .builder(() -> new ItemStack(Items.ENDER_PEARL), TELEPORT_COOLDOWN_1)
            .addModifier(BLEntityAttributes.BLINK_COOLDOWN, "50378ac0-be25-4a7a-985b-f51f1c78e1ae", -75, EntityAttributeModifier.Operation.ADDITION)
            .build()
            .incompatible(() -> BLVampireAbilities.TELEPORT_RANGE_1);

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
            () -> new ItemStack(Items.DIAMOND_CHESTPLATE),
            DAMAGE_REDUCTION
    ).condition((entity, vampire, container) -> false);

    public static void register() {
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.VAMPIRE_HEALTH_1_ID, HEALTH_1);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.VAMPIRE_HEALTH_2_ID, HEALTH_2);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.VAMPIRE_STRENGTH_1_ID, VAMPIRE_STRENGTH_1);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.VAMPIRE_STRENGTH_2_ID, VAMPIRE_STRENGTH_2);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.TELEPORT_ID, TELEPORT);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.TELEPORT_RANGE_1_ID, TELEPORT_RANGE_1);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.TELEPORT_RANGE_2_ID, TELEPORT_RANGE_2);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.TELEPORT_COOLDOWN_1_ID, TELEPORT_COOLDOWN_1);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.TELEPORT_COOLDOWN_2_ID, TELEPORT_COOLDOWN_2);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.MORE_BLOOD_ID, MORE_BLOOD);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.TRANSFER_EFFECTS_ID, TRANSFER_EFFECTS);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.BITE_ID, BITE);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.SUN_PROTECTION_ID, SUN_PROTECTION);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.DAMAGE_REDUCTION_ID, DAMAGE_REDUCTION);
        Registry.register(BLRegistries.VAMPIRE_ABILITIES, BLResources.DOWNED_RESISTANCE_ID, DOWNED_RESISTANCE);
    }
}
