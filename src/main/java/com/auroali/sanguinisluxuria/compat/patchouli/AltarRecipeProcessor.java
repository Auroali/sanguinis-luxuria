package com.auroali.sanguinisluxuria.compat.patchouli;

import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.auroali.sanguinisluxuria.common.recipes.AltarRitualRecipe;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.Optional;

public class AltarRecipeProcessor implements IComponentProcessor {
    private AltarRitualRecipe recipe;

    @Override
    public void setup(World world, IVariableProvider variables) {
        String id = variables.get("recipe").asString();
        RecipeManager manager = world.getRecipeManager();
        this.recipe = manager.get(new Identifier(id))
          .flatMap(r -> r instanceof AltarRitualRecipe ritualRecipe ? Optional.of(ritualRecipe) : Optional.empty())
          .orElse(null);
        if (this.recipe == null) {
            SanguinisLuxuria.LOGGER.warn("Could not find ritual recipe {} for patchouli entry", id);
        }
    }

    @Override
    public IVariable process(World world, String key) {
        if (this.recipe == null)
            return null;

        if (key.startsWith("input")) {
            int i = Integer.parseInt(key.substring(5)) - 1;
            if (i < this.recipe.getIngredients().size()) {
                Ingredient ingredient = this.recipe.getIngredients().get(i);
                return IVariable.from(ingredient.getMatchingStacks());
            }
            return IVariable.empty();
        }
        if (key.startsWith("catalyst")) {
            return IVariable.from(this.recipe.getCatalyst().getMatchingStacks());
        }
        if (key.startsWith("ritual_name")) {
            Identifier id = RitualType.getId(this.recipe.getRitual().getType());
            if (id == null)
                return IVariable.empty();
            Text name = Text.translatable(this.recipe.getRitual().getType().getTranslationKey()).formatted(Formatting.GOLD, Formatting.BOLD);
            return IVariable.from(name);
        }
        return null;
    }
}
