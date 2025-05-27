package com.auroali.sanguinisluxuria.common.rituals.types;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.SLAdvancementCriterion;
import com.auroali.sanguinisluxuria.common.registry.SLRitualTypes;
import com.auroali.sanguinisluxuria.common.rituals.Ritual;
import com.auroali.sanguinisluxuria.common.rituals.RitualParameters;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.mojang.serialization.Codec;

public class VampireAbilityResetRitual implements Ritual {
    public static final VampireAbilityResetRitual INSTANCE = new VampireAbilityResetRitual();
    public static final Codec<VampireAbilityResetRitual> CODEC = Codec.unit(INSTANCE);

    protected VampireAbilityResetRitual() {
    }

    @Override
    public void onCompleted(RitualParameters parameters) {
        if (!VampireHelper.isVampire(parameters.target()) || !parameters.targetWithin(32.d))
            return;

        VampireComponent vampire = VampireComponent.KEY.get(parameters.target());
        VampireAbilityContainer abilities = vampire.getAbilityContainer();
        for (VampireAbility ability : abilities.abilities()) {
            ability.onAbilityRemoved(parameters.target(), vampire);
            abilities.removeAbility(ability);
            parameters.applyToPlayerTarget(SLAdvancementCriterion.RESET_ABILITIES::trigger);
        }

        VampireComponent.KEY.sync(parameters.target());
    }

    @Override
    public RitualType<?> getType() {
        return SLRitualTypes.ABILITY_RESET_RITUAL_TYPE;
    }
}
