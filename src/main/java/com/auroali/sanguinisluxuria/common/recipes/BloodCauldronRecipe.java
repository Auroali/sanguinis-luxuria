package com.auroali.sanguinisluxuria.common.recipes;

import com.auroali.sanguinisluxuria.common.registry.BLRecipeSerializers;
import com.auroali.sanguinisluxuria.common.registry.BLRecipeTypes;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class BloodCauldronRecipe implements Recipe<SimpleInventory> {
    private final Ingredient ingredient;
    private final Identifier id;
    private final ItemStack result;

    public BloodCauldronRecipe(Identifier id, Ingredient ingredient, ItemStack result) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
    }
    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        return inventory.size() == 1 && ingredient.test(inventory.getStack(0));
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return result.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return width == 1 && height == 1;
    }

    @Override
    public ItemStack getOutput() {
        return result;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return DefaultedList.copyOf(Ingredient.EMPTY, ingredient);
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BLRecipeSerializers.BLOOD_CAULDRON_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return BLRecipeTypes.BLOOD_CAULDRON_TYPE;
    }

    public static class Serializer implements RecipeSerializer<BloodCauldronRecipe> {
        @Override
        public BloodCauldronRecipe read(Identifier id, JsonObject json) {
            if(!json.has("input"))
                throw new JsonParseException("Missing recipe input!");
            if(!json.has("result"))
                throw new JsonParseException("Missing recipe result!");
            Ingredient ingredient = Ingredient.fromJson(json.get("input"));
            ItemStack result = ShapedRecipe.outputFromJson(json.get("result").getAsJsonObject());
            return new BloodCauldronRecipe(id, ingredient, result);
        }

        @Override
        public BloodCauldronRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient ingredient = Ingredient.fromPacket(buf);
            ItemStack result = buf.readItemStack();
            return new BloodCauldronRecipe(id, ingredient, result);
        }

        @Override
        public void write(PacketByteBuf buf, BloodCauldronRecipe recipe) {
            recipe.ingredient.write(buf);
            buf.writeItemStack(recipe.result);
        }
    }
}
