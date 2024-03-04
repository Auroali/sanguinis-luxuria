package com.auroali.bloodlust.common.registry;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.common.recipes.AltarRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

public class BLRecipeSerializers {
    public static final RecipeSerializer<AltarRecipe> ALTAR_RECIPE_SERIALIZER = new AltarRecipe.Serializer();

    public static void register() {
        Registry.register(Registry.RECIPE_SERIALIZER, BLResources.ALTAR_RECIPE_ID, ALTAR_RECIPE_SERIALIZER);
    }
}
