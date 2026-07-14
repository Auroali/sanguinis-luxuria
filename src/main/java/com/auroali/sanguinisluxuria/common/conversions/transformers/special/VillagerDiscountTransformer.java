package com.auroali.sanguinisluxuria.common.conversions.transformers.special;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.conversions.EntityConversionTransformer;
import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.EntityInteraction;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class VillagerDiscountTransformer implements EntityConversionTransformer {
    public static final VillagerDiscountTransformer INSTANCE = new VillagerDiscountTransformer();
    public static final Codec<VillagerDiscountTransformer> CODEC = Codec.unit(INSTANCE);

    private VillagerDiscountTransformer() {
    }

    @Override
    public void apply(ConversionContext context, NbtCompound nbtIn, NbtCompound nbtOut, List<ConversionContext.ConversionCallback> callbacks) {
        callbacks.add((convertedEntity, conversionSource) -> {
            if (conversionSource instanceof ServerPlayerEntity playerSource && convertedEntity instanceof VillagerEntity villager) {
                if (context.entity() instanceof ZombieEntity zombie)
                    Criteria.CURED_ZOMBIE_VILLAGER.trigger(playerSource, zombie, villager);
                context.world().handleInteraction(EntityInteraction.ZOMBIE_VILLAGER_CURED, playerSource, villager);
            }
        });
    }

    @Override
    public Codec<? extends EntityConversionTransformer> getCodec() {
        return CODEC;
    }
}
