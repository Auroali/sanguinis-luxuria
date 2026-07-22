package com.auroali.sanguinisluxuria.common.rituals.types;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.SLAdvancementCriterion;
import com.auroali.sanguinisluxuria.common.registry.SLRegistries;
import com.auroali.sanguinisluxuria.common.registry.SLRitualTypes;
import com.auroali.sanguinisluxuria.common.rituals.Ritual;
import com.auroali.sanguinisluxuria.common.rituals.RitualParameters;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.auroali.sanguinisluxuria.common.rituals.RitualUtil;
import com.auroali.sanguinisluxuria.util.VampireHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;

import java.util.List;

public record VampireAbilityRitual(VampireAbility ability) implements Ritual {
    public static final Codec<VampireAbilityRitual> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      SLRegistries.VAMPIRE_ABILITIES.getCodec().fieldOf("ability").forGetter(VampireAbilityRitual::ability)
    ).apply(instance, VampireAbilityRitual::new));


    @Override
    public void onCompleted(RitualParameters parameters) {
        if (!parameters.targetWithin(32.d))
            return;

        parameters.target().ifPresent(target -> {
            if (!VampireHelper.isVampire(target))
                return;

            VampireComponent vampire = VampireComponent.KEY.get(target);
            VampireAbilityContainer abilities = vampire.getAbilityContainer();
            if (abilities.has(this.ability) || !this.ability.testConditions(target, vampire, abilities))
                // todo: add feedback
                return;
            abilities.addAbility(this.ability);
            parameters.ifPlayerTarget(player -> SLAdvancementCriterion.UNLOCK_ABILITY.trigger(player, this.ability));

            RitualUtil.spawnSuccessParticles(parameters);
            RitualUtil.spawnSuccessParticlesAt(parameters, target.getPos());
        });

    }

    @Override
    public void appendTooltips(List<Text> tooltips) {
        tooltips.add(Text.translatable(this.ability.getTranslationKey()));
    }

    @Override
    public RitualType<?> getType() {
        return SLRitualTypes.ABILITY_RITUAL_TYPE;
    }
}
