package com.auroali.bloodlust.common.statuseffects;

import com.auroali.bloodlust.VampireHelper;
import com.auroali.bloodlust.common.registry.BLDamageSources;
import com.auroali.bloodlust.config.BLConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import org.jetbrains.annotations.Nullable;

public class BloodProtectionEffect extends StatusEffect {
    public BloodProtectionEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }
}
