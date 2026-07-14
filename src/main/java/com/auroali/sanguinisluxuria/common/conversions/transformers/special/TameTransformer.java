package com.auroali.sanguinisluxuria.common.conversions.transformers.special;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionTransformer;
import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class TameTransformer implements EntityConversionTransformer {
    public static final TameTransformer INSTANCE = new TameTransformer();
    public static final Codec<TameTransformer> CODEC = Codec.unit(INSTANCE);

    private TameTransformer() {
    }

    @Override
    public void apply(ConversionContext context, NbtCompound nbtIn, NbtCompound nbtOut, List<ConversionContext.ConversionCallback> callbacks) {
        callbacks.add((convertedEntity, conversionSource) -> {
            if (conversionSource instanceof ServerPlayerEntity player) {
                if (convertedEntity instanceof Tameable tameable) {
                    if (tameable.getOwnerUuid() != null) {
                        if (convertedEntity instanceof TameableEntity tameableEntity) {
                            tameableEntity.setOwner(player);
                        } else if (convertedEntity instanceof AbstractHorseEntity horseEntity) {
                            horseEntity.bondWithPlayer(player);
                        }
                        if (convertedEntity instanceof AnimalEntity animal)
                            Criteria.TAME_ANIMAL.trigger(player, animal);
                    }
                }
            }
        });
    }

    @Override
    public Codec<? extends EntityConversionTransformer> getCodec() {
        return CODEC;
    }
}
