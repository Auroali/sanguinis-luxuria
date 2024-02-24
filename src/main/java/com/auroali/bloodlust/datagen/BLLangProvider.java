package com.auroali.bloodlust.datagen;

import com.auroali.bloodlust.Bloodlust;
import com.auroali.bloodlust.common.registry.BLDamageSources;
import com.auroali.bloodlust.common.registry.BLItems;
import com.auroali.bloodlust.common.registry.BLStatusEffects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;

public class BLLangProvider extends FabricLanguageProvider {
    public BLLangProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("key.bloodlust.bite", "Bite");
        translationBuilder.add("category.bloodlust.bloodlust", "Bloodlust");

        translationBuilder.add(BLItems.MASK_1, "Carved Mask");
        translationBuilder.add(BLItems.MASK_2, "Carved Mask");
        translationBuilder.add(BLItems.MASK_3, "Carved Mask");
        translationBuilder.add(BLItems.BLOOD_BAG, "Blood Bag");
        translationBuilder.add(BLItems.BLOOD_BOTTLE, "Blood Bottle");

        translationBuilder.add("subtitles.bloodlust.drain_blood", "Vampire feeding");

        translationBuilder.add("bloodlust.config.title", "Bloodlust");
        translationBuilder.add("bloodlust.config.category.gameplay", "Gameplay");
        translationBuilder.add("bloodlust.config.option.vampire_damage_multiplier", "Damage Multiplier");
        translationBuilder.add("bloodlust.config.option.vampire_damage_multiplier.desc", "How much damage is multiplied for vampires from damage types they are weak to, such as fire.");
        translationBuilder.add("bloodlust.config.option.vampire_exhaustion_multiplier", "Exhaustion Multiplier");
        translationBuilder.add("bloodlust.config.option.vampire_exhaustion_multiplier.desc", "How much exhaustion is multiplied by for vampires");
        translationBuilder.add("bloodlust.config.option.blessed_water_damage", "Blessed Water Damage");
        translationBuilder.add("bloodlust.config.option.blessed_water_damage.desc", "The base amount of damage blessed water does against vampires and undead entities");

        translationBuilder.add("death.attack.%s".formatted(BLDamageSources.BLOOD_DRAIN.name), "%1$s had their blood drained");
        translationBuilder.add("death.attack.%s.player".formatted(BLDamageSources.BLOOD_DRAIN.name), "%1$s had their blood drained whilst fighting %2$s");
        translationBuilder.add("death.attack.blood_drain_by_entity", "%1$s had their blood drained by %2$s");
        translationBuilder.add("death.attack.blood_drain_by_entity.item", "%1$s had their blood drained by %2$s");

        translationBuilder.add("death.attack.bloodlust.blessed_water", "%1$s was burned by blessed water");
        translationBuilder.add("death.attack.bloodlust.blessed_water.player", "%1$s was burned by blessed water whilst fighting %2$s");
        translationBuilder.add("death.attack.bloodlust.blessed_water_entity", "%1$s was burned by %2$s using blessed water");
        translationBuilder.add("death.attack.bloodlust.blessed_water_entity.item", "%1$s was burned by %2$s using blessed water");


        translationBuilder.add(Bloodlust.BLOODLUST_TAB, "Bloodlust");

        translationBuilder.add(BLStatusEffects.BLOOD_SICKNESS, "Blood Sickness");
        translationBuilder.add(BLStatusEffects.BLESSED_WATER, "Blessed Water");
        translationBuilder.add(BLStatusEffects.BLOOD_PROTECTION, "Blessed Blood");

        translationBuilder.add("argument.bloodlust.id.invalid", "'%s' is not a valid id!");

        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION, Items.POTION, "Potion of Blessed Water");
        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION, Items.SPLASH_POTION, "Splash Potion of Blessed Water");
        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION, Items.LINGERING_POTION, "Lingering Potion of Blessed Water");
        generatePotionKey(translationBuilder, BLStatusEffects.BLESSED_WATER_POTION, Items.TIPPED_ARROW, "Arrow of Blessed Water");
    }

    public static void generatePotionKey(TranslationBuilder builder, Potion potion, Item item, String entry) {
        ItemStack stack = new ItemStack(item);
        PotionUtil.setPotion(stack, potion);
        builder.add(stack.getTranslationKey(), entry);
    }
}
