package com.auroali.sanguinisluxuria.datagen.patchouli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IVariable;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PatchouliJsonBook {
    private final Identifier id;
    private final String name;
    private final String landing;
    private final String version;
    private Optional<RegistryKey<ItemGroup>> group;
    private Optional<Identifier> texture;
    private Optional<Identifier> model;
    private Optional<Color> textColor;
    private Optional<Color> linkColor;
    private Optional<Color> linkHoverColor;
    private Optional<Color> nameplateColor;
    private Optional<Color> progressBarColor;
    private Optional<Color> progressBarBackground;
    private Optional<Identifier> openSound;
    private Optional<Identifier> flipSound;
    private Optional<PatchouliJsonIcon> indexIcon;
    private HashMap<String, String> macros;

    private Set<LangPack> langPacks;

    public PatchouliJsonBook(Identifier id, String name, String landing, String version) {
        this.id = id;
        this.name = name;
        this.landing = landing;
        this.version = version;

        this.group = Optional.empty();
        this.texture = Optional.empty();
        this.model = Optional.empty();
        this.textColor = Optional.empty();
        this.linkColor = Optional.empty();
        this.linkHoverColor = Optional.empty();
        this.nameplateColor = Optional.empty();
        this.progressBarColor = Optional.empty();
        this.progressBarBackground = Optional.empty();
        this.openSound = Optional.empty();
        this.flipSound = Optional.empty();
        this.indexIcon = Optional.empty();

        this.langPacks = new HashSet<>();
        this.macros = new HashMap<>();
    }

    public PatchouliJsonBook group(RegistryKey<ItemGroup> key) {
        this.group = Optional.of(key);
        return this;
    }

    public PatchouliJsonBook texture(Identifier texture) {
        this.texture = Optional.of(texture);
        return this;
    }

    public PatchouliJsonBook model(Identifier model) {
        this.model = Optional.of(model);
        return this;
    }

    public PatchouliJsonBook textColor(Color color) {
        this.textColor = Optional.of(color);
        return this;
    }

    public PatchouliJsonBook linkColor(Color color) {
        this.linkColor = Optional.of(color);
        return this;
    }

    public PatchouliJsonBook linkHoverColor(Color color) {
        this.linkHoverColor = Optional.of(color);
        return this;
    }

    public PatchouliJsonBook nameplateColor(Color color) {
        this.nameplateColor = Optional.of(color);
        return this;
    }

    public PatchouliJsonBook progressBarColor(Color color) {
        this.progressBarColor = Optional.of(color);
        return this;
    }

    public PatchouliJsonBook progressBarBackgroundColor(Color color) {
        this.progressBarBackground = Optional.of(color);
        return this;
    }

    public PatchouliJsonBook openSound(Identifier sound) {
        this.openSound = Optional.of(sound);
        return this;
    }

    public PatchouliJsonBook flipSound(Identifier sound) {
        this.flipSound = Optional.of(sound);
        return this;
    }

    public PatchouliJsonBook indexIcon(Identifier texture) {
        this.indexIcon = Optional.of(PatchouliJsonIcon.texture(texture));
        return this;
    }

    public PatchouliJsonBook indexIcon(ItemStack stack) {
        this.indexIcon = Optional.of(PatchouliJsonIcon.stack(stack));
        return this;
    }

    public PatchouliJsonBook indexIcon(ItemConvertible item) {
        this.indexIcon = Optional.of(PatchouliJsonIcon.stack(new ItemStack(item)));
        return this;
    }

    public PatchouliJsonBook macro(String key, String macro) {
        this.macros.put(key, macro);
        return this;
    }

    public Identifier getId() {
        return this.id;
    }

    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("name", this.name);
        object.addProperty("landing_text", this.landing);
        object.addProperty("version", this.version);
        object.addProperty("use_resource_pack", true);
        this.textColor.ifPresent(color -> object.addProperty("text_color", String.format("%06x", color.getRGB() & 0xFFFFFF)));
        this.linkColor.ifPresent(color -> object.addProperty("link_color", String.format("%06x", color.getRGB() & 0xFFFFFF)));
        this.linkHoverColor.ifPresent(color -> object.addProperty("link_hover_color", String.format("%06x", color.getRGB() & 0xFFFFFF)));
        this.nameplateColor.ifPresent(color -> object.addProperty("nameplate_color", String.format("%06x", color.getRGB() & 0xFFFFFF)));
        this.progressBarColor.ifPresent(color -> object.addProperty("progress_bar_color", String.format("%06x", color.getRGB() & 0xFFFFFF)));
        this.progressBarBackground.ifPresent(color -> object.addProperty("progress_bar_background", String.format("%06x", color.getRGB() & 0xFFFFFF)));
        this.group.ifPresent(key -> object.addProperty("creative_tab", key.getValue().toString()));
        this.texture.ifPresent(id -> object.addProperty("book_texture", id.toString()));
        this.model.ifPresent(id -> object.addProperty("model", id.toString()));
        this.openSound.ifPresent(id -> object.addProperty("open_sound", id.toString()));
        this.flipSound.ifPresent(id -> object.addProperty("flip_sound", id.toString()));
        this.indexIcon.ifPresent(icon -> object.addProperty("index_icon", icon.toString()));
        if (!this.macros.isEmpty()) {
            JsonObject macros = new JsonObject();
            this.macros.forEach(macros::addProperty);
            object.add("macros", macros);
        }
        return object;
    }

    public LangPack createLang(String lang) {
        LangPack pack = new LangPack(lang);
        if (this.langPacks.contains(pack))
            throw new IllegalArgumentException(lang + " already has a language pack");
        this.langPacks.add(pack);
        return pack;
    }

    protected Set<LangPack> getLangPacks() {
        return this.langPacks;
    }

    public static class LangPack {
        private String lang;
        private List<PatchouliJsonCategory> categories;

        protected LangPack(String lang) {
            this.lang = lang;
            this.categories = new ArrayList<>();
        }

        public String getLang() {
            return this.lang;
        }

        public List<PatchouliJsonCategory> getCategories() {
            return this.categories;
        }

        public PatchouliJsonCategory category(PatchouliJsonCategory category) {
            this.categories.add(category);
            return category;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof LangPack pack)
                return pack.lang.equals(this.lang);
            return false;
        }

        @Override
        public int hashCode() {
            return this.lang.hashCode();
        }
    }
}
