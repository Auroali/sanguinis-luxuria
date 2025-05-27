package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.registry.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;

import java.util.function.BiConsumer;

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
        tags(translationBuilder);
        subtitles(translationBuilder);
        keybindings(translationBuilder);
        config(translationBuilder);
        statusEffects(translationBuilder);
        gui(translationBuilder);
        entities(translationBuilder);
        enchantments(translationBuilder);
        items(translationBuilder);
        blocks(translationBuilder);
        attributes(translationBuilder);
        potions(translationBuilder);
        abilities(translationBuilder);
        deathMessages(translationBuilder);
        advancements(translationBuilder);
        emiTranslations(translationBuilder);
        rituals(translationBuilder);
    }

    private static void generateTagTranslation(TranslationBuilder builder, TagKey<?> key, String translation) {
        String transKey = "tag.%s.%s.%s".formatted(key.registry().getValue().getPath(), key.id().getNamespace(), key.id().getPath().replace("/", "."));
        builder.add(transKey, translation);
    }

    private static void emiTranslations(TranslationBuilder builder) {
        builder.add("emi.category.sanguinisluxuria.ritual", "Rituals");
        builder.add("emi.category.sanguinisluxuria.blood_cauldron", "Cauldron Infusing");
    }

    private static void tags(TranslationBuilder builder) {
        generateTagTranslation(builder, SLTags.Items.VAMPIRE_MASKS, "Vampire Masks");
        generateTagTranslation(builder, SLTags.Items.SUN_BLOCKING_HELMETS, "Sun Blocking Helmets");
        generateTagTranslation(builder, SLTags.Items.VAMPIRES_GET_HUNGER_FROM, "Vampire Food");
        generateTagTranslation(builder, SLTags.Items.BLOOD_STORING_BOTTLES, "Blood Storing Bottles");
        generateTagTranslation(builder, SLTags.Items.DECAYED_LOGS, "Decayed Logs");
        generateTagTranslation(builder, SLTags.Items.SILVER_INGOTS, "Silver Ingots");
        generateTagTranslation(builder, SLTags.Items.SILVER_ORES, "Silver Ores");
        generateTagTranslation(builder, SLTags.Items.SILVER_BLOCKS, "Silver Blocks");
        generateTagTranslation(builder, SLTags.Items.RAW_SILVER_BLOCKS, "Raw Silver Blocks");
    }

    private static void deathMessages(TranslationBuilder translationBuilder) {
        BiConsumer<RegistryKey<DamageType>, String> death = (key, name) -> translationBuilder.add("death.attack.%s.%s".formatted(key.getValue().getNamespace(), key.getValue().getPath()), name);
        BiConsumer<RegistryKey<DamageType>, String> deathItem = (key, name) -> translationBuilder.add("death.attack.%s.%s.item".formatted(key.getValue().getNamespace(), key.getValue().getPath()), name);
        BiConsumer<RegistryKey<DamageType>, String> deathPlayer = (key, name) -> translationBuilder.add("death.attack.%s.%s.player".formatted(key.getValue().getNamespace(), key.getValue().getPath()), name);
        death.accept(SLResources.BLESSED_WATER_DAMAGE_KEY, "%s was burned by blessed water");
        deathPlayer.accept(SLResources.BLESSED_WATER_DAMAGE_KEY, "%s was burned by blessed water whilst trying to escape %s");
        death.accept(SLResources.BITE_DAMAGE_KEY, "%s was bitten by %s");
        deathItem.accept(SLResources.BITE_DAMAGE_KEY, "%s was bitten by %s using %s");
        death.accept(SLResources.BLOOD_DRAIN_DAMAGE_KEY, "%s had their blood drained");
        deathPlayer.accept(SLResources.BLOOD_DRAIN_DAMAGE_KEY, "%s had their blood drained whilst trying to escape %s");
        death.accept(SLResources.TELEPORT_DAMAGE_KEY, "%s was pierced by %s");
        deathItem.accept(SLResources.TELEPORT_DAMAGE_KEY, "%s was pierced by %s using %s");
    }

    private static void abilities(TranslationBuilder translationBuilder) {
        generateAbilityKey(translationBuilder, SLVampireAbilities.TELEPORT, "Blink");
        generateAbilityKey(translationBuilder, SLVampireAbilities.INFECTIOUS, "Infectious");
        generateAbilityKey(translationBuilder, SLVampireAbilities.BITE, "Bite");
        generateAbilityKey(translationBuilder, SLVampireAbilities.MIST, "Mist");
        generateAbilityKey(translationBuilder, SLVampireAbilities.VULNERABILITY, "Vulnerability");
        generateAbilityKey(translationBuilder, SLVampireAbilities.RESILIENCE, "Resilience");
    }

    public static void rituals(TranslationBuilder builder) {
        builder.add(SLRitualTypes.ABILITY_RITUAL_TYPE.getTranslationKey(), "Ritual of Transformation");
        builder.add(SLRitualTypes.ABILITY_RESET_RITUAL_TYPE.getTranslationKey(), "Ritual of Cleansing");
        builder.add(SLRitualTypes.ITEM_RITUAL_TYPE.getTranslationKey(), "Ritual of Transmutation");
        builder.add(SLRitualTypes.ABILITY_REVEAL_RITUAL_TYPE.getTranslationKey(), "Ritual of Revealing");
        builder.add(SLRitualTypes.ENTITY_SPAWNING_RITUAL_TYPE.getTranslationKey(), "Ritual of Summoning");
        builder.add(SLRitualTypes.STATUS_EFFECT_RITUAL_TYPE.getTranslationKey(), "Ritual of Alchemy");
        builder.add(SLRitualTypes.CONVERT_ENTITY_RITUAL.getTranslationKey(), "Ritual of Conversion");
        builder.add("altar_ritual.sanguinisluxuria.convert.converting", "Converting");
        builder.add("altar_ritual.sanguinisluxuria.convert.deconverting", "Deconverting");
        builder.add("altar_ritual.sanguinisluxuria.effects", "Applies the effects to %s for %.1ds");
    }

    private static void potions(TranslationBuilder translationBuilder) {
        generatePotionKey(translationBuilder, SLStatusEffects.BLESSED_WATER_POTION, Items.POTION, "Potion of Blessed Water");
        generatePotionKey(translationBuilder, SLStatusEffects.BLESSED_WATER_POTION, Items.SPLASH_POTION, "Splash Potion of Blessed Water");
        generatePotionKey(translationBuilder, SLStatusEffects.BLESSED_WATER_POTION, Items.LINGERING_POTION, "Lingering Potion of Blessed Water");
        generatePotionKey(translationBuilder, SLStatusEffects.BLESSED_WATER_POTION, Items.TIPPED_ARROW, "Arrow of Blessed Water");

        generatePotionKey(translationBuilder, SLStatusEffects.BLESSED_WATER_POTION_TWO, Items.POTION, "Potion of Blessed Water");
        generatePotionKey(translationBuilder, SLStatusEffects.BLESSED_WATER_POTION_TWO, Items.SPLASH_POTION, "Splash Potion of Blessed Water");
        generatePotionKey(translationBuilder, SLStatusEffects.BLESSED_WATER_POTION_TWO, Items.LINGERING_POTION, "Lingering Potion of Blessed Water");
        generatePotionKey(translationBuilder, SLStatusEffects.BLESSED_WATER_POTION_TWO, Items.TIPPED_ARROW, "Arrow of Blessed Water");

        generatePotionKey(translationBuilder, SLStatusEffects.BLOOD_LUST_POTION, Items.POTION, "Potion of Blood Lust");
        generatePotionKey(translationBuilder, SLStatusEffects.BLOOD_LUST_POTION, Items.SPLASH_POTION, "Splash Potion of Blood Lust");
        generatePotionKey(translationBuilder, SLStatusEffects.BLOOD_LUST_POTION, Items.LINGERING_POTION, "Lingering Potion of Blood Lust");
        generatePotionKey(translationBuilder, SLStatusEffects.BLOOD_LUST_POTION, Items.TIPPED_ARROW, "Arrow of Blood Lust");
    }

    private static void enchantments(TranslationBuilder translationBuilder) {
        translationBuilder.add(SLEnchantments.SUN_PROTECTION, "Sun Protection");
        enchantmentDescription(translationBuilder, SLEnchantments.SUN_PROTECTION, "Increases the amount of time a vampire can stay in the sun");
        translationBuilder.add(SLEnchantments.BLOOD_TRANSFER, "Blood Transfer");
        enchantmentDescription(translationBuilder, SLEnchantments.BLOOD_TRANSFER, "Allows a trident to latch on and slowly transfer blood from a target to the thrower");
        translationBuilder.add(SLEnchantments.SERRATED, "Serrated");
        enchantmentDescription(translationBuilder, SLEnchantments.SERRATED, "Has a chance of inflicting bleeding on targets");
    }

    private static void enchantmentDescription(TranslationBuilder builder, Enchantment enchantment, String description) {
        builder.add(enchantment.getTranslationKey() + ".desc", description);
    }

    private static void attributes(TranslationBuilder translationBuilder) {
        translationBuilder.add(SLEntityAttributes.BLESSED_DAMAGE.getTranslationKey(), "Blessed Damage");
        translationBuilder.add(SLEntityAttributes.BLINK_COOLDOWN.getTranslationKey(), "Blink Cooldown");
        translationBuilder.add(SLEntityAttributes.BLINK_RANGE.getTranslationKey(), "Blink Range");
        translationBuilder.add(SLEntityAttributes.SUN_RESISTANCE.getTranslationKey(), "Sun Resistance");
        translationBuilder.add(SLEntityAttributes.VULNERABILITY.getTranslationKey(), "Vulnerability");
    }

    private static void entities(TranslationBuilder translationBuilder) {
        translationBuilder.add(SLEntities.VAMPIRE_VILLAGER, "Vampiric Villager");
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
        translationBuilder.add(SLItems.VAMPIRE_VILLAGER_SPAWN_EGG, "Vampire Villager Spawn Egg");
        translationBuilder.add(SLItems.MASK_1, "Carved Mask");
        translationBuilder.add(SLItems.MASK_2, "Carved Mask");
        translationBuilder.add(SLItems.MASK_3, "Carved Mask");
        translationBuilder.add(SLItems.BLOOD_BAG, "Blood Bag");
        translationBuilder.add(SLItems.BLOOD_BOTTLE, "Blood Bottle");
        translationBuilder.add(SLItems.VAMPIRE_FANG, "Vampire Fang");
        translationBuilder.add("item.sanguinisluxuria.book", "Sanguinis Luxuria");
        translationBuilder.add("sanguinisluxuria.landing", "A book of vampires.");
    }

    private static void statusEffects(TranslationBuilder translationBuilder) {
        translationBuilder.add(SLStatusEffects.BLOOD_SICKNESS, "Blood Sickness");
        generateStatusEffectDescription(translationBuilder, SLStatusEffects.BLOOD_SICKNESS, "Gained from drinking blood. High enough levels will convert you to a vampire");
        translationBuilder.add(SLStatusEffects.BLESSED_WATER, "Blessed Water");
        generateStatusEffectDescription(translationBuilder, SLStatusEffects.BLESSED_WATER, "Damages the undead and grants Blessed Blood to the living");
        translationBuilder.add(SLStatusEffects.BLOOD_PROTECTION, "Blessed Blood");
        generateStatusEffectDescription(translationBuilder, SLStatusEffects.BLOOD_PROTECTION, "Protects you from having your blood drained by vampires");
        translationBuilder.add(SLStatusEffects.BLEEDING, "Bleeding");
        generateStatusEffectDescription(translationBuilder, SLStatusEffects.BLEEDING, "Causes the afflicted entity to slowly bleed out");
        translationBuilder.add(SLStatusEffects.BLOOD_LUST, "Bloodlust");
        generateStatusEffectDescription(translationBuilder, SLStatusEffects.BLOOD_LUST, "Gives the living a thirst for blood");
    }

    public static void advancements(TranslationBuilder translationBuilder) {
        translationBuilder.add(SLAdvancementsProvider.title("become_vampire"), "Bloodlust");
        translationBuilder.add(SLAdvancementsProvider.desc("become_vampire"), "Transform into a vampire after drinking enough blood");

        translationBuilder.add(SLAdvancementsProvider.title("drink_twisted_blood"), "Consumption");
        translationBuilder.add(SLAdvancementsProvider.desc("drink_twisted_blood"), "Drink twisted blood");

        translationBuilder.add(SLAdvancementsProvider.title("blood_sickness"), "Feeling Ill");
        translationBuilder.add(SLAdvancementsProvider.desc("blood_sickness"), "Get blood sickness from drinking blood");

        translationBuilder.add(SLAdvancementsProvider.title("unlock_ability"), "Abilities");
        translationBuilder.add(SLAdvancementsProvider.desc("unlock_ability"), "Perform the Ritual of Transformation");

        translationBuilder.add(SLAdvancementsProvider.title("reset_abilities"), "Clean Slate");
        translationBuilder.add(SLAdvancementsProvider.desc("reset_abilities"), "Convert unlocked abilities back into skill points using blessed blood");

        translationBuilder.add(SLAdvancementsProvider.title("transfer_effects"), "No Need for Bottles");
        translationBuilder.add(SLAdvancementsProvider.desc("transfer_effects"), "Transfer a potion effect while draining blood");

        translationBuilder.add(SLAdvancementsProvider.title("transfer_more_effects"), "Alchemist");
        translationBuilder.add(SLAdvancementsProvider.desc("transfer_more_effects"), "Transfer 4 potions effects at once");

        translationBuilder.add(SLAdvancementsProvider.title("infect_other"), "Infectious");
        translationBuilder.add(SLAdvancementsProvider.desc("infect_other"), "Inflict blood sickness on something with Weakness");

        translationBuilder.add(SLAdvancementsProvider.title("unbecome_vampire"), "Humanity");
        translationBuilder.add(SLAdvancementsProvider.desc("unbecome_vampire"), "Become human again after drinking Blessed Water with weakness");

        translationBuilder.add(SLAdvancementsProvider.title("craft_hungry_sapling"), "Grafted Petal");
        translationBuilder.add(SLAdvancementsProvider.desc("craft_hungry_sapling"), "Craft a Hungry Sapling");

        translationBuilder.add(SLAdvancementsProvider.title("grow_decayed_tree"), "Decayed");
        translationBuilder.add(SLAdvancementsProvider.desc("grow_decayed_tree"), "Grow a Grafted Sapling");

        translationBuilder.add(SLAdvancementsProvider.title("obtain_hungry_decayed_log"), "Blood Collector");
        translationBuilder.add(SLAdvancementsProvider.desc("obtain_hungry_decayed_log"), "Obtain a Hungry Decayed Log");

        translationBuilder.add(SLAdvancementsProvider.title("transfer_all_effects"), "How did we get there?");
        translationBuilder.add(SLAdvancementsProvider.desc("transfer_all_effects"), "Transfer every effect at once via Infectious");
    }

    public static void generatePotionKey(TranslationBuilder builder, Potion potion, Item item, String entry) {
        ItemStack stack = new ItemStack(item);
        PotionUtil.setPotion(stack, potion);
        builder.add(stack.getTranslationKey(), entry);
    }

    public static void generateAbilityKey(TranslationBuilder builder, VampireAbility ability, String entry) {
        builder.add(ability.getTranslationKey(), entry);
    }

    public static void generateStatusEffectDescription(TranslationBuilder builder, StatusEffect effect, String entry) {
        builder.add(effect.getTranslationKey() + ".desc", entry);
    }
}
