package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.Bloodlust;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.registry.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;

public class BLLangProvider extends FabricLanguageProvider {
    public BLLangProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("key.sanguinisluxuria.bite", "Bite");
        translationBuilder.add("key.sanguinisluxuria.open_abilities", "Open Abilities");
        translationBuilder.add("key.sanguinisluxuria.ability_1", "Ability 1");
        translationBuilder.add("key.sanguinisluxuria.ability_2", "Ability 2");
        translationBuilder.add("category.sanguinisluxuria.sanguinisluxuria", "Sanguinis Luxuria");

        translationBuilder.add(BLItems.MASK_1, "Carved Mask");
        translationBuilder.add(BLItems.MASK_2, "Carved Mask");
        translationBuilder.add(BLItems.MASK_3, "Carved Mask");
        translationBuilder.add(BLItems.BLOOD_BAG, "Blood Bag");
        translationBuilder.add(BLItems.BLOOD_BOTTLE, "Blood Bottle");

        translationBuilder.add("subtitles.sanguinisluxuria.drain_blood", "Vampire feeding");
        translationBuilder.add("subtitles.sanguinisluxuria.altar_beats", "Altar beats");
        translationBuilder.add("subtitles.sanguinisluxuria.bleeding", "Something bleeds");
        translationBuilder.add("subtitles.sanguinisluxuria.entity_converted_to_vampire", "Something transforms");

        translationBuilder.add("fluids.sanguinisluxuria.blood", "Blood");

        translationBuilder.add("sanguinisluxuria.config.title", "Bloodlust");
        translationBuilder.add("sanguinisluxuria.config.category.gameplay", "Gameplay");
        translationBuilder.add("sanguinisluxuria.config.option.vampire_damage_multiplier", "Damage Multiplier");
        translationBuilder.add("sanguinisluxuria.config.option.vampire_damage_multiplier.desc", "How much damage is multiplied for vampires from damage types they are weak to, such as fire.");
        translationBuilder.add("sanguinisluxuria.config.option.vampire_exhaustion_multiplier", "Exhaustion Multiplier");
        translationBuilder.add("sanguinisluxuria.config.option.vampire_exhaustion_multiplier.desc", "How much exhaustion is multiplied by for vampires");
        translationBuilder.add("sanguinisluxuria.config.option.blessed_water_damage", "Blessed Water Damage");
        translationBuilder.add("sanguinisluxuria.config.option.blessed_water_damage.desc", "The base amount of damage blessed water does against vampires and undead entities");
        translationBuilder.add("sanguinisluxuria.config.category.abilities", "Abilities");
        translationBuilder.add("sanguinisluxuria.config.option.skill_points_per_level", "Skill Points Per Level");
        translationBuilder.add("sanguinisluxuria.config.option.skill_points_per_level.desc", "How many skill points are gained per level");
        translationBuilder.add("sanguinisluxuria.config.category.worldgen", "Worldgen");
        translationBuilder.add("sanguinisluxuria.config.option.generate_silver_ore", "Generate Silver Ore");
        translationBuilder.add("sanguinisluxuria.config.option.generate_silver_ore.desc", "If silver ore should generate naturally in the world");

        translationBuilder.add("death.attack.%s".formatted(BLDamageSources.BLOOD_DRAIN.name), "%1$s had their blood drained");
        translationBuilder.add("death.attack.%s.player".formatted(BLDamageSources.BLOOD_DRAIN.name), "%1$s had their blood drained whilst fighting %2$s");
        translationBuilder.add("death.attack.sanguinisluxuria.blood_drain_by_entity", "%1$s had their blood drained by %2$s");
        translationBuilder.add("death.attack.sanguinisluxuria.blood_drain_by_entity.item", "%1$s had their blood drained by %2$s");

        translationBuilder.add("death.attack.sanguinisluxuria.bite", "%1$s was bit by %2$s");
        translationBuilder.add("death.attack.sanguinisluxuria.bite.item", "%1$s was bit by %2$s");

        translationBuilder.add("death.attack.sanguinisluxuria.teleport", "%1$s was pierced by %2$s");
        translationBuilder.add("death.attack.sanguinisluxuria.teleport.item", "%1$s was pierced by %2$s using %3$s");

