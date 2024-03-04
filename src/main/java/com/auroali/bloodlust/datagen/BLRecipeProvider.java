package com.auroali.bloodlust.datagen;

import com.auroali.bloodlust.common.advancements.BecomeVampireCriterion;
import com.auroali.bloodlust.common.registry.BLItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.SingleItemRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;

import java.util.function.Consumer;

public class BLRecipeProvider extends FabricRecipeProvider {
    public BLRecipeProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.fromTag(ItemTags.LOGS), BLItems.MASK_1)
                .criterion("has_log", conditionsFromTag(ItemTags.LOGS))
                .offerTo(exporter);
        SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.fromTag(ItemTags.LOGS), BLItems.MASK_2)
                .criterion("has_log", conditionsFromTag(ItemTags.LOGS))
                .offerTo(exporter);
        SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.fromTag(ItemTags.LOGS), BLItems.MASK_3)
                .criterion("has_log", conditionsFromTag(ItemTags.LOGS))
                .offerTo(exporter);
        AltarRecipeJsonBuilder.create(BLItems.TWISTED_BLOOD)
                .input(BLItems.BLOOD_BOTTLE)
                .input(Items.NETHER_WART)
                .input(Items.WITHER_ROSE)
                .input(Items.TWISTING_VINES)
                .criterion("is_vampire", BecomeVampireCriterion.Conditions.create())
                .offerTo(exporter);

    }
}
