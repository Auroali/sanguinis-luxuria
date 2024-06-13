package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.BLRecipeSerializers;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BloodCauldronRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final Item output;
    private final int outputCount;
    private String group;
    private final Ingredient ingredient;
    private final Advancement.Builder advancementBuilder = Advancement.Builder.create();

    BloodCauldronRecipeJsonBuilder(Ingredient ingredient, Item output, int outputCount) {
        this.output = output;
        this.outputCount = outputCount;
        this.ingredient = ingredient;
    }

    public static BloodCauldronRecipeJsonBuilder create(Ingredient ingredient, ItemConvertible output) {
        return new BloodCauldronRecipeJsonBuilder(ingredient, output.asItem(), 1);
    }

    @Override
    public BloodCauldronRecipeJsonBuilder criterion(String name, CriterionConditions conditions) {
        this.advancementBuilder.criterion(name, conditions);
        return this;
    }

    @Override
    public BloodCauldronRecipeJsonBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getOutputItem() {
        return output;
    }

    public void validate(Identifier id) {
        if (this.advancementBuilder.getCriteria().isEmpty())
            throw new IllegalStateException("No way of obtaining recipe " + id);
    }

    @Override
    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
        validate(recipeId);
        this.advancementBuilder
                .parent(ROOT)
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .criteriaMerger(CriterionMerger.OR);
        exporter.accept(
                new Provider(
                        recipeId,
                        new Identifier(recipeId.getNamespace(), "recipes/" + this.output.getGroup().getName() + "/" + recipeId.getPath()),
                        this
                )
        );
    }

    public static class Provider implements RecipeJsonProvider {
        final Item output;
        final int outputCount;
        final String group;
        public final Ingredient ingredient;
        private final Advancement.Builder advancementBuilder;
        private final Identifier id;
        private final Identifier advancementId;

        public Provider(Identifier recipeId, Identifier advancementId, BloodCauldronRecipeJsonBuilder builder) {
            this.outputCount = builder.outputCount;
            this.output = builder.output;
            this.advancementBuilder = builder.advancementBuilder;
            this.group = builder.group == null ? "" : builder.group;
            this.ingredient = builder.ingredient;
            this.id = recipeId;
            this.advancementId = advancementId;
        }

        @Override
        public void serialize(JsonObject json) {
            json.add("input", ingredient.toJson());

            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            JsonObject result = new JsonObject();
            result.addProperty("item", Registry.ITEM.getId(this.output).toString());
            if (this.outputCount > 1) {
                result.addProperty("count", this.outputCount);
            }
            json.add("result", result);
        }

        @Override
        public Identifier getRecipeId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return BLRecipeSerializers.BLOOD_CAULDRON_SERIALIZER;
        }

        @Nullable
        @Override
        public JsonObject toAdvancementJson() {
            return this.advancementBuilder.toJson();
        }

        @Nullable
        @Override
        public Identifier getAdvancementId() {
            return advancementId;
        }
    }
}
