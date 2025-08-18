package com.auroali.sanguinisluxuria.datagen.patchouli.pages;

import com.auroali.sanguinisluxuria.datagen.patchouli.PatchouliJsonPage;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class PatchouliCraftingPage extends PatchouliTextTitlePage<PatchouliCraftingPage> {
    private final Identifier recipe1;
    private final Identifier recipe2;

    protected PatchouliCraftingPage(Identifier recipe1, Identifier recipe2) {
        super(PatchouliJsonPage.CRAFTING);
        this.recipe1 = recipe1;
        this.recipe2 = recipe2;
    }

    public static PatchouliCraftingPage create(Identifier recipe, Identifier recipe2) {
        return new PatchouliCraftingPage(recipe, recipe2);
    }

    public static PatchouliCraftingPage create(Identifier recipe) {
        return create(recipe, null);
    }

    public static PatchouliCraftingPage create(ItemConvertible item) {
        return create(Registries.ITEM.getId(item.asItem()), null);
    }

    public static PatchouliCraftingPage create(ItemConvertible item, ItemConvertible item2) {
        return create(Registries.ITEM.getId(item.asItem()), Registries.ITEM.getId(item2.asItem()));
    }

    @Override
    public void addAdditionalJson(JsonObject object) {
        super.addAdditionalJson(object);
        object.addProperty("recipe", this.recipe1.toString());
        if (this.recipe2 != null)
            object.addProperty("recipe2", this.recipe2.toString());
    }
}
