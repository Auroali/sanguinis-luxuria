package com.auroali.sanguinisluxuria.datagen.patchouli.pages;

import com.auroali.sanguinisluxuria.datagen.patchouli.PatchouliJsonIcon;
import com.auroali.sanguinisluxuria.datagen.patchouli.PatchouliJsonPage;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

public class PatchouliSpotlightPage extends PatchouliTextTitlePage<PatchouliSpotlightPage> {
    private final PatchouliJsonIcon stack;
    private boolean linkRecipe;

    protected PatchouliSpotlightPage(PatchouliJsonIcon stack) {
        super(PatchouliJsonPage.SPOTLIGHT);
        if (stack.type() != PatchouliJsonIcon.Type.STACK)
            throw new IllegalArgumentException("cannot use texture icon for spotlight page");
        this.stack = stack;
    }

    public static PatchouliSpotlightPage create(ItemStack stack) {
        return new PatchouliSpotlightPage(PatchouliJsonIcon.stack(stack));
    }

    public static PatchouliSpotlightPage create(ItemConvertible item) {
        return new PatchouliSpotlightPage(PatchouliJsonIcon.item(item));
    }

    public static PatchouliSpotlightPage create(ItemStack... stack) {
        return new PatchouliSpotlightPage(PatchouliJsonIcon.stacks(stack));
    }

    public static PatchouliSpotlightPage create(ItemConvertible... item) {
        return new PatchouliSpotlightPage(PatchouliJsonIcon.items(item));
    }

    public static PatchouliSpotlightPage create(TagKey<Item> tag) {
        return new PatchouliSpotlightPage(PatchouliJsonIcon.tag(tag));
    }

    public PatchouliSpotlightPage linkRecipe() {
        this.linkRecipe = true;
        return this;
    }

    @Override
    public void addAdditionalJson(JsonObject object) {
        super.addAdditionalJson(object);
        object.addProperty("item", this.stack.toString());
        object.addProperty("link_recipe", this.linkRecipe);
    }
}