        translationBuilder.add("death.attack.sanguinisluxuria.blessed_water", "%1$s was burned by blessed water");
        translationBuilder.add("death.attack.sanguinisluxuria.blessed_water.player", "%1$s was burned by blessed water whilst fighting %2$s");
        translationBuilder.add("death.attack.sanguinisluxuria.blessed_water_entity", "%1$s was burned by %2$s using blessed water");
        translationBuilder.add("death.attack.sanguinisluxuria.blessed_water_entity.item", "%1$s was burned by %2$s using blessed water");


        translationBuilder.add(Bloodlust.BLOODLUST_TAB, "Sanguinis Luxuria");

        translationBuilder.add(BLStatusEffects.BLOOD_SICKNESS, "Blood Sickness");
        translationBuilder.add(BLStatusEffects.BLESSED_WATER, "Blessed Water");
        translationBuilder.add(BLStatusEffects.BLOOD_PROTECTION, "Blessed Blood");
        translationBuilder.add(BLStatusEffects.BLEEDING, "Bleeding");

        translationBuilder.add("argument.sanguinisluxuria.id.invalid", "'%s' is not a valid id!");

        translationBuilder.add("gui.sanguinisluxuria.abilities", "Abilities");
        translationBuilder.add("gui.sanguinisluxuria.skill_points", "Skill Points: %d");

        translationBuilder.add("gui.sanguinisluxuria.abilities.bound", "Bound to [%s]");
        translationBuilder.add("gui.sanguinisluxuria.abilities.bind_prompt", "Right click to bind...");
        translationBuilder.add("gui.sanguinisluxuria.abilities.binding", "Press a key...");

        translationBuilder.add("gui.sanguinisluxuria.abilities.incompatibilites", "Incompatible With:");
        translationBuilder.add("gui.sanguinisluxuria.abilities.incompatibilites_entry", "  - %s");

        translationBuilder.add("gui.sanguinisluxuria.abilities.required_skill_points", "Requires %d skill point(s)");

        translationBuilder.add(BLItems.TWISTED_BLOOD, "Twisted Blood Bottle");
        translationBuilder.add(BLItems.BLESSED_BLOOD, "Blessed Blood Bottle");
        translationBuilder.add(BLBlocks.PEDESTAL, "Pedestal");
        translationBuilder.add(BLBlocks.ALTAR, "Altar");
        translationBuilder.add(BLItems.VAMPIRE_VILLAGER_SPAWN_EGG, "Vampire Villager Spawn Egg");

        translationBuilder.add(BLEntities.VAMPIRE_VILLAGER, "Vampire Villager");

        translationBuilder.add("item.sanguinisluxuria.book", "Sanguinis Luxuria");
        translationBuilder.add("sanguinisluxuria.landing", "A book of vampires.");

        translationBuilder.add(BLEnchantments.SUN_PROTECTION, "Sun Protection");
        translationBuilder.add(BLEnchantments.SUN_PROTECTION.getTranslationKey() + ".desc", "Increases the amount of time a vampire can stay in the sun");

        translationBuilder.add(BLEnchantments.BLOOD_DRAIN, "Blood Transfer");
        translationBuilder.add(BLEnchantments.BLOOD_DRAIN.getTranslationKey() + ".desc", "Allows a trident to latch on and slowly transfer blood from a target to the thrower");

        translationBuilder.add(BLBlocks.BLOOD_CAULDRON, "Blood Cauldron");
        translationBuilder.add(BLBlocks.BLOOD_SPLATTER, "Blood");

        translationBuilder.add(BLItems.PENDANT_OF_PIERCING, "Pendant of Piercing");
        translationBuilder.add(BLItems.PENDANT_OF_TRANSFUSION, "Pendant of Transfusion");

        translationBuilder.add(BLEnchantments.SERRATED, "Serrated");
        translationBuilder.add(BLEnchantments.SERRATED.getTranslationKey() + ".desc", "Has a chance of inflicting bleeding on targets");

        translationBuilder.add(BLEntities.VAMPIRE_MERCHANT, "Vampiric Merchant");

        translationBuilder.add("sanguinisluxuria.config.option.blink_piercing_exhaustion", "Blink Piercing Exhaustion");
        translationBuilder.add("sanguinisluxuria.config.option.blink_piercing_exhaustion.desc", "The amount of exhaustion per entity pierced when using the Pendant of Piercing");

        translationBuilder.add(BLItems.BLOOD_PETAL, "Blood Petal");

