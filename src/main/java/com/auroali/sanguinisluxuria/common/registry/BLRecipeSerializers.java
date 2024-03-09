package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.recipes.AltarRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.registry.Registry;

public class BLRecipeSerializers {
    public static final RecipeSerializer<AltarRecipe> ALTAR_RECIPE_SERIALIZER = new AltarRecipe.Serializer();

    public static void register() {
        Registry.register(Registry.RECIPE_SERIALIZER, BLResources.ALTAR_RECIPE_ID, ALTAR_RECIPE_SERIALIZER);
    }
}
