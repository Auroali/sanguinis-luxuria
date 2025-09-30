package com.auroali.sanguinisluxuria.common.recipes;

import com.auroali.sanguinisluxuria.common.registry.SLRecipeSerializers;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class BloodCauldronCopyRecipe extends BloodCauldronRecipe {
    private final List<String> copiedKeys;

    public BloodCauldronCopyRecipe(Identifier id, int level, Ingredient ingredient, ItemStack result, List<String> copiedKeys) {
        super(id, level, ingredient, result);
        this.copiedKeys = copiedKeys;
    }

    private NbtList detextify(NbtList in) {
        NbtList detextified = new NbtList();
        in.forEach(e -> {
            Text page = Text.Serializer.fromJson(e.asString());
            detextified.add(NbtString.of(page == null ? "" : page.getString()));
        });
        return detextified;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack output = super.craft(inventory, registryManager);
        ItemStack input = inventory.getStack(0);
        if (!input.hasNbt())
            return output;

        NbtCompound inNbt = input.getNbt();
        NbtCompound outNbt = new NbtCompound();
        for (String key : this.copiedKeys) {
            if (!inNbt.contains(key))
                continue;
            // todo: dont hardcode
            if (key.equals(WrittenBookItem.PAGES_KEY) && output.isOf(Items.WRITABLE_BOOK) && input.isOf(Items.WRITTEN_BOOK))
                outNbt.put(key, this.detextify(inNbt.getList(key, NbtElement.STRING_TYPE)));
            else outNbt.put(key, inNbt.get(key));
        }

        if (!outNbt.isEmpty())
            output.setNbt(outNbt);

        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SLRecipeSerializers.BLOOD_CAULDRON_COPY_SERIALIZER;
    }

    public static class Serializer implements RecipeSerializer<BloodCauldronCopyRecipe> {
        @Override
        public BloodCauldronCopyRecipe read(Identifier id, JsonObject json) {
            if (!json.has("input"))
                throw new JsonParseException("Missing recipe input!");
            if (!json.has("result"))
                throw new JsonParseException("Missing recipe result!");
            int level = 1;
            if (json.has("level"))
                level = json.get("level").getAsInt();

            List<String> copiedKeys = new ArrayList<>();
            if (json.has("copied")) {
                for (JsonElement element : json.getAsJsonArray("copied")) {
                    if (!JsonHelper.isString(element))
                        throw new JsonParseException("Invalid copied entry: must be a string");

                    copiedKeys.add(element.getAsString());
                }
            }
            Ingredient ingredient = Ingredient.fromJson(json.get("input"));
            ItemStack result = ShapedRecipe.outputFromJson(json.get("result").getAsJsonObject());
            return new BloodCauldronCopyRecipe(id, level, ingredient, result, copiedKeys);
        }

        @Override
        public BloodCauldronCopyRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient ingredient = Ingredient.fromPacket(buf);
            int level = buf.readVarInt();
            ItemStack result = buf.readItemStack();
            List<String> copiedKeys = buf.readList(PacketByteBuf::readString);
            return new BloodCauldronCopyRecipe(id, level, ingredient, result, copiedKeys);
        }

        @Override
        public void write(PacketByteBuf buf, BloodCauldronCopyRecipe recipe) {
            recipe.ingredient.write(buf);
            buf.writeVarInt(recipe.getCauldronLevel());
            buf.writeItemStack(recipe.result);
            buf.writeCollection(recipe.copiedKeys, PacketByteBuf::writeString);
        }
    }
}
