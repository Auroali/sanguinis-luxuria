package com.auroali.sanguinisluxuria.common.rituals;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.registry.BLAdvancementCriterion;
import com.auroali.sanguinisluxuria.common.registry.BLConversions;
import com.auroali.sanguinisluxuria.common.registry.BLRitualTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class ConvertEntityRitual implements Ritual {
    public static final Codec<ConvertEntityRitual> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(
        ConversionContext.Conversion.CODEC.fieldOf("conversion").forGetter(ConvertEntityRitual::getConversion)
      ).apply(instance, ConvertEntityRitual::new)
    );

    private final ConversionContext.Conversion conversion;

    public ConvertEntityRitual(ConversionContext.Conversion conversion) {
        this.conversion = conversion;
    }

    @Override
    public void onCompleted(RitualParameters parameters) {
        LivingEntity target = parameters.target();
        if (BLConversions.convertEntity(ConversionContext.from(target, this.conversion)))
            parameters.applyToPlayerTarget(player -> BLAdvancementCriterion.CONVERT.trigger(player, this.conversion));
    }

    public ConversionContext.Conversion getConversion() {
        return this.conversion;
    }

    @Override
    public RitualType<?> getType() {
        return BLRitualTypes.CONVERT_ENTITY_RITUAL;
    }
}
