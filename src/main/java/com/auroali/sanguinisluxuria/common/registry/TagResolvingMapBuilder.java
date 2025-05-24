package com.auroali.sanguinisluxuria.common.registry;

import com.mojang.datafixers.util.Either;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TagResolvingMapBuilder<T, V> {
    private final Merger<V> merger;
    private final Supplier<Map<T, V>> mapSupplier;
    private final List<Entry<T, V>> entries;

    public TagResolvingMapBuilder(Supplier<Map<T, V>> mapSupplier, Merger<V> merger) {
        this.merger = merger;
        this.mapSupplier = mapSupplier;
        this.entries = new ArrayList<>();
    }

    public void add(T key, V val) {
        this.entries.add(new Entry<>(Either.right(key), val));
    }

    public void add(TagKey<T> key, V val) {
        this.entries.add(new Entry<>(Either.left(key), val));
    }

    public Map<T, V> resolveAndBuild(Registry<T> registry) {
        Map<T, V> map = this.mapSupplier.get();
        for (Entry<T, V> entry : this.entries) {
            List<T> targets = entry.getTargets(registry);
            for (T target : targets) {
                map.compute(target, (k, v) -> {
                    if (v == null)
                        return entry.value;
                    return this.merger.merge(v, entry.value);
                });
            }
        }
        this.entries.clear();

        return map;
    }

    @FunctionalInterface
    public interface Merger<V> {
        V merge(V original, V resolved);
    }

    protected record Entry<T, V>(Either<TagKey<T>, T> key, V value) {
        public List<T> getTargets(Registry<T> registry) {
            return this.key.map(
              tag -> SLTags.getAllEntriesInTag(tag, registry),
              List::of
            );
        }
    }
}
