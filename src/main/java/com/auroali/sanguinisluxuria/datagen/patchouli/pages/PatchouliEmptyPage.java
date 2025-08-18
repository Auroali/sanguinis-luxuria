package com.auroali.sanguinisluxuria.datagen.patchouli.pages;

import com.auroali.sanguinisluxuria.datagen.patchouli.PatchouliJsonPage;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

public class PatchouliEmptyPage extends PatchouliJsonPage {
    private final boolean drawFiller;

    protected PatchouliEmptyPage(boolean drawFiller) {
        super(PatchouliJsonPage.EMPTY);
        this.drawFiller = drawFiller;
    }

    public PatchouliEmptyPage create() {
        return new PatchouliEmptyPage(true);
    }

    public PatchouliEmptyPage createWithoutFiller() {
        return new PatchouliEmptyPage(false);
    }

    @Override
    public void addAdditionalJson(JsonObject object) {
        object.addProperty("draw_filler", this.drawFiller);
    }
}
