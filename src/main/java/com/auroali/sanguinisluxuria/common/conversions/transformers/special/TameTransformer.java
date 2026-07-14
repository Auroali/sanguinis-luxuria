package com.auroali.sanguinisluxuria.common.conversions.transformers.special;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionTransformer;
import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.passive.AbstractHorseEntity;
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
                if (convertedEntity instanceof TameableEntity tameable) {
                    if (tameable.getOwnerUuid() != null) {
                        tameable.setOwner(player);
                        Criteria.TAME_ANIMAL.trigger(player, tameable);
                    }
                } else if (convertedEntity instanceof AbstractHorseEntity horse) {
                    horse.bondWithPlayer(player);
                    Criteria.TAME_ANIMAL.trigger(player, horse);
                }
            }
        });
    }

    @Override
    public Codec<? extends EntityConversionTransformer> getCodec() {
        return CODEC;
    }
}
