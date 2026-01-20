package com.auroali.sanguinisluxuria.datagen.generators;

import com.auroali.sanguinisluxuria.datagen.builders.BloodDrainEffectBuilder;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Generates blood effect datapack entries
 *
 * @see BloodDrainEffectBuilder
 */
public abstract class SanguinisLuxuriaBloodEffectsProvider implements DataProvider {
    protected final FabricDataOutput output;
    protected final DataOutput.PathResolver pathResolver;

    protected SanguinisLuxuriaBloodEffectsProvider(FabricDataOutput output) {
        this.output = output;
        this.pathResolver = this.output.getResolver(DataOutput.OutputType.DATA_PACK, "blood_drain_effects");
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        HashMap<Identifier, BloodDrainEffectBuilder> builders = new HashMap<>();
        this.generateEffects((builder, id) -> {
            if (builders.containsKey(id))
                throw new IllegalStateException("Duplicated id " + id);
            builders.put(id, builder);
        });

        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (var builderEntry : builders.entrySet()) {
            JsonObject json = builderEntry.getValue().toJson();
            ConditionJsonProvider.write(json, FabricDataGenHelper.consumeConditions(builderEntry.getValue()));
            futures.add(DataProvider.writeToPath(writer, json, this.getOutputPath(builderEntry.getKey())));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    protected BloodEffectExporter withConditions(BloodEffectExporter exporter, ConditionJsonProvider... conditions) {
        Preconditions.checkArgument(conditions.length > 0, "Must add at least one condition.");
        return (builder, id) -> {
            FabricDataGenHelper.addConditions(builder, conditions);
            exporter.offer(builder, id);
        };
    }

    protected abstract void generateEffects(BloodEffectExporter exporter);

    protected Path getOutputPath(Identifier id) {
        return this.pathResolver.resolve(id, "json");
    }

    @Override
    public String getName() {
        return "Blood Drain Effects";
    }

    @FunctionalInterface
    public interface BloodEffectExporter {
        void offer(BloodDrainEffectBuilder builder, Identifier id);

        default void offer(BloodDrainEffectBuilder builder) {
            this.offer(builder, builder.getId());
        }
    }
}
