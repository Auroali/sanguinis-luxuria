package com.auroali.sanguinisluxuria.datagen.patchouli.pages;

import com.auroali.sanguinisluxuria.datagen.patchouli.PatchouliJsonIcon;
import com.auroali.sanguinisluxuria.datagen.patchouli.PatchouliJsonPage;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class PatchouliSpotlightPage extends PatchouliTextTitlePage<PatchouliSpotlightPage> {
    private final ItemStack stack;
    private boolean linkRecipe;

    protected PatchouliSpotlightPage(ItemStack stack) {
        super(PatchouliJsonPage.SPOTLIGHT);
        this.stack = stack;
    }

    public static PatchouliSpotlightPage create(ItemStack stack) {
        return new PatchouliSpotlightPage(stack);
    }

    public static PatchouliSpotlightPage create(ItemConvertible item) {
        return create(new ItemStack(item.asItem()));
    }

    public PatchouliSpotlightPage linkRecipe() {
        this.linkRecipe = true;
        return this;
    }

    @Override
    public void addAdditionalJson(JsonObject object) {
        super.addAdditionalJson(object);
        object.addProperty("item", PatchouliJsonIcon.toStackString(this.stack));
        object.addProperty("link_recipe", this.linkRecipe);
    }
}
