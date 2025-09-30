package com.auroali.sanguinisluxuria.datagen.builders;

import com.auroali.sanguinisluxuria.common.registry.SLRecipeSerializers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class BloodCauldronRecipeJsonBuilder extends RecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final Item output;
    private int level;
    private final int outputCount;
    private String group;
    private final Ingredient ingredient;
    private final Advancement.Builder advancementBuilder = Advancement.Builder.create();
    private final RecipeCategory category;
    private final RecipeSerializer<?> serializer;

    BloodCauldronRecipeJsonBuilder(RecipeCategory category, Ingredient ingredient, Item output, int outputCount, RecipeSerializer<?> serializer) {
        this.output = output;
        this.outputCount = outputCount;
        this.ingredient = ingredient;
        this.category = category;
        this.level = 1;
        this.serializer = serializer;
    }

    public BloodCauldronRecipeJsonBuilder level(int level) {
        if (level < 1 || level > 3)
            throw new IllegalArgumentException("level must be between 1 and 3!");
        this.level = level;
        return this;
    }

    public static BloodCauldronRecipeJsonBuilder create(RecipeCategory category, Ingredient ingredient, ItemConvertible output) {
        return new Default(category, ingredient, output.asItem(), 1);
    }

    public static BloodCauldronRecipeJsonBuilder createFilling(RecipeCategory category, Ingredient ingredient, ItemConvertible output) {
        return new Fill(category, ingredient, output.asItem(), 1);
    }

    public static BloodCauldronRecipeJsonBuilder createFilling(RecipeCategory category, ItemConvertible output) {
        return new Fill(category, Ingredient.ofItems(output), output.asItem(), 1);
    }

    public static BloodCauldronRecipeJsonBuilder createCopying(RecipeCategory category, Ingredient ingredient, ItemConvertible output, String... keys) {
        return new Copy(category, ingredient, output.asItem(), 1, keys);
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
        return this.output;
    }

    protected abstract void serializeAdditional(JsonObject object);

    public void validate(Identifier id) {
        if (this.advancementBuilder.getCriteria().isEmpty())
            throw new IllegalStateException("No way of obtaining recipe " + id);
    }

    @Override
    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
        this.validate(recipeId);
        this.advancementBuilder
          .parent(ROOT)
          .criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId))
          .rewards(AdvancementRewards.Builder.recipe(recipeId))
          .criteriaMerger(CriterionMerger.OR);
        exporter.accept(
          new Provider(
            recipeId,
            recipeId.withPrefixedPath("recipes/" + this.category.getName() + "/"),
            this,
            getCraftingCategory(this.category),
            this::serializeAdditional,
            this.serializer
          )
        );
    }

    protected static class Default extends BloodCauldronRecipeJsonBuilder {

        Default(RecipeCategory category, Ingredient ingredient, Item output, int outputCount) {
            super(category, ingredient, output, outputCount, SLRecipeSerializers.BLOOD_CAULDRON_SERIALIZER);
        }

        @Override
        protected void serializeAdditional(JsonObject object) {

        }
    }

    protected static class Fill extends BloodCauldronRecipeJsonBuilder {

        Fill(RecipeCategory category, Ingredient ingredient, Item output, int outputCount) {
            super(category, ingredient, output, outputCount, SLRecipeSerializers.BLOOD_CAULDRON_FILL_SERIALIZER);
        }

        @Override
        protected void serializeAdditional(JsonObject object) {

        }
    }

    protected static class Copy extends BloodCauldronRecipeJsonBuilder {
        private final String[] keys;

        Copy(RecipeCategory category, Ingredient ingredient, Item output, int outputCount, String[] keys) {
            super(category, ingredient, output, outputCount, SLRecipeSerializers.BLOOD_CAULDRON_COPY_SERIALIZER);
            this.keys = keys;
        }

        @Override
        protected void serializeAdditional(JsonObject object) {
            JsonArray copied = new JsonArray();
            for (String key : this.keys) {
                copied.add(key);
            }
            object.add("copied", copied);
        }
    }

    public static class Provider implements RecipeJsonProvider {
        final Item output;
        final int outputCount;
        final String group;
        public final Ingredient ingredient;
        public final int level;
        private final Advancement.Builder advancementBuilder;
        private final Identifier id;
        private final Identifier advancementId;
        private final CraftingRecipeCategory category;
        // todo: make this not suck
        private final Consumer<JsonObject> writer;
        private final RecipeSerializer<?> serializer;

        public Provider(Identifier recipeId, Identifier advancementId, BloodCauldronRecipeJsonBuilder builder, CraftingRecipeCategory category, Consumer<JsonObject> writer, RecipeSerializer<?> serializer) {
            this.outputCount = builder.outputCount;
            this.output = builder.output;
            this.advancementBuilder = builder.advancementBuilder;
            this.group = builder.group == null ? "" : builder.group;
            this.ingredient = builder.ingredient;
            this.id = recipeId;
            this.advancementId = advancementId;
            this.category = category;
            this.level = builder.level;
            this.writer = writer;
            this.serializer = serializer;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("category", this.category.asString());
            json.add("input", this.ingredient.toJson());
            json.addProperty("level", this.level);

            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            JsonObject result = new JsonObject();
            result.addProperty("item", Registries.ITEM.getId(this.output).toString());
            if (this.outputCount > 1) {
                result.addProperty("count", this.outputCount);
            }
            json.add("result", result);
            this.writer.accept(json);
        }

        @Override
        public Identifier getRecipeId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return this.serializer;
        }

        @Nullable
        @Override
        public JsonObject toAdvancementJson() {
            return this.advancementBuilder.toJson();
        }

        @Nullable
        @Override
        public Identifier getAdvancementId() {
            return this.advancementId;
        }
    }
}
