package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.registry.*;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.auroali.sanguinisluxuria.datagen.util.GenericTranslationBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class SLLangProvider extends FabricLanguageProvider {
    public SLLangProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(SLItemGroups.SANGUINIS_LUXURIA_TAB, "Sanguinis Luxuria");
        translationBuilder.add("fluids.sanguinisluxuria.blood", "Blood");
        translationBuilder.add("argument.sanguinisluxuria.id.invalid", "'%s' is not a valid id!");
        translationBuilder.add("commands.sanguinisluxuria.convert.invalid_conversion", "Invalid conversion type \"%s\"");
        translationBuilder.add("commands.sanguinisluxuria.convert.failed", "Failed to convert %s");
        translationBuilder.add("commands.sanguinisluxuria.ability.failed_conditions", "Failed to add %s to %s");
        translationBuilder.add("options.sanguinisluxuria.use_hunger_effects", "Low Hunger Effects");
        translationBuilder.add("options.sanguinisluxuria.use_hunger_effects.tooltip", "Toggles the use of the low hunger shader for vampires");

        tags(new GenericTranslationBuilder.TagTranslationBuilder(translationBuilder));
        subtitles(translationBuilder);
        keybindings(translationBuilder);
        config(translationBuilder);
        statusEffects(new GenericTranslationBuilder.DescriptionTranslationBuilder<>(translationBuilder, StatusEffect::getTranslationKey));
        gui(translationBuilder);
        entities(translationBuilder);
        enchantments(new GenericTranslationBuilder.DescriptionTranslationBuilder<>(translationBuilder, Enchantment::getTranslationKey));
        items(translationBuilder);
        blocks(translationBuilder);
        attributes(translationBuilder);
        potions(new GenericTranslationBuilder.Potions(translationBuilder, "Potion of ", "Splash Potion of ", "Lingering Potion of ", "Arrow of "));
        abilities(new GenericTranslationBuilder.RegistryTranslationBuilder<>(translationBuilder, SLRegistries.VAMPIRE_ABILITIES));
        deathMessages(
          new GenericTranslationBuilder<>(translationBuilder, key -> "death.attack.%s.%s" .formatted(key.getValue().getNamespace(), key.getValue().getPath().replace('/', '.'))),
          new GenericTranslationBuilder<>(translationBuilder, key -> "death.attack.%s.%s.item" .formatted(key.getValue().getNamespace(), key.getValue().getPath().replace('/', '.'))),
          new GenericTranslationBuilder<>(translationBuilder, key -> "death.attack.%s.%s.player" .formatted(key.getValue().getNamespace(), key.getValue().getPath().replace('/', '.')))
        );
        advancements(new GenericTranslationBuilder.DescriptionTranslationBuilder<>(
          translationBuilder,
          id -> "advancements." + id.getNamespace() + "." + id.getPath().replace('/', '.') + ".title",
          id -> "advancements." + id.getNamespace() + "." + id.getPath().replace('/', '.') + ".description"
        ));
        emiTranslations(translationBuilder);
        rituals(new GenericTranslationBuilder.RegistryTranslationBuilder<>(translationBuilder, SLRegistries.RITUAL_TYPES));
    }

    private static void emiTranslations(TranslationBuilder builder) {
        builder.add("emi.category.sanguinisluxuria.ritual", "Rituals");
        builder.add("emi.category.sanguinisluxuria.blood_cauldron", "Cauldron Infusing");
    }

    private static void tags(GenericTranslationBuilder.TagTranslationBuilder builder) {
        builder.add(SLTags.Items.VAMPIRE_MASKS, "Vampire Masks");
        builder.add(SLTags.Items.SUN_BLOCKING_HELMETS, "Sun Blocking Helmets");
        builder.add(SLTags.Items.VAMPIRES_GET_HUNGER_FROM, "Vampire Food");
        builder.add(SLTags.Items.BLOOD_STORING_BOTTLES, "Blood Storing Bottles");
        builder.add(SLTags.Items.DECAYED_LOGS, "Decayed Logs");
        builder.add(SLTags.Items.SILVER_INGOTS, "Silver Ingots");
        builder.add(SLTags.Items.SILVER_ORES, "Silver Ores");
        builder.add(SLTags.Items.SILVER_BLOCKS, "Silver Blocks");
        builder.add(SLTags.Items.RAW_SILVER_BLOCKS, "Raw Silver Blocks");
        builder.add(SLTags.Items.HUNGRY_DECAYED_LOGS, "Hungry Decayed Logs");
    }

    private static void deathMessages(GenericTranslationBuilder<RegistryKey<DamageType>> death, GenericTranslationBuilder<RegistryKey<DamageType>> afterDamage, GenericTranslationBuilder<RegistryKey<DamageType>> item) {
        death.add(SLResources.BLESSED_WATER_DAMAGE_KEY, "%s was burned by blessed water");
        death.add(SLResources.BITE_DAMAGE_KEY, "%s was bitten by %s");
        death.add(SLResources.BLOOD_DRAIN_DAMAGE_KEY, "%s had their blood drained");
        death.add(SLResources.PIERCING_DAMAGE_KEY, "%s was pierced by %s");
        afterDamage.add(SLResources.BLESSED_WATER_DAMAGE_KEY, "%s was burned by blessed water by %s");
        afterDamage.add(SLResources.BLOOD_DRAIN_DAMAGE_KEY, "%s had their blood drained by %s");
        item.add(SLResources.BITE_DAMAGE_KEY, "%s was bitten by %s using %s");
        item.add(SLResources.PIERCING_DAMAGE_KEY, "%s was pierced by %s using %s");
    }

    private static void abilities(GenericTranslationBuilder.RegistryTranslationBuilder<VampireAbility> builder) {
        builder.add(SLVampireAbilities.TELEPORT, "Blink");
        builder.add(SLVampireAbilities.INFECTIOUS, "Infectious");
        builder.add(SLVampireAbilities.BITE, "Bite");
        builder.add(SLVampireAbilities.MIST, "Mist");
        builder.add(SLVampireAbilities.VULNERABILITY, "Vulnerability");
        builder.add(SLVampireAbilities.RESILIENCE, "Resilience");
    }

    public static void rituals(GenericTranslationBuilder.RegistryTranslationBuilder<RitualType<?>> builder) {
        builder.add(SLRitualTypes.ABILITY_RITUAL_TYPE, "Ritual of Transformation");
        builder.add(SLRitualTypes.ABILITY_RESET_RITUAL_TYPE, "Ritual of Cleansing");
        builder.add(SLRitualTypes.ITEM_RITUAL_TYPE, "Ritual of Transmutation");
        builder.add(SLRitualTypes.ABILITY_REVEAL_RITUAL_TYPE, "Ritual of Revealing");
        builder.add(SLRitualTypes.ENTITY_SPAWNING_RITUAL_TYPE, "Ritual of Summoning");
        builder.add(SLRitualTypes.STATUS_EFFECT_RITUAL_TYPE, "Ritual of Alchemy");
        builder.add(SLRitualTypes.CONVERT_ENTITY_RITUAL, "Ritual of Conversion");
        builder.add(SLRitualTypes.CONVERT_ENTITY_RITUAL.getTranslationKey() + ".converting", "Corruption");
        builder.add(SLRitualTypes.CONVERT_ENTITY_RITUAL.getTranslationKey() + ".deconverting", "Purification");
        builder.add(SLRitualTypes.STATUS_EFFECT_RITUAL_TYPE.getTranslationKey() + ".effects", "Applies the effects to %s for %.0fs");
        builder.add(SLRitualTypes.STATUS_EFFECT_RITUAL_TYPE.getTranslationKey() + ".effect_entry", "- %s");
    }

    private static void potions(GenericTranslationBuilder.Potions builder) {
        builder.add(SLStatusEffects.BLESSED_WATER_POTION, "Blessed Water");
        builder.add(SLStatusEffects.BLESSED_WATER_POTION_TWO, "Blessed Water");
        builder.add(SLStatusEffects.BLOOD_LUST_POTION, "Bloodlust");
    }

    private static void enchantments(GenericTranslationBuilder.DescriptionTranslationBuilder<Enchantment> translationBuilder) {
        translationBuilder.add(SLEnchantments.SUN_PROTECTION,
          "Sun Protection",
          "Increases the amount of time a vampire can stay in the sun"
        );
        translationBuilder.add(SLEnchantments.BLOOD_TRANSFER,
          "Blood Transfer",
          "Allows a trident to latch on and slowly transfer blood from a target to the thrower"
        );
        translationBuilder.add(SLEnchantments.SERRATED,
          "Serrated",
          "Has a chance of inflicting bleeding on targets"
        );
    }

    private static void attributes(TranslationBuilder translationBuilder) {
        translationBuilder.add(SLEntityAttributes.BLESSED_DAMAGE, "Blessed Damage");
        translationBuilder.add(SLEntityAttributes.BLINK_COOLDOWN, "Blink Cooldown");
        translationBuilder.add(SLEntityAttributes.BLINK_RANGE, "Blink Range");
        translationBuilder.add(SLEntityAttributes.SUN_RESISTANCE, "Sun Resistance");
        translationBuilder.add(SLEntityAttributes.VULNERABILITY, "Vulnerability");
    }

    private static void entities(TranslationBuilder translationBuilder) {
        translationBuilder.add(SLEntities.VAMPIRE_ILLAGER, "Vampiric Illager");
        translationBuilder.add(SLEntities.VAMPIRE_MERCHANT, "Vampiric Merchant");
    }

    private static void keybindings(TranslationBuilder translationBuilder) {
        translationBuilder.add("key.sanguinisluxuria.drain_blood", "Drain Blood");
        translationBuilder.add("key.sanguinisluxuria.activate_bite", "Bite");
        translationBuilder.add("key.sanguinisluxuria.activate_blink", "Blink");
        translationBuilder.add("key.sanguinisluxuria.activate_mist", "Mist");
        translationBuilder.add("category.sanguinisluxuria.sanguinisluxuria", "Sanguinis Luxuria");
    }

    private static void subtitles(TranslationBuilder translationBuilder) {
        translationBuilder.add("subtitles.sanguinisluxuria.drain_blood", "Vampire feeding");
        translationBuilder.add("subtitles.sanguinisluxuria.altar_beats", "Altar beats");
        translationBuilder.add("subtitles.sanguinisluxuria.bleeding", "Something bleeds");
        translationBuilder.add("subtitles.sanguinisluxuria.entity_converted_to_vampire", "Something transforms");
    }

    private static void gui(TranslationBuilder translationBuilder) {
        translationBuilder.add("gui.sanguinisluxuria.blood_bottle_tooltip", "%d Blood Bottle(s)");
    }

    private static void config(TranslationBuilder translationBuilder) {
        translationBuilder.add("sanguinisluxuria.config.title", "Sanguinis Luxuria");
        translationBuilder.add("sanguinisluxuria.config.category.gameplay", "Gameplay");
        translationBuilder.add("sanguinisluxuria.config.option.vampire_damage_multiplier", "Damage Multiplier");
        translationBuilder.add("sanguinisluxuria.config.option.vampire_damage_multiplier.desc", "How much damage is multiplied for vampires from damage types they are weak to, such as fire.");
        translationBuilder.add("sanguinisluxuria.config.option.vampire_exhaustion_multiplier", "Exhaustion Multiplier");
        translationBuilder.add("sanguinisluxuria.config.option.vampire_exhaustion_multiplier.desc", "How much exhaustion is multiplied by for vampires");
        translationBuilder.add("sanguinisluxuria.config.category.abilities", "Abilities");
        translationBuilder.add("sanguinisluxuria.config.category.worldgen", "Worldgen");
        translationBuilder.add("sanguinisluxuria.config.option.generate_silver_ore", "Generate Silver Ore");
        translationBuilder.add("sanguinisluxuria.config.option.generate_silver_ore.desc", "If silver ore should generate naturally in the world");
        translationBuilder.add("sanguinisluxuria.config.option.blink_piercing_exhaustion", "Blink Piercing Exhaustion");
        translationBuilder.add("sanguinisluxuria.config.option.blink_piercing_exhaustion.desc", "The amount of exhaustion per entity pierced when using the Pendant of Piercing");
    }

    private static void blocks(TranslationBuilder translationBuilder) {
        translationBuilder.add(SLBlocks.DEEPSLATE_SILVER_ORE, "Deepslate Silver Ore");
        translationBuilder.add(SLBlocks.RAW_SILVER_BLOCK, "Block of Raw Silver");
        translationBuilder.add(SLBlocks.SILVER_BLOCK, "Silver Block");
        translationBuilder.add(SLBlocks.SILVER_ORE, "Silver Ore");
        translationBuilder.add(SLBlocks.PEDESTAL, "Pedestal");
        translationBuilder.add(SLBlocks.ALTAR, "Altar");
        translationBuilder.add(SLBlocks.BLOOD_CAULDRON, "Blood Cauldron");
        translationBuilder.add(SLBlocks.BLOOD_SPLATTER, "Blood");
        translationBuilder.add(SLBlocks.HUNGRY_DECAYED_LOG, "Hungry Decayed Log");
        translationBuilder.add(SLBlocks.STRIPPED_HUNGRY_DECAYED_LOG, "Stripped Hungry Decayed Log");
        translationBuilder.add(SLBlocks.STRIPPED_DECAYED_LOG, "Stripped Decayed Log");
        translationBuilder.add(SLBlocks.DECAYED_LOG, "Decayed Log");
        translationBuilder.add(SLBlocks.DECAYED_WOOD, "Decayed Wood");
        translationBuilder.add(SLBlocks.STRIPPED_DECAYED_WOOD, "Stripped Decayed Wood");
        translationBuilder.add(SLBlocks.DECAYED_TWIGS, "Decayed Twigs");
        translationBuilder.add(SLBlocks.GRAFTED_SAPLING, "Grafted Sapling");
        translationBuilder.add(SLBlocks.DECAYED_PRESSURE_PLATE, "Decayed Pressure Plate");
        translationBuilder.add(SLBlocks.SILVER_PRESSURE_PLATE, "Silver Pressure Plate");
        translationBuilder.add(SLBlocks.DECAYED_PLANKS, "Decayed Planks");
        translationBuilder.add(SLBlocks.DECAYED_SLAB, "Decayed Slab");
        translationBuilder.add(SLBlocks.DECAYED_FENCE, "Decayed Fence");
        translationBuilder.add(SLBlocks.DECAYED_FENCE_GATE, "Decayed Fence Gate");
        translationBuilder.add(SLBlocks.DECAYED_TRAPDOOR, "Decayed Trapdoor");
        translationBuilder.add(SLBlocks.DECAYED_DOOR, "Decayed Door");
        translationBuilder.add(SLBlocks.DECAYED_BUTTON, "Decayed Button");
        translationBuilder.add(SLBlocks.DECAYED_SIGN, "Decayed Sign");
        translationBuilder.add(SLBlocks.DECAYED_HANGING_SIGN, "Decayed Hanging Sign");
        translationBuilder.add(SLBlocks.DECAYED_STAIRS, "Decayed Stairs");
        translationBuilder.add(SLBlocks.POTTED_GRAFTED_SAPLING, "Potted Grafted Sapling");
        translationBuilder.add(SLBlocks.SILVER_BARS, "Silver Bars");

        translationBuilder.add("block.sanguinisluxuria.bed.no_sleep", "You can sleep only at day or during thunderstorms");
    }

    private static void items(TranslationBuilder translationBuilder) {
        translationBuilder.add(SLItems.BLOOD_PETAL, "Blood Petal");
        translationBuilder.add(SLItems.PENDANT_OF_PIERCING, "Pendant of Piercing");
        translationBuilder.add(SLItems.TWISTED_BLOOD, "Twisted Blood Bottle");
        translationBuilder.add(SLItems.SILVER_INGOT, "Silver Ingot");
        translationBuilder.add(SLItems.RAW_SILVER, "Raw Silver");
        translationBuilder.add(SLItems.SILVER_SWORD, "Silver Sword");
        translationBuilder.add(SLItems.SILVER_AXE, "Silver Axe");
        translationBuilder.add(SLItems.SILVER_PICKAXE, "Silver Pickaxe");
        translationBuilder.add(SLItems.SILVER_SHOVEL, "Silver Shovel");
        translationBuilder.add(SLItems.SILVER_HOE, "Silver Hoe");
        translationBuilder.add(SLItems.VAMPIRE_VILLAGER_SPAWN_EGG, "Vampiric Illager Spawn Egg");
        translationBuilder.add(SLItems.VAMPIRE_MERCHANT_SPAWN_EGG, "Vampiric Merchant Spawn Egg");
        translationBuilder.add(SLItems.MASK_1, "Carved Mask");
        translationBuilder.add(SLItems.MASK_2, "Carved Mask");
        translationBuilder.add(SLItems.MASK_3, "Carved Mask");
        translationBuilder.add(SLItems.BLOOD_BAG, "Blood Bag");
        translationBuilder.add(SLItems.BLOOD_BOTTLE, "Blood Bottle");
        translationBuilder.add(SLItems.VAMPIRE_FANG, "Vampire Fang");
        translationBuilder.add("item.sanguinisluxuria.book", "Sanguinis Luxuria");
        translationBuilder.add("sanguinisluxuria.landing", "A book of vampires.");
    }

    private static void statusEffects(GenericTranslationBuilder.DescriptionTranslationBuilder<StatusEffect> translationBuilder) {
        translationBuilder.add(SLStatusEffects.BLESSED_WATER,
          "Blessed Water",
          "Damages the undead and grants Blessed Blood to the living"
        );
        translationBuilder.add(SLStatusEffects.BLOOD_PROTECTION,
          "Blessed Blood",
          "Protects you from having your blood drained by vampires"
        );
        translationBuilder.add(SLStatusEffects.BLEEDING,
          "Bleeding",
          "Causes the afflicted entity to slowly bleed out"
        );
        translationBuilder.add(SLStatusEffects.BLOOD_LUST,
          "Bloodlust",
          "Gives the living a thirst for blood"
        );
    }

    public static void advancements(GenericTranslationBuilder.DescriptionTranslationBuilder<Identifier> translationBuilder) {
        translationBuilder.add(SLResources.id("become_vampire"),
          "Forever insatiable",
          "Cling on to life after receiving Bloodlust"
        );

        translationBuilder.add(SLResources.id("drink_twisted_blood"),
          "Consumption",
          "Drink twisted blood"
        );

        translationBuilder.add(SLResources.id("blood_sickness"),
          "Feeling ill",
          "Get blood sickness from drinking blood"
        );

        translationBuilder.add(SLResources.id("unlock_ability"),
          "Abilities",
          "Perform the Ritual of Transformation"
        );

        translationBuilder.add(SLResources.id("reset_abilities"),
          "Clean slate",
          "Perform the Ritual of Cleansing"
        );

        translationBuilder.add(SLResources.id("transfer_effects"),
          "No need for bottles",
          "Transfer a potion effect while draining blood"
        );

        translationBuilder.add(SLResources.id("transfer_more_effects"),
          "Alchemist",
          "Transfer 4 potions effects at once"
        );

        translationBuilder.add(SLResources.id("infect_other"),
          "Infectious",
          "Inflict blood sickness on something with Weakness"
        );

        translationBuilder.add(SLResources.id("unbecome_vampire"),
          "Return to humanity",
          "Perform the Ritual of Purification"
        );

        translationBuilder.add(SLResources.id("purify_other"),
          "No need for weakness",
          "Perform the Ritual of Purification on something other than yourself"
        );

        translationBuilder.add(SLResources.id("craft_hungry_sapling"),
          "Never meant to exist",
          "Craft a Grafted Sapling"
        );

        translationBuilder.add(SLResources.id("grow_decayed_tree"),
          "Decayed",
          "Grow a Grafted Sapling"
        );

        translationBuilder.add(SLResources.id("obtain_hungry_decayed_log"),
          "Blood collector",
          "Obtain a Hungry Decayed Log"
        );

        translationBuilder.add(SLResources.id("transfer_all_effects"),
          "How did we get there?",
          "Transfer every effect at once via Infectious"
        );

        translationBuilder.add(SLResources.id("receive_bloodlust"),
          "A terrible affliction",
          "Receive a thirst for blood"
        );
    }
}
