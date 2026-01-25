package com.auroali.sanguinisluxuria.datagen.util;

import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Equivelant to {@link net.fabricmc.fabric.impl.datagen.FabricDataGenHelper#addConditions(Object, ConditionJsonProvider[])},
 * but split out to avoid depending on FAPI internals
 */
public class DatagenConditions {
    private static final Map<Object, ConditionJsonProvider[]> CONDITIONS = new IdentityHashMap<>();

    public static void addConditions(Object object, ConditionJsonProvider[] conditions) {
        CONDITIONS.merge(
          object,
          conditions,
          (left, right) -> {
              ConditionJsonProvider[] newArray = Arrays.copyOf(left, left.length + right.length);
              System.arraycopy(right, 0, newArray, left.length, right.length);
              return newArray;
          }
        );
    }

    public static ConditionJsonProvider[] consumeConditions(Object object) {
        return CONDITIONS.remove(object);
    }
}
