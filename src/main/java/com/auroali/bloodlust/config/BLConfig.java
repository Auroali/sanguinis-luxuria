package com.auroali.bloodlust.config;

import com.auroali.bloodlust.Bloodlust;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.string.number.FloatFieldController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BLConfig {
    public static BLConfig INSTANCE = new BLConfig();

    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("bloodlust.json");
    private static final Gson GSON = new Gson()
            .newBuilder()
            .setPrettyPrinting()
            .create();

    public float vampireDamageMultiplier = 2.5f;
    public float vampireExhaustionMultiplier = 0.25f;

    public Screen generateScreen(Screen screen) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("bloodlust.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("bloodlust.config.category.gameplay"))
                        .group(OptionGroup.createBuilder()
                                .option(Option.createBuilder(Float.class)
                                        .name(Text.translatable("bloodlust.config.option.vampire_damage_multiplier"))
                                        .tooltip(Text.translatable("bloodlust.config.option.vampire_damage_multiplier.desc"))
                                        .binding(2.5f, () -> this.vampireDamageMultiplier, f -> this.vampireDamageMultiplier = f)
                                        .controller(FloatFieldController::new)
                                        .build()
                                ).option(Option.createBuilder(Float.class)
                                        .name(Text.translatable("bloodlust.config.option.vampire_exhaustion_multiplier"))
                                        .tooltip(Text.translatable("bloodlust.config.option.vampire_exhaustion_multiplier.desc"))
                                        .binding(0.25f, () -> this.vampireExhaustionMultiplier, f -> this.vampireExhaustionMultiplier = f)
                                        .controller(FloatFieldController::new)
                                        .build()
                                ).build()
                        ).build()
                )
                .save(INSTANCE::save)
                .build()
                .generateScreen(screen);
    }

    public void save() {
        JsonObject root = new JsonObject();
        root.addProperty("vampireDamageMultiplier", vampireDamageMultiplier);
        root.addProperty("vampireExhaustionMultiplier", vampireExhaustionMultiplier);

        try {
            Files.writeString(CONFIG_FILE, GSON.toJson(root));
        } catch (IOException e) {
            Bloodlust.LOGGER.warn("An error occurred whilst saving the config file!", e);
        }
    }

    public void load() {
        if (!Files.exists(CONFIG_FILE)) {
            save();
            return;
        }
        JsonObject root;
        try {
            root = GSON.fromJson(Files.readString(CONFIG_FILE), JsonObject.class);
        } catch (IOException | JsonSyntaxException e) {
            Bloodlust.LOGGER.warn("Failed to load config file!", e);
            return;
        }

        boolean isMissingEntry = false;

        if(root.has("vampireDamageMultiplier"))
            vampireDamageMultiplier = root.get("vampireDamageMultiplier").getAsFloat();
        else isMissingEntry = true;

        if(root.has("vampireExhaustionMultiplier"))
            vampireExhaustionMultiplier = root.get("vampireExhaustionMultiplier").getAsFloat();
        else isMissingEntry = true;

        if(isMissingEntry)
            save();
    }

}
