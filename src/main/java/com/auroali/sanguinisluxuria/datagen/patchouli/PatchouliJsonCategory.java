package com.auroali.sanguinisluxuria.datagen.patchouli;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class PatchouliJsonCategory {
    private final String id;
    private final String name;
    private final String description;
    private final PatchouliJsonIcon icon;
    private boolean secret;
    private Optional<Identifier> parent;
    private Optional<String> flag;
    private OptionalInt sortNumber;

    private List<PatchouliJsonEntry> entries;

    protected PatchouliJsonCategory(String id, String name, String description, PatchouliJsonIcon icon) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.parent = Optional.empty();
        this.flag = Optional.empty();
        this.sortNumber = OptionalInt.empty();

        this.entries = new ArrayList<>();
    }

    public static PatchouliJsonCategory create(String id, String name, String description, ItemStack icon) {
        return new PatchouliJsonCategory(id, name, description, PatchouliJsonIcon.stack(icon));
    }

    public static PatchouliJsonCategory create(String id, String name, String description, Identifier icon) {
        return new PatchouliJsonCategory(id, name, description, PatchouliJsonIcon.texture(icon));
    }

    public static PatchouliJsonCategory create(String id, String name, String description, ItemConvertible icon) {
        return create(id, name, description, new ItemStack(icon));
    }

    public PatchouliJsonCategory parent(Identifier parent) {
        this.parent = Optional.of(parent);
        return this;
    }

    public PatchouliJsonCategory secret() {
        this.secret = true;
        return this;
    }

    public PatchouliJsonCategory sort(int sortnum) {
        this.sortNumber = OptionalInt.of(sortnum);
        return this;
    }

    public PatchouliJsonCategory flag(String flag) {
        this.flag = Optional.of(flag);
        return this;
    }

    public String getId() {
        return this.id;
    }

    public PatchouliJsonEntry entry(String id, String name, ItemStack icon) {
        PatchouliJsonEntry entry = new PatchouliJsonEntry(this, id, name, PatchouliJsonIcon.stack(icon));
        this.entries.add(entry);
        return entry;
    }

    public PatchouliJsonEntry entry(String id, String name, Identifier icon) {
        PatchouliJsonEntry entry = new PatchouliJsonEntry(this, id, name, PatchouliJsonIcon.texture(icon));
        this.entries.add(entry);
        return entry;
    }

    public PatchouliJsonEntry entry(String id, String name, ItemConvertible icon) {
        PatchouliJsonEntry entry = new PatchouliJsonEntry(this, id, name, PatchouliJsonIcon.stack(new ItemStack(icon)));
        this.entries.add(entry);
        return entry;
    }

    public List<PatchouliJsonEntry> getEntries() {
        return this.entries;
    }

    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("name", this.name);
        object.addProperty("description", this.description);
        object.addProperty("icon", this.icon.toString());
        object.addProperty("secret", this.secret);
        this.parent.ifPresent(id -> object.addProperty("parent", id.toString()));
        this.flag.ifPresent(flag -> object.addProperty("flag", flag));
        this.sortNumber.ifPresent(sort -> object.addProperty("sortnum", sort));
        return object;
    }
}
