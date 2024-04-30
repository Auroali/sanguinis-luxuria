package com.auroali.sanguinisluxuria.common.statuseffects;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.registry.BLAdvancementCriterion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

public class BloodProtectionEffect extends StatusEffect {
    public BloodProtectionEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
        if(entity.world.isClient || !VampireHelper.isVampire(entity))
            return;

        int duration = entity.getStatusEffect(this).getDuration();

        if(entity.getRandom().nextInt(200) == 0)
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100));

        if(duration == 1) {
            if(entity instanceof ServerPlayerEntity p)
                BLAdvancementCriterion.UNBECOME_VAMPIRE.trigger(p);
            entity.clearStatusEffects();
            BLEntityComponents.VAMPIRE_COMPONENT.get(entity).setIsVampire(false);
        }
    }
}
