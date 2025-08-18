package com.auroali.sanguinisluxuria.datagen.patchouli.pages;

import com.auroali.sanguinisluxuria.datagen.patchouli.PatchouliJsonPage;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.Optional;

public abstract class PatchouliTextTitlePage<T extends PatchouliTextTitlePage<?>> extends PatchouliJsonPage {
    protected Optional<String> text;
    protected Optional<String> title;

    protected PatchouliTextTitlePage(Identifier type) {
        super(type);
        this.text = Optional.empty();
        this.title = Optional.empty();
    }

    public T text(String text) {
        if (this.text.isPresent()) {
            this.text = Optional.of(this.text.get() + "$(br2)" + text);
            return (T) this;
        }
        this.text = Optional.of(text);
        return (T) this;
    }

    public T text(String... text) {
        return this.text(String.join("$(br)", text));
    }

    public T title(String title) {
        this.title = Optional.of(title);
        return (T) this;
    }

    @Override
    public void addAdditionalJson(JsonObject object) {
        this.title.ifPresent(str -> object.addProperty("title", str));
        this.text.ifPresent(str -> object.addProperty("text", str));
    }
}
