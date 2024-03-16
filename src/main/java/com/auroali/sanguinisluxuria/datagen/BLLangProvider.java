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

        translationBuilder.add("sanguinisluxuria.config.title", "Bloodlust");
        translationBuilder.add("sanguinisluxuria.config.category.gameplay", "Gameplay");
        translationBuilder.add("sanguinisluxuria.config.option.vampire_damage_multiplier", "Damage Multiplier");
        translationBuilder.add("sanguinisluxuria.config.option.vampire_damage_multiplier.desc", "How much damage is multiplied for vampires from damage types they are weak to, such as fire.");
        translationBuilder.add("sanguinisluxuria.config.option.vampire_exhaustion_multiplier", "Exhaustion Multiplier");
        translationBuilder.add("sanguinisluxuria.config.option.vampire_exhaustion_multiplier.desc", "How much exhaustion is multiplied by for vampires");
        translationBuilder.add("sanguinisluxuria.config.option.blessed_water_damage", "Blessed Water Damage");
        translationBuilder.add("sanguinisluxuria.config.option.blessed_water_damage.desc", "The base amount of damage blessed water does against vampires and undead entities");

        translationBuilder.add("death.attack.%s".formatted(BLDamageSources.BLOOD_DRAIN.name), "%1$s had their blood drained");
        translationBuilder.add("death.attack.%s.player".formatted(BLDamageSources.BLOOD_DRAIN.name), "%1$s had their blood drained whilst fighting %2$s");
        translationBuilder.add("death.attack.sanguinisluxuria.blood_drain_by_entity", "%1$s had their blood drained by %2$s");
        translationBuilder.add("death.attack.sanguinisluxuria.blood_drain_by_entity.item", "%1$s had their blood drained by %2$s");

        translationBuilder.add("death.attack.sanguinisluxuria.bite", "%1$s was bit by %2$s");
        translationBuilder.add("death.attack.sanguinisluxuria.bite.item", "%1$s was bit by %2$s");

        translationBuilder.add("death.attack.sanguinisluxuria.blessed_water", "%1$s was burned by blessed water");
        translationBuilder.add("death.attack.sanguinisluxuria.blessed_water.player", "%1$s was burned by blessed water whilst fighting %2$s");
        translationBuilder.add("death.attack.sanguinisluxuria.blessed_water_entity", "%1$s was burned by %2$s using blessed water");
        translationBuilder.add("death.attack.sanguinisluxuria.blessed_water_entity.item", "%1$s was burned by %2$s using blessed water");


        translationBuilder.add(Bloodlust.BLOODLUST_TAB, "Sanguinis Luxuria");

        translationBuilder.add(BLStatusEffects.BLOOD_SICKNESS, "Blood Sickness");
        translationBuilder.add(BLStatusEffects.BLESSED_WATER, "Blessed Water");
        translationBuilder.add(BLStatusEffects.BLOOD_PROTECTION, "Blessed Blood");

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
        translationBuilder.add(BLBlocks.SKILL_UPGRADER, "Altar");

        translationBuilder.add("sanguinisluxuria.config.category.abilities", "Abilities");
        translationBuilder.add("sanguinisluxuria.config.option.skill_points_per_level", "Skill Points Per Level");
        translationBuilder.add("sanguinisluxuria.config.option.skill_points_per_level.desc", "How many skill points are gained per level");

        translationBuilder.add("item.sanguinisluxuria.book", "Sanguinis Luxuria");
        translationBuilder.add("sanguinisluxuria.landing", "A book of vampires.");

        translationBuilder.add(BLEnchantments.SUN_PROTECTION, "Sun Protection");
        translationBuilder.add(BLEnchantments.SUN_PROTECTION.getTranslationKey() + ".desc", "Increases the amount of time a vampire can stay in the sun");

        translationBuilder.add(BLEnchantments.BLOOD_DRAIN, "Blood Transfer");
        translationBuilder.add(BLEnchantments.BLOOD_DRAIN.getTranslationKey() + ".desc", "Allows a trident to latch on and slowly drain blood from what it hits, filling up either a vampire's blood bar or a held item that can store blood");

        translationBuilder.add(BLBlocks.BLOOD_CAULDRON, "Blood Cauldron");
        translationBuilder.add(BLBlocks.BLOOD_SPLATTER, "Blood");

        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION, Items.POTION, "Potion of Blessed Water");
        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION, Items.SPLASH_POTION, "Splash Potion of Blessed Water");
        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION, Items.LINGERING_POTION, "Lingering Potion of Blessed Water");
        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION, Items.TIPPED_ARROW, "Arrow of Blessed Water");

        generateAbilityKey(translationBuilder, BLVampireAbilities.HEALTH_1, "Increased Health");
        generateAbilityKey(translationBuilder, BLVampireAbilities.HEALTH_2, "Increased Health");
        generateAbilityKey(translationBuilder, BLVampireAbilities.VAMPIRE_STRENGTH_1, "Increased Strength");
        generateAbilityKey(translationBuilder, BLVampireAbilities.VAMPIRE_STRENGTH_2, "Increased Strength");
        generateAbilityKey(translationBuilder, BLVampireAbilities.TELEPORT, "Teleport");
        generateAbilityKey(translationBuilder, BLVampireAbilities.TELEPORT_COOLDOWN_1, "Decreased Teleport Cooldown");
        generateAbilityKey(translationBuilder, BLVampireAbilities.TELEPORT_COOLDOWN_2, "Decreased Teleport Cooldown");
        generateAbilityKey(translationBuilder, BLVampireAbilities.TELEPORT_RANGE_1, "Increased Teleport Range");
        generateAbilityKey(translationBuilder, BLVampireAbilities.TELEPORT_RANGE_2, "Increased Teleport Range");
        generateAbilityKey(translationBuilder, BLVampireAbilities.MORE_BLOOD, "Blood Efficiency");
        generateAbilityKey(translationBuilder, BLVampireAbilities.TRANSFER_EFFECTS, "Infectious");
        generateAbilityKey(translationBuilder, BLVampireAbilities.BITE, "Bite");
        generateAbilityKey(translationBuilder, BLVampireAbilities.SUN_PROTECTION, "Sun Resistance");
        generateAbilityKey(translationBuilder, BLVampireAbilities.DAMAGE_REDUCTION, "Resistance");

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
    }

    public static void generatePotionKey(TranslationBuilder builder, Potion potion, Item item, String entry) {
        ItemStack stack = new ItemStack(item);
        PotionUtil.setPotion(stack, potion);
        builder.add(stack.getTranslationKey(), entry);
    }

    public static void generateAbilityKey(TranslationBuilder builder, VampireAbility ability, String entry) {
        builder.add(ability.getTranslationKey(), entry);
    }
}
