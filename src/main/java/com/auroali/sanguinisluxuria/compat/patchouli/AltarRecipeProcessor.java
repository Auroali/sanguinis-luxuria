package com.auroali.sanguinisluxuria.compat.patchouli;

import com.auroali.sanguinisluxuria.common.recipes.AltarRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class AltarRecipeProcessor implements IComponentProcessor {
    private AltarRecipe recipe;
    @Override
    public void setup(IVariableProvider variables) {
        String id = variables.get("recipe").asString();
        RecipeManager manager = MinecraftClient.getInstance().world.getRecipeManager();
        recipe = (AltarRecipe) manager.get(new Identifier(id)).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public IVariable process(String key) {
        if(key.startsWith("item")) {
            int i = Integer.parseInt(key.substring(4)) - 1;
            if(i < recipe.getIngredients().size()) {
                Ingredient ingredient = recipe.getIngredients().get(i);
                ItemStack stack = ingredient.getMatchingStacks().length == 0 ? ItemStack.EMPTY : ingredient.getMatchingStacks()[0];
                return IVariable.from(stack);
            }
            return IVariable.from(ItemStack.EMPTY);
        }
        if(key.equals("output"))
            return IVariable.from(recipe.getOutput());
        if(key.equals("time")) {
            return IVariable.from(Text.of("%ds".formatted(recipe.getProcessingTicks() / 20)));
        }
        return null;
    }
}
