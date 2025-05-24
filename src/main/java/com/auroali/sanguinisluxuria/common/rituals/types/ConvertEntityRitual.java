package com.auroali.sanguinisluxuria.common.rituals.types;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.registry.SLAdvancementCriterion;
import com.auroali.sanguinisluxuria.common.registry.SLConversions;
import com.auroali.sanguinisluxuria.common.registry.SLRitualTypes;
import com.auroali.sanguinisluxuria.common.rituals.Ritual;
import com.auroali.sanguinisluxuria.common.rituals.RitualParameters;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

import java.util.List;

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
        if (SLConversions.convertEntity(ConversionContext.from(target, this.conversion)))
            parameters.applyToPlayerTarget(player -> SLAdvancementCriterion.CONVERT.trigger(player, this.conversion));
    }

    public ConversionContext.Conversion getConversion() {
        return this.conversion;
    }

    @Override
    public void appendTooltips(List<Text> tooltips) {
        switch (this.conversion) {
            case CONVERTING -> tooltips.add(Text.translatable("altar_ritual.sanguinisluxuria.convert.converting"));
            case DECONVERTING -> tooltips.add(Text.translatable("altar_ritual.sanguinisluxuria.convert.deconverting"));
        }
    }

    @Override
    public RitualType<?> getType() {
        return SLRitualTypes.CONVERT_ENTITY_RITUAL;
    }
}
