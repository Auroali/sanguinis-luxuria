package com.auroali.sanguinisluxuria.datagen.patchouli;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.*;

public class PatchouliJsonBook {
    public static final Identifier GUI_BOOK_BLUE = new Identifier("patchouli", "textures/gui/book_blue.png");
    public static final Identifier GUI_BOOK_BROWN = new Identifier("patchouli", "textures/gui/book_brown.png");
    public static final Identifier GUI_BOOK_CYAN = new Identifier("patchouli", "textures/gui/book_cyan.png");
    public static final Identifier GUI_BOOK_GRAY = new Identifier("patchouli", "textures/gui/book_gray.png");
    public static final Identifier GUI_BOOK_GREEN = new Identifier("patchouli", "textures/gui/book_green.png");
    public static final Identifier GUI_BOOK_PURPLE = new Identifier("patchouli", "textures/gui/book_purple.png");
    public static final Identifier GUI_BOOK_RED = new Identifier("patchouli", "textures/gui/book_red.png");
    public static final Identifier MODEL_BOOK_BLUE = new Identifier("patchouli", "item/book_blue");
    public static final Identifier MODEL_BOOK_BROWN = new Identifier("patchouli", "item/book_brown");
    public static final Identifier MODEL_BOOK_CYAN = new Identifier("patchouli", "item/book_cyan");
    public static final Identifier MODEL_BOOK_GRAY = new Identifier("patchouli", "item/book_gray");
    public static final Identifier MODEL_BOOK_GREEN = new Identifier("patchouli", "item/book_green");
    public static final Identifier MODEL_BOOK_PURPLE = new Identifier("patchouli", "item/book_purple");
    public static final Identifier MODEL_BOOK_RED = new Identifier("patchouli", "item/book_red");
    public static final Identifier MODEL_GUIDE_BOOK = new Identifier("patchouli", "item/guide_book");

    private final Identifier id;
    private final String name;
    private final String landing;
    private final String version;
    private Optional<RegistryKey<ItemGroup>> group;
    private Optional<Identifier> texture;
    private Optional<Identifier> model;
    private Optional<PatchouliColor> textColor;
    private Optional<PatchouliColor> linkColor;
    private Optional<PatchouliColor> linkHoverColor;
    private Optional<PatchouliColor> nameplateColor;
    private Optional<PatchouliColor> progressBarColor;
    private Optional<PatchouliColor> progressBarBackground;
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

    public PatchouliJsonBook textColor(PatchouliColor color) {
        this.textColor = Optional.of(color);
        return this;
    }

    public PatchouliJsonBook linkColor(PatchouliColor color) {
        this.linkColor = Optional.of(color);
        return this;
    }

    public PatchouliJsonBook linkHoverColor(PatchouliColor color) {
        this.linkHoverColor = Optional.of(color);
        return this;
    }

    public PatchouliJsonBook nameplateColor(PatchouliColor color) {
        this.nameplateColor = Optional.of(color);
        return this;
    }

    public PatchouliJsonBook progressBarColor(PatchouliColor color) {
        this.progressBarColor = Optional.of(color);
        return this;
    }

    public PatchouliJsonBook progressBarBackgroundColor(PatchouliColor color) {
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
        this.textColor.ifPresent(color -> object.addProperty("text_color", color.toString()));
        this.linkColor.ifPresent(color -> object.addProperty("link_color", color.toString()));
        this.linkHoverColor.ifPresent(color -> object.addProperty("link_hover_color", color.toString()));
        this.nameplateColor.ifPresent(color -> object.addProperty("nameplate_color", color.toString()));
        this.progressBarColor.ifPresent(color -> object.addProperty("progress_bar_color", color.toString()));
        this.progressBarBackground.ifPresent(color -> object.addProperty("progress_bar_background", color.toString()));
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
