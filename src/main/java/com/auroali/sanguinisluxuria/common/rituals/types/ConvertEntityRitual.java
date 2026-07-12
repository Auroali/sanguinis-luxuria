package com.auroali.sanguinisluxuria.common.rituals.types;

import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.data.EntityConversionLoader;
import com.auroali.sanguinisluxuria.common.registry.SLAdvancementCriterion;
import com.auroali.sanguinisluxuria.common.registry.SLRitualTypes;
import com.auroali.sanguinisluxuria.common.rituals.Ritual;
import com.auroali.sanguinisluxuria.common.rituals.RitualParameters;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.auroali.sanguinisluxuria.common.rituals.RitualUtil;
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
        if (!parameters.hasTarget() || !parameters.targetWithin(64))
            return;

        LivingEntity target = parameters.target();
        if (EntityConversionLoader.convertEntity(ConversionContext.from(target, this.conversion))) {
            parameters.applyToPlayerTarget(player -> SLAdvancementCriterion.CONVERT.trigger(player, this.conversion));
            RitualUtil.spawnSuccessParticles(parameters);
            RitualUtil.spawnSuccessParticlesAt(parameters, parameters.target().getPos());
        }
    }

    public ConversionContext.Conversion getConversion() {
        return this.conversion;
    }

    @Override
    public void appendTooltips(List<Text> tooltips) {
        switch (this.conversion) {
            case CONVERTING -> tooltips.add(Text.translatable(this.getType().getTranslationKey() + ".converting"));
            case DECONVERTING -> tooltips.add(Text.translatable(this.getType().getTranslationKey() + ".deconverting"));
        }
    }

    @Override
    public RitualType<?> getType() {
        return SLRitualTypes.CONVERT_ENTITY_RITUAL;
    }
}
