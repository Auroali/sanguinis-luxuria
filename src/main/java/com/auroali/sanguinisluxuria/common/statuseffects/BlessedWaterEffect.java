package com.auroali.sanguinisluxuria.common.statuseffects;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.registry.SLDamageSources;
import com.auroali.sanguinisluxuria.common.registry.SLStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.jetbrains.annotations.Nullable;

public class BlessedWaterEffect extends StatusEffect {
    public BlessedWaterEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        super.applyInstantEffect(source, attacker, target, amplifier, proximity);
        if (!target.isUndead()) {
            target.addStatusEffect(new StatusEffectInstance(SLStatusEffects.BLOOD_PROTECTION, 3600, amplifier));
            return;
        }

        float damage = 2.f * (amplifier + 1);

        target.setOnFireFor(20 + 2 * amplifier);

        if (source != null)
            target.damage(SLDamageSources.blessedWater(source, attacker), damage);
        else
            target.damage(SLDamageSources.get(target.getWorld(), SLResources.BLESSED_WATER_DAMAGE_KEY), damage);
    }

    @Override
    public boolean isInstant() {
        return true;
    }
}
