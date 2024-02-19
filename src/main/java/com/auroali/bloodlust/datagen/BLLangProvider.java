package com.auroali.bloodlust.datagen;

import com.auroali.bloodlust.common.registry.BLItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

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

        translationBuilder.add("subtitles.bloodlust.drain_blood", "Vampire feeding");

        translationBuilder.add("bloodlust.config.title", "Bloodlust");
        translationBuilder.add("bloodlust.config.category.gameplay", "Gameplay");
        translationBuilder.add("bloodlust.config.option.vampire_damage_multiplier", "Damage Multiplier");
        translationBuilder.add("bloodlust.config.option.vampire_damage_multiplier.desc", "How much damage is multiplied for vampires from damage types they are weak to, such as fire.");
        translationBuilder.add("bloodlust.config.option.vampire_exhaustion_multiplier", "Exhaustion Multiplier");
        translationBuilder.add("bloodlust.config.option.vampire_exhaustion_multiplier.desc", "How much exhaustion is multiplied by for vampires");
    }
}
