package com.auroali.sanguinisluxuria.config;

import com.auroali.configserializer.ConfigSerializer;
import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SLClientConfig {
    public static final SLClientConfig INSTANCE = new SLClientConfig();

    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("sanguinisluxuria-client.json");
    private static final Gson GSON = new Gson()
      .newBuilder()
      .setPrettyPrinting()
      .create();

    public boolean useCustomHungerEffect = true;

    public void save() {
        JsonObject root = new JsonObject();
        ConfigSerializer.create(root)
          .category("effects")
          .writeValue("useCustomHungerEffect", this.useCustomHungerEffect, JsonObject::addProperty)
          .up();

        try {
            Files.writeString(CONFIG_FILE, GSON.toJson(root));
        } catch (IOException e) {
            SanguinisLuxuria.LOGGER.warn("An error occurred whilst saving the config file!", e);
        }
    }

    public void load() {
        if (!Files.exists(CONFIG_FILE)) {
            this.save();
            return;
        }
        JsonObject root;
        try {
            root = GSON.fromJson(Files.readString(CONFIG_FILE), JsonObject.class);
        } catch (IOException | JsonSyntaxException e) {
            SanguinisLuxuria.LOGGER.warn("Failed to load config file!", e);
            return;
        }

        ConfigSerializer.create(root)
          .category("effects")
          .readValue("useCustomHungerEffect", v -> this.useCustomHungerEffect = v, this.useCustomHungerEffect, JsonElement::getAsBoolean)
          .up()
          .saveIfNeeded(this::save);
    }

}