        translationBuilder.add(BLItems.SILVER_INGOT, "Silver Ingot");
        translationBuilder.add(BLBlocks.SILVER_BLOCK, "Silver Block");
        translationBuilder.add(BLItems.RAW_SILVER, "Raw Silver");
        translationBuilder.add(BLBlocks.SILVER_ORE, "Silver Ore");
        translationBuilder.add(BLBlocks.DEEPSLATE_SILVER_ORE, "Deepslate Silver Ore");
        translationBuilder.add(BLBlocks.RAW_SILVER_BLOCK, "Block of Raw Silver");
        translationBuilder.add(BLItems.SILVER_SWORD, "Silver Sword");
        translationBuilder.add(BLItems.SILVER_AXE, "Silver Axe");
        translationBuilder.add(BLItems.SILVER_PICKAXE, "Silver Pickaxe");
        translationBuilder.add(BLItems.SILVER_SHOVEL, "Silver Shovel");
        translationBuilder.add(BLItems.SILVER_HOE, "Silver Hoe");

        translationBuilder.add(BLEntityAttributes.BLESSED_DAMAGE.getTranslationKey(), "Blessed Damage");

        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION, Items.POTION, "Potion of Blessed Water");
        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION, Items.SPLASH_POTION, "Splash Potion of Blessed Water");
        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION, Items.LINGERING_POTION, "Lingering Potion of Blessed Water");
        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION, Items.TIPPED_ARROW, "Arrow of Blessed Water");

        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION_TWO, Items.POTION, "Potion of Blessed Water");
        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION_TWO, Items.SPLASH_POTION, "Splash Potion of Blessed Water");
        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION_TWO, Items.LINGERING_POTION, "Lingering Potion of Blessed Water");
        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION_TWO, Items.TIPPED_ARROW, "Arrow of Blessed Water");

        generateAbilityKey(translationBuilder, BLVampireAbilities.HEALTH_1, "Increased Health");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.HEALTH_1, "Increases your maximum health");

        generateAbilityKey(translationBuilder, BLVampireAbilities.HEALTH_2, "Increased Health");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.HEALTH_2, "Increases your maximum health");

        generateAbilityKey(translationBuilder, BLVampireAbilities.VAMPIRE_STRENGTH_1, "Increased Strength");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.VAMPIRE_STRENGTH_1, "Increases your base damage");

        generateAbilityKey(translationBuilder, BLVampireAbilities.VAMPIRE_STRENGTH_2, "Increased Strength");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.VAMPIRE_STRENGTH_2, "Increases your base damage");

        generateAbilityKey(translationBuilder, BLVampireAbilities.TELEPORT, "Blink");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.TELEPORT, "Moves you forward a short distance");

        generateAbilityKey(translationBuilder, BLVampireAbilities.TELEPORT_COOLDOWN_1, "Decreased Blink Cooldown");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.TELEPORT_COOLDOWN_1, "Reduces the cooldown for blink");

        generateAbilityKey(translationBuilder, BLVampireAbilities.TELEPORT_COOLDOWN_2, "Decreased Blink Cooldown");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.TELEPORT_COOLDOWN_2, "Reduces the cooldown for blink");

        generateAbilityKey(translationBuilder, BLVampireAbilities.TELEPORT_RANGE_1, "Increased Blink Range");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.TELEPORT_RANGE_1, "Increases the range of blink");

        generateAbilityKey(translationBuilder, BLVampireAbilities.TELEPORT_RANGE_2, "Increased Blink Range");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.TELEPORT_RANGE_2, "Increases the range of blink");

        generateAbilityKey(translationBuilder, BLVampireAbilities.MORE_BLOOD, "Blood Efficiency");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.MORE_BLOOD, "Increases the amount of blood gained");

        generateAbilityKey(translationBuilder, BLVampireAbilities.TRANSFER_EFFECTS, "Infectious");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.TRANSFER_EFFECTS, "Allows transferring potion effects");

        generateAbilityKey(translationBuilder, BLVampireAbilities.BITE, "Bite");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.BITE, "Deals damage and inflicts bleeding");

        generateAbilityKey(translationBuilder, BLVampireAbilities.SUN_PROTECTION, "Sun Resistance");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.SUN_PROTECTION, "Increases the amount of time you can spend in the sun");

        generateAbilityKey(translationBuilder, BLVampireAbilities.DAMAGE_REDUCTION, "Resistance");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.DAMAGE_REDUCTION, "Reduces damage taken from most sources");

        generateAbilityKey(translationBuilder, BLVampireAbilities.DOWNED_RESISTANCE, "Death Protection");
        generateAbilityDescKey(translationBuilder, BLVampireAbilities.DOWNED_RESISTANCE, "Blocks most damage after being downed");

        generateAdvancements(translationBuilder);
    }

    public static void generateAdvancements(TranslationBuilder translationBuilder) {
        translationBuilder.add(BLAdvancementsProvider.title("become_vampire"), "Bloodlust");
        translationBuilder.add(BLAdvancementsProvider.desc("become_vampire"), "Transform into a vampire after drinking enough blood");

        translationBuilder.add(BLAdvancementsProvider.title("drink_twisted_blood"), "Consumption");
        translationBuilder.add(BLAdvancementsProvider.desc("drink_twisted_blood"), "Drink twisted blood");

        translationBuilder.add(BLAdvancementsProvider.title("blood_sickness"), "Feeling Ill");
        translationBuilder.add(BLAdvancementsProvider.desc("blood_sickness"), "Get blood sickness from drinking blood");

        translationBuilder.add(BLAdvancementsProvider.title("unlock_ability"), "Abilities");
        translationBuilder.add(BLAdvancementsProvider.desc("unlock_ability"), "Allocate a skill point to an ability");

        translationBuilder.add(BLAdvancementsProvider.title("reset_abilities"), "Clean Slate");
        translationBuilder.add(BLAdvancementsProvider.desc("reset_abilities"), "Convert unlocked abilities back into skill points using blessed blood");

        translationBuilder.add(BLAdvancementsProvider.title("transfer_effects"), "Infectious");
        translationBuilder.add(BLAdvancementsProvider.desc("transfer_effects"), "Transfer a potion effect while draining blood");

        translationBuilder.add(BLAdvancementsProvider.title("transfer_more_effects"), "Alchemist");
        translationBuilder.add(BLAdvancementsProvider.desc("transfer_more_effects"), "Transfer 4 potions effects at once");

        translationBuilder.add(BLAdvancementsProvider.title("infect_other"), "It Spreads");
        translationBuilder.add(BLAdvancementsProvider.desc("infect_other"), "Inflict blood sickness on something with Weakness");

        translationBuilder.add(BLAdvancementsProvider.title("unbecome_vampire"), "Humanity");
        translationBuilder.add(BLAdvancementsProvider.desc("unbecome_vampire"), "Become human again after drinking Blessed Water with weakness");
        tags(translationBuilder);
        emiTranslations(translationBuilder);
    }

    private void generateTagTranslation(TranslationBuilder builder, TagKey<?> key, String translation) {
        String transKey = "tag.%s.%s.%s".formatted(key.registry().getValue().getPath(), key.id().getNamespace(), key.id().getPath().replace("/", "."));
        builder.add(transKey, translation);
    }

    private void emiTranslations(TranslationBuilder builder) {
        builder.add("emi.category.sanguinisluxuria.altar", "Altar");
        builder.add("emi.category.sanguinisluxuria.blood_cauldron", "Cauldron Infusing");
    }

    private void tags(TranslationBuilder builder) {
        generateTagTranslation(builder, BLTags.Items.VAMPIRE_MASKS, "Vampire Masks");
        generateTagTranslation(builder, BLTags.Items.SUN_BLOCKING_HELMETS, "Sun Blocking Helmets");
        generateTagTranslation(builder, BLTags.Items.VAMPIRES_GET_HUNGER_FROM, "Vampire Food");
    }

    public static void generatePotionKey(TranslationBuilder builder, Potion potion, Item item, String entry) {
        ItemStack stack = new ItemStack(item);
        PotionUtil.setPotion(stack, potion);
        builder.add(stack.getTranslationKey(), entry);
    }

    public static void generateAbilityKey(TranslationBuilder builder, VampireAbility ability, String entry) {
        builder.add(ability.getTranslationKey(), entry);
    }

    public static void generateAbilityDescKey(TranslationBuilder builder, VampireAbility ability, String entry) {
        builder.add(ability.getDescTranslationKey(), entry);
    }
}
