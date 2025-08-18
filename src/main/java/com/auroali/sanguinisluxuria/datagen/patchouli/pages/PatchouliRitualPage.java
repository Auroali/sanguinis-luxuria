package com.auroali.sanguinisluxuria.datagen.patchouli.pages;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.datagen.patchouli.PatchouliJsonPage;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class PatchouliRitualPage extends PatchouliJsonPage {
    private static final Identifier ID = SLResources.id("ritual");

    private final Identifier recipe;

    protected PatchouliRitualPage(Identifier recipe) {
        super(ID);
        this.recipe = recipe;
    }

    public static PatchouliRitualPage create(Identifier recipe) {
        return new PatchouliRitualPage(recipe);
    }

    public static PatchouliRitualPage create(ItemConvertible item) {
        return create(Registries.ITEM.getId(item.asItem()));
    }

    @Override
    public void addAdditionalJson(JsonObject object) {
        object.addProperty("recipe", this.recipe.toString());
    }
}
