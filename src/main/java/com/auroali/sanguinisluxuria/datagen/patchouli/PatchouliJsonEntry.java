package com.auroali.sanguinisluxuria.datagen.patchouli;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class PatchouliJsonEntry {
    private final PatchouliJsonCategory category;
    private final String id;
    private final String name;
    private final PatchouliJsonIcon icon;
    private final List<PatchouliJsonPage> pages;
    private boolean secret;
    private boolean priority;
    private boolean readByDefault;
    private Optional<Identifier> advancement;
    private Optional<Identifier> turnIn;
    private Optional<PatchouliColor> entryColor;
    private Optional<Identifier> parent;
    private Optional<String> flag;
    private OptionalInt sortNumber;

    protected PatchouliJsonEntry(PatchouliJsonCategory category, String id, String name, PatchouliJsonIcon icon) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.pages = new ArrayList<>();

        this.advancement = Optional.empty();
        this.turnIn = Optional.empty();
        this.entryColor = Optional.empty();
        this.parent = Optional.empty();
        this.flag = Optional.empty();
        this.sortNumber = OptionalInt.empty();
    }

    public PatchouliJsonEntry page(PatchouliJsonPage page) {
        this.pages.add(page);
        return this;
    }

    public PatchouliJsonEntry parent(Identifier parent) {
        this.parent = Optional.of(parent);
        return this;
    }

    public PatchouliJsonEntry secret() {
        this.secret = true;
        return this;
    }

    public PatchouliJsonEntry priority() {
        this.priority = true;
        return this;
    }

    public PatchouliJsonEntry readByDefault() {
        this.readByDefault = true;
        return this;
    }

    public PatchouliJsonEntry sort(int sortnum) {
        this.sortNumber = OptionalInt.of(sortnum);
        return this;
    }

    public PatchouliJsonEntry flag(String flag) {
        this.flag = Optional.of(flag);
        return this;
    }

    public PatchouliJsonEntry advancement(Identifier id) {
        this.advancement = Optional.of(id);
        return this;
    }

    public PatchouliJsonEntry turnIn(Identifier id) {
        this.turnIn = Optional.of(id);
        return this;
    }

    public PatchouliJsonEntry color(PatchouliColor color) {
        this.entryColor = Optional.of(color);
        return this;
    }

    public String getId() {
        return this.id;
    }

    public JsonElement toJson(String namespace) {
        JsonObject object = new JsonObject();
        object.addProperty("category", namespace + ":" + this.category.getId().toString());
        object.addProperty("name", this.name);
        object.addProperty("icon", this.icon.toString());
        object.addProperty("secret", this.secret);
        object.addProperty("priority", this.priority);
        object.addProperty("read_by_default", this.readByDefault);

        JsonArray pagesJson = new JsonArray();
        this.pages.forEach(page -> pagesJson.add(page.toJson()));
        object.add("pages", pagesJson);

        this.advancement.ifPresent(id -> object.addProperty("advancement", id.toString()));
        this.turnIn.ifPresent(id -> object.addProperty("turnin", id.toString()));
        this.parent.ifPresent(id -> object.addProperty("parent", id.toString()));
        this.flag.ifPresent(flag -> object.addProperty("flag", flag));
        this.sortNumber.ifPresent(sort -> object.addProperty("sortnum", sort));
        this.entryColor.ifPresent(color -> object.addProperty("entry_color", color.toString()));
        return object;
    }
}
