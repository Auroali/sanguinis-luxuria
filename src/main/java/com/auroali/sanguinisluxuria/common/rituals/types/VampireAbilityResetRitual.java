package com.auroali.sanguinisluxuria.common.rituals.types;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.SLAdvancementCriterion;
import com.auroali.sanguinisluxuria.common.registry.SLRitualTypes;
import com.auroali.sanguinisluxuria.common.rituals.Ritual;
import com.auroali.sanguinisluxuria.common.rituals.RitualParameters;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.auroali.sanguinisluxuria.common.rituals.RitualUtil;
import com.auroali.sanguinisluxuria.util.VampireHelper;
import com.mojang.serialization.Codec;

public class VampireAbilityResetRitual implements Ritual {
    public static final VampireAbilityResetRitual INSTANCE = new VampireAbilityResetRitual();
    public static final Codec<VampireAbilityResetRitual> CODEC = Codec.unit(INSTANCE);

    protected VampireAbilityResetRitual() {
    }

    @Override
    public void onCompleted(RitualParameters parameters) {
        if (!parameters.targetWithin(12.d))
            return;

        parameters.target().ifPresent(target -> {
            if (!VampireHelper.isVampire(target))
                return;

            VampireComponent vampire = VampireComponent.KEY.get(target);
            VampireAbilityContainer abilities = vampire.getAbilityContainer();
            for (VampireAbility ability : abilities.abilities()) {
                ability.onAbilityRemoved(target, vampire);
                abilities.removeAbility(ability);
                parameters.ifPlayerTarget(SLAdvancementCriterion.RESET_ABILITIES::trigger);
            }

            VampireComponent.KEY.sync(target);
            RitualUtil.spawnSuccessParticles(parameters);
            RitualUtil.spawnSuccessParticlesAt(parameters, target.getPos());
        });
    }

    @Override
    public RitualType<?> getType() {
        return SLRitualTypes.ABILITY_RESET_RITUAL_TYPE;
    }
}
