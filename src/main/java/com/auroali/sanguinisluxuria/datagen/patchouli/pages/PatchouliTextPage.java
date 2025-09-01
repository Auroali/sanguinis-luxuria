package com.auroali.sanguinisluxuria.datagen.patchouli.pages;

import com.auroali.sanguinisluxuria.datagen.patchouli.PatchouliJsonPage;
import com.google.gson.JsonObject;

public class PatchouliTextPage extends PatchouliTextTitlePage<PatchouliTextPage> {
    protected PatchouliTextPage() {
        super(PatchouliJsonPage.TEXT);
    }

    public static PatchouliTextPage create(String title, String text) {
        return new PatchouliTextPage().text(text).title(title);
    }

    public static PatchouliTextPage create(String text) {
        return new PatchouliTextPage().text(text);
    }

    public static PatchouliTextPage create() {
        return new PatchouliTextPage();
    }

    @Override
    public void addAdditionalJson(JsonObject object) {
        if (this.text.isEmpty())
            throw new IllegalArgumentException("PatchouliTextPage must have a text field");
        super.addAdditionalJson(object);
    }
}
