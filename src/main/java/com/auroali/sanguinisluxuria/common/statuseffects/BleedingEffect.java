package com.auroali.sanguinisluxuria.common.statuseffects;

import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.registry.BLSounds;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;

import java.util.List;

public class BleedingEffect extends StatusEffect {
    public BleedingEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(!entity.getType().isIn(BLTags.Entities.HAS_BLOOD))
            return;

        BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(entity);
        if(blood.drainBlood())
            entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), BLSounds.BLEEDING, SoundCategory.NEUTRAL, 1.0f, 1.0f);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 15 == 0;
    }
}
