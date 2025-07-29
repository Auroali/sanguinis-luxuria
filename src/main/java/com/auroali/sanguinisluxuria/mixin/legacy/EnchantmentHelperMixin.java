package com.auroali.sanguinisluxuria.mixin.legacy;

import com.auroali.sanguinisluxuria.common.legacy.RegistryOverrides;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @ModifyReturnValue(method = "getIdFromNbt", at = @At("RETURN"))
    private static Identifier sanguinisluxuria$modifyEnchantmentId(Identifier original) {
        return RegistryOverrides.getIdOverride(Registries.ENCHANTMENT, original)
                .orElse(original);
    }
}
