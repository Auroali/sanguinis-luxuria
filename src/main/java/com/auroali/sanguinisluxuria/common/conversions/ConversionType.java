package com.auroali.sanguinisluxuria.common.conversions;

import com.auroali.sanguinisluxuria.common.registry.SLRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public interface ConversionType {
    Entity apply(World world, Entity original, EntityType<?> targetType, NbtCompound tag);

    static Identifier getId(ConversionType type) {
        return SLRegistries.CONVERSION_TYPES.getId(type);
    }
}
