package com.auroali.bloodlust.common.recipes;

import com.auroali.bloodlust.common.registry.BLRecipeSerializers;
import com.auroali.bloodlust.common.registry.BLRecipeTypes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AltarRecipe implements Recipe<AltarInventory> {
    private final Identifier id;
    private final String group;
    private final ItemStack output;
    private final int minLevel;
    private final int maxLevel;
    private final DefaultedList<Ingredient> ingredients;
    private final int ticksToProcess;

    public AltarRecipe(Identifier id, ItemStack output, String group, int minLevel, int maxLevel, int ticksToProcess, DefaultedList<Ingredient> ingredientList) {
        this.id = id;
        this.output = output;
        this.group = group;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.ingredients = ingredientList;
        this.ticksToProcess = ticksToProcess;
    }

    @Override
    public boolean matches(AltarInventory inventory, World world) {
        return false;
    }

    @Override
    public ItemStack craft(AltarInventory inventory) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getOutput() {
        return output;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeType<?> getType() {
        return BLRecipeTypes.ALTAR_RECIPE;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BLRecipeSerializers.ALTAR_RECIPE_SERIALIZER;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public boolean matches(int level, Collection<ItemStack> input) {
        List<ItemStack> stacks = new ArrayList<>(input);
        boolean matches = false;
        for(Ingredient i : ingredients) {
            for(ItemStack stack : stacks) {
                if(i.test(stack)) {
                    stacks.remove(stack);
                    matches = true;
                    break;
                }
                matches = false;
            }
        }

        return matches;
    }

    public int getProcessingTicks() {
        return ticksToProcess;
    }

    public static class Serializer implements RecipeSerializer<AltarRecipe> {

        @Override
        public AltarRecipe read(Identifier id, JsonObject json) {
            int minLevel = json.get("minLevel").getAsInt();
            int maxLevel = Integer.MAX_VALUE;
            String string = JsonHelper.getString(json, "group", "");
            int ticksToProcess = JsonHelper.getInt(json, "ticks", 300);
            if(json.has("maxLevel"))
                maxLevel = json.get("maxLevel").getAsInt();

            DefaultedList<Ingredient> ingredients = getIngredients(JsonHelper.getArray(json, "input"));
            if(ingredients.isEmpty())
                throw new JsonParseException("No ingredients for recipe");

            ItemStack itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
            return new AltarRecipe(id, itemStack, string, minLevel, maxLevel, ticksToProcess, ingredients);
        }

        private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
            DefaultedList<Ingredient> defaultedList = DefaultedList.of();

            for(int i = 0; i < json.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i));
                if (!ingredient.isEmpty()) {
                    defaultedList.add(ingredient);
                }
            }

            return defaultedList;
        }

        public AltarRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            int minLevel = packetByteBuf.readVarInt();
            int maxLevel = packetByteBuf.readVarInt();
            int ticksToProcess = packetByteBuf.readVarInt();
            int i = packetByteBuf.readVarInt();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);

            defaultedList.replaceAll(ignored -> Ingredient.fromPacket(packetByteBuf));

            ItemStack itemStack = packetByteBuf.readItemStack();
            return new AltarRecipe(identifier, itemStack, string, minLevel, maxLevel, ticksToProcess, defaultedList);
        }

        public void write(PacketByteBuf packetByteBuf, AltarRecipe altarRecipe) {
            packetByteBuf.writeString(altarRecipe.group);
            packetByteBuf.writeVarInt(altarRecipe.minLevel);
            packetByteBuf.writeVarInt(altarRecipe.maxLevel);
            packetByteBuf.writeVarInt(altarRecipe.ticksToProcess);
            packetByteBuf.writeVarInt(altarRecipe.ingredients.size());

            for(Ingredient ingredient : altarRecipe.ingredients) {
                ingredient.write(packetByteBuf);
            }

            packetByteBuf.writeItemStack(altarRecipe.output);
        }
    }
}
