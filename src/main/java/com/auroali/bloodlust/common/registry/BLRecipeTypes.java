package com.auroali.bloodlust.common.registry;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.common.recipes.AltarRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

public class BLRecipeTypes {
    public static final RecipeType<AltarRecipe> ALTAR_RECIPE = new RecipeType<>() {
        @Override
        public String toString() {
            return BLResources.ALTAR_RECIPE_ID.toString();
        }
    };

    public static void register() {
        Registry.register(Registry.RECIPE_TYPE, BLResources.ALTAR_RECIPE_ID, ALTAR_RECIPE);
    }
}
