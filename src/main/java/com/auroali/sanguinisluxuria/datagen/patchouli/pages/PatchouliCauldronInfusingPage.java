package com.auroali.sanguinisluxuria.datagen.patchouli.pages;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.datagen.patchouli.PatchouliJsonPage;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class PatchouliCauldronInfusingPage extends PatchouliJsonPage {
    private static final Identifier ID = SLResources.id("cauldron_infusing");

    private final Identifier recipe;

    protected PatchouliCauldronInfusingPage(Identifier recipe) {
        super(ID);
        this.recipe = recipe;
    }

    public static PatchouliCauldronInfusingPage create(Identifier recipe) {
        return new PatchouliCauldronInfusingPage(recipe);
    }

    public static PatchouliCauldronInfusingPage create(ItemConvertible item) {
        return create(Registries.ITEM.getId(item.asItem()));
    }

    @Override
    public void addAdditionalJson(JsonObject object) {
        object.addProperty("recipe", this.recipe.toString());
    }
}
