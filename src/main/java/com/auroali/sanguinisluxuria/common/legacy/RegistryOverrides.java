package com.auroali.sanguinisluxuria.common.legacy;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Optional;

/**
 * Allows registering overrides for registries
 * <br>
 * This can be removed when we move to 1.21, due to fabric's registry aliasing API
 */
public class RegistryOverrides {
    private static final HashMap<RegistryKey<? extends Registry<?>>, HashMap<RegistryKey<?>, RegistryKey<?>>> KEY_OVERRIDES = new HashMap<>();
    private static final HashMap<RegistryKey<? extends Registry<?>>, HashMap<Identifier, Identifier>> ID_OVERRIDES = new HashMap<>();

    private static void registerInternal(RegistryKey<? extends Registry<?>> registry, RegistryKey<?> original, RegistryKey<?> replacement) {
        if (KEY_OVERRIDES.containsKey(registry) && KEY_OVERRIDES.get(registry).containsKey(original))
            throw new IllegalArgumentException("Attempting to register a replacement for " + original + " when one has already been registered");

        KEY_OVERRIDES.computeIfAbsent(registry, key -> new HashMap<>())
          .put(original, replacement);
        ID_OVERRIDES.computeIfAbsent(registry, key -> new HashMap<>())
          .put(original.getValue(), replacement.getValue());
    }

    public static <T> void register(Registry<T> registry, Identifier original, T replacement) {
        registry.getKey(replacement)
          .ifPresentOrElse(
            key -> registerInternal(
              registry.getKey(),
              RegistryKey.of(registry.getKey(), original),
              key
            ),
            () -> {
                throw new IllegalArgumentException("Replacement is not registered with " + registry.getKey());
            }
          );
    }

    public static Optional<RegistryKey<?>> getKeyOverride(Registry<?> registry, RegistryKey<?> key) {
        HashMap<RegistryKey<?>, RegistryKey<?>> replacements = KEY_OVERRIDES.get(registry.getKey());
        if (replacements == null)
            return Optional.empty();

        RegistryKey<?> replacement = replacements.get(key);
        return replacement == null ? Optional.empty() : Optional.of(replacement);
    }

    public static Optional<Identifier> getIdOverride(Registry<?> registry, Identifier key) {
        HashMap<Identifier, Identifier> replacements = ID_OVERRIDES.get(registry.getKey());
        if (replacements == null)
            return Optional.empty();

        Identifier replacement = replacements.get(key);
        return replacement == null ? Optional.empty() : Optional.of(replacement);
    }
}
