package com.auroali.sanguinisluxuria.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides deduplicated decoding for the wrapped {@link Codec<T>}. The decoded value should implement {@link Object#hashCode()}
 * and {@link Object#equals(Object)}, as the internal cache is implemented as a {@link T} -> {@link T} hashmap. The internal cache
 * is also never cleared, so depending on usage it may be better to only create an instance of this class when necessary, and discard it
 * afterward
 *
 * @param <T> the target type
 */
public class CachedCodec<T> implements Codec<T> {
    private final Codec<T> wrapped;
    private final Map<T, T> cache;

    /**
     * Wraps a {@link Codec<T>}
     *
     * @param codec the codec to wrap
     * @param <T>   the target type
     * @return the new {@link CachedCodec<T>}
     */
    public static <T> CachedCodec<T> wrap(Codec<T> codec) {
        return new CachedCodec<>(codec);
    }

    protected CachedCodec(Codec<T> codec) {
        this.wrapped = codec;
        this.cache = new ConcurrentHashMap<>();
    }

    @Override
    public <T1> DataResult<Pair<T, T1>> decode(DynamicOps<T1> ops, T1 input) {
        DataResult<Pair<T, T1>> result = this.wrapped.decode(ops, input);
        return result.map(pair -> {
            T value = pair.getFirst();
            if (this.cache.containsKey(value)) {
                return Pair.of(this.cache.get(value), pair.getSecond());
            }
            this.cache.put(value, value);
            return pair;
        });
    }

    @Override
    public <T1> DataResult<T1> encode(T input, DynamicOps<T1> ops, T1 prefix) {
        return this.wrapped.encode(input, ops, prefix);
    }
}
