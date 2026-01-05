package com.auroali.sanguinisluxuria.datagen.generators;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public abstract class SanguinusLuxuriaRegistryCodecProvider<T> extends FabricCodecDataProvider<T> {
    private final RegistryKey<Registry<T>> registryKey;

    protected SanguinusLuxuriaRegistryCodecProvider(FabricDataOutput dataOutput, RegistryKey<Registry<T>> registry, Codec<T> codec) {
        super(dataOutput, DataOutput.OutputType.DATA_PACK, registry.getValue().getPath(), codec);
        this.registryKey = registry;
    }

    @Override
    protected void configure(BiConsumer<Identifier, T> provider) {
        this.generate((v, id) -> provider.accept(id, v));
    }

    protected abstract void generate(Exporter<T> exporter);

    @Override
    public String getName() {
        return this.registryKey.getValue().toString();
    }

    @FunctionalInterface
    public interface Exporter<T> {
        void export(T value, Identifier id);

        default void export(T value, RegistryKey<T> key) {
            this.export(value, key.getValue());
        }
    }
}
