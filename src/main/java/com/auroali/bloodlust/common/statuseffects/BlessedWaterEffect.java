package com.auroali.bloodlust.common.statuseffects;

import com.auroali.bloodlust.VampireHelper;
import com.auroali.bloodlust.common.registry.BLDamageSources;
import com.auroali.bloodlust.common.registry.BLStatusEffects;
import com.auroali.bloodlust.config.BLConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlessedWaterEffect extends StatusEffect {
    public BlessedWaterEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        super.applyInstantEffect(source, attacker, target, amplifier, proximity);
        if(!target.isUndead() && !VampireHelper.isVampire(target)) {
            target.addStatusEffect(new StatusEffectInstance(BLStatusEffects.BLOOD_PROTECTION, 3600, 0));
            return;
        }

        float damage = BLConfig.INSTANCE.blessedWaterDamage + amplifier;

        if(source != null)
            target.damage(BLDamageSources.blessedWater(source, attacker), damage);
        else
            target.damage(BLDamageSources.BLESSED_WATER, damage);
    }

    @Override
    public boolean isInstant() {
        return true;
    }
}
