package com.auroali.sanguinisluxuria.common.entities;

import com.auroali.sanguinisluxuria.common.registry.SLEntityAttributes;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;

public class VampireDamageHandler {
    public static boolean canKillVampire(DamageSource source) {
        if (isEffectiveAgainstVampires(source))
            return true;

        Entity attacker = source.getAttacker();
        if (attacker instanceof LivingEntity living) {
            ItemStack stack = living.getMainHandStack();
            double blessedDamage = living.getAttributes().hasAttribute(SLEntityAttributes.BLESSED_DAMAGE) ? living.getAttributeValue(SLEntityAttributes.BLESSED_DAMAGE) : 0.d;
            return blessedDamage > 0 || EnchantmentHelper.getLevel(Enchantments.SMITE, stack) > 0;
        }
        return false;
    }

    public static boolean isEffectiveAgainstVampires(DamageSource source) {
        return source.isIn(SLTags.DamageTypes.VAMPIRES_WEAK_TO);
    }

    public static float modifyIncomingDamage(LivingEntity vampire, DamageSource source, float amount) {
        if (isEffectiveAgainstVampires(source)) {
            double vulnerability = vampire.getAttributeValue(SLEntityAttributes.VULNERABILITY);
            amount *= (float) vulnerability;
        }
        return amount;
    }
}
