package com.auroali.sanguinisluxuria.mixin.client;

import com.auroali.sanguinisluxuria.config.BLConfig;
import com.auroali.sanguinisluxuria.config.SLClientConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(AccessibilityOptionsScreen.class)
public class AccessibilityOptionsScreenMixin {
    @Unique
    private static final SimpleOption<Boolean> sanguinisluxuria$HUNGERFX_OPTION = SimpleOption.ofBoolean(
      "options.sanguinisluxuria.use_hunger_effects",
      SimpleOption.constantTooltip(Text.translatable("options.sanguinisluxuria.use_hunger_effects.tooltip")),
      SLClientConfig.INSTANCE.useCustomHungerEffect,
      v -> {
          SLClientConfig.INSTANCE.useCustomHungerEffect = v;
          SLClientConfig.INSTANCE.save();
      });

    @ModifyReturnValue(method = "getOptions", at = @At("RETURN"))
    private static SimpleOption<?>[] sanguinisluxuria$addClientEffectOptionsToAccessibilityScreen(SimpleOption<?>[] original) {
        SimpleOption<?>[] newOptions = Arrays.copyOf(original, original.length + 1);
        newOptions[original.length] = sanguinisluxuria$HUNGERFX_OPTION;
        return newOptions;
    }
}
