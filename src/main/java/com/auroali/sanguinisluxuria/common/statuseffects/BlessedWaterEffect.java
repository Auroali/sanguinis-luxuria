package com.auroali.sanguinisluxuria.common.statuseffects;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.registry.BLDamageSources;
import com.auroali.sanguinisluxuria.common.registry.BLStatusEffects;
import com.auroali.sanguinisluxuria.config.BLConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.jetbrains.annotations.Nullable;

public class BlessedWaterEffect extends StatusEffect {
    public BlessedWaterEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        super.applyInstantEffect(source, attacker, target, amplifier, proximity);
        if(!target.isUndead() || (VampireHelper.isVampire(target) && target.hasStatusEffect(StatusEffects.WEAKNESS))) {
            target.addStatusEffect(new StatusEffectInstance(BLStatusEffects.BLOOD_PROTECTION, 3600, 0));
            return;
        }

        float damage = BLConfig.INSTANCE.blessedWaterDamage * (amplifier + 1);

        if(source != null)
            target.damage(BLDamageSources.blessedWater(source, attacker), damage);
        else
            target.damage(BLDamageSources.get(target.getWorld(), BLResources.BLESSED_WATER_DAMAGE_KEY), damage);
    }

    @Override
    public boolean isInstant() {
        return true;
    }
}
