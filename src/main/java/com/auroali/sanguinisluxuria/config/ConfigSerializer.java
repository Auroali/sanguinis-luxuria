package com.auroali.sanguinisluxuria.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.function.Consumer;

public class ConfigSerializer {
    private final ConfigSerializer parent;
    private final JsonObject obj;
    boolean shouldResaveConfig;

    public ConfigSerializer(ConfigSerializer parent, JsonObject obj) {
        this.parent = parent;
        this.obj = obj;
    }

    public ConfigSerializer category(String name) {
        if(obj.has(name))
            return new ConfigSerializer(this, obj.getAsJsonObject(name));

        JsonObject object = new JsonObject();
        obj.add(name, object);
        return new ConfigSerializer(this, object);
    }

    public ConfigSerializer up() {
        return parent;
    }

    public <T> ConfigSerializer writeValue(String name, T value, Writer<T> writer) {
        writer.write(obj, name, value);
        return this;
    }

    public <T> ConfigSerializer readValue(String name, Consumer<T> valueWriter, T defaultVal, Reader<T> conversionFunc) {
        if(obj.has(name))
            valueWriter.accept(conversionFunc.read(obj.get(name)));
        else {
            ConfigSerializer root = getRoot();
            if(root != null)
                root.shouldResaveConfig = true;
            valueWriter.accept(defaultVal);
        }
        return this;
    }

    private ConfigSerializer getRoot() {
        ConfigSerializer root = parent;
        while (root != null) {
            if(root.parent == null)
                return root;
            root = root.parent;
        }
        return null;
    }

    public void saveIfNeeded(Runnable runnable) {
        if(shouldResaveConfig)
            runnable.run();
    }

    public static ConfigSerializer create(JsonObject root) {
        return new ConfigSerializer(null, root);
    }

    @FunctionalInterface
    public interface Writer<T> {
        void write(JsonObject object, String name, T value);
    }

    @FunctionalInterface
    public interface Reader<T> {
        T read(JsonElement object);
    }
}
