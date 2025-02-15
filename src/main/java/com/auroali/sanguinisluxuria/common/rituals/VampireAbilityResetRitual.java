package com.auroali.sanguinisluxuria.common.rituals;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.BLAdvancementCriterion;
import com.auroali.sanguinisluxuria.common.registry.BLRitualTypes;
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

        VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(parameters.target());
        VampireAbilityContainer abilities = vampire.getAbilties();
        for (VampireAbility ability : abilities) {
            ability.onAbilityRemoved(parameters.target(), vampire);
            abilities.removeAbility(ability);
            parameters.applyToPlayerTarget(BLAdvancementCriterion.RESET_ABILITIES::trigger);
        }

        BLEntityComponents.VAMPIRE_COMPONENT.sync(parameters.target());
    }

    @Override
    public RitualType<?> getType() {
        return BLRitualTypes.ABILITY_RESET_RITUAL_TYPE;
    }
}
