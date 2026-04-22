package com.auroali.sanguinisluxuria.mixin.legacy;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @ModifyReturnValue(method = "getIdFromNbt", at = @At("RETURN"))
    private static Identifier sanguinisluxuria$modifyEnchantmentId(Identifier original) {
        // vanilla just compares the raw id in the enchantment tag,
        // so to get aliases to work we have to get the enchantment from the
        // id then get the id from the enchantment again
        if (original != null) {
            Enchantment ench = Registries.ENCHANTMENT.get(original);
            if (ench != null)
                return Registries.ENCHANTMENT.getId(ench);
        }
        return original;
    }
}
