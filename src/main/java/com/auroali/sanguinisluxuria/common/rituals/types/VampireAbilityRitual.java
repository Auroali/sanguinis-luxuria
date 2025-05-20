package com.auroali.sanguinisluxuria.common.rituals.types;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.BLAdvancementCriterion;
import com.auroali.sanguinisluxuria.common.registry.BLRegistries;
import com.auroali.sanguinisluxuria.common.registry.BLRitualTypes;
import com.auroali.sanguinisluxuria.common.rituals.Ritual;
import com.auroali.sanguinisluxuria.common.rituals.RitualParameters;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;

import java.util.List;

public record VampireAbilityRitual(VampireAbility ability) implements Ritual {
    public static final Codec<VampireAbilityRitual> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      BLRegistries.VAMPIRE_ABILITIES.getCodec().fieldOf("ability").forGetter(VampireAbilityRitual::ability)
    ).apply(instance, VampireAbilityRitual::new));


    @Override
    public void onCompleted(RitualParameters parameters) {
        if (!VampireHelper.isVampire(parameters.target()) || !parameters.targetWithin(32.d))
            return;

        VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(parameters.target());
        VampireAbilityContainer abilities = vampire.getAbilityContainer();
        if (abilities.hasAbility(this.ability) || !this.ability.testConditions(parameters.target(), vampire, abilities))
            // todo: add feedback
            return;
        abilities.addAbility(this.ability);
        parameters.applyToPlayerTarget(player -> BLAdvancementCriterion.UNLOCK_ABILITY.trigger(player, this.ability));
    }

    @Override
    public void appendTooltips(List<Text> tooltips) {
        tooltips.add(Text.translatable(this.ability.getTranslationKey()));
    }

    @Override
    public RitualType<?> getType() {
        return BLRitualTypes.ABILITY_RITUAL_TYPE;
    }
}
