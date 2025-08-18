package com.auroali.sanguinisluxuria.datagen.patchouli;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.Optional;

public abstract class PatchouliJsonPage {
    public static final Identifier TEXT = new Identifier("patchouli", "text");
    public static final Identifier IMAGE = new Identifier("patchouli", "image");
    public static final Identifier CRAFTING = new Identifier("patchouli", "crafting");
    public static final Identifier SMELTING = new Identifier("patchouli", "smelting");
    public static final Identifier MULTIBLOCK = new Identifier("patchouli", "multiblock");
    public static final Identifier ENTITY = new Identifier("patchouli", "entity");
    public static final Identifier SPOTLIGHT = new Identifier("patchouli", "spotlight");
    public static final Identifier LINK = new Identifier("patchouli", "link");
    public static final Identifier RELATIONS = new Identifier("patchouli", "relations");
    public static final Identifier QUEST = new Identifier("patchouli", "quest");
    public static final Identifier EMPTY = new Identifier("patchouli", "empty");


    private final Identifier type;
    private Optional<Identifier> advancement;
    private Optional<String> flag;
    private Optional<String> anchor;

    protected PatchouliJsonPage(Identifier type) {
        this.type = type;
        this.advancement = Optional.empty();
        this.flag = Optional.empty();
        this.anchor = Optional.empty();
    }

    public PatchouliJsonPage advancement(Identifier id) {
        this.advancement = Optional.of(id);
        return this;
    }

    public PatchouliJsonPage flag(String flag) {
        this.flag = Optional.of(flag);
        return this;
    }

    public PatchouliJsonPage anchor(String anchor) {
        this.anchor = Optional.of(anchor);
        return this;
    }

    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", this.type.toString());
        this.advancement.ifPresent(id -> object.addProperty("advancement", id.toString()));
        this.flag.ifPresent(flag -> object.addProperty("flag", flag));
        this.anchor.ifPresent(anchor -> object.addProperty("anchor", anchor));
        this.addAdditionalJson(object);
        return object;
    }

    public abstract void addAdditionalJson(JsonObject object);
}
