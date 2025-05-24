package com.auroali.sanguinisluxuria.mixin.client;

import com.auroali.sanguinisluxuria.SanguinisLuxuriaClient;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Inject(method = "shouldDarkenSky", at = @At("HEAD"), cancellable = true)
    public void sanguinisluxuria$makeSkyDarkDuringAltarCrafting(CallbackInfoReturnable<Boolean> cir) {
        if (SanguinisLuxuriaClient.isAltarActive) {
            SanguinisLuxuriaClient.isAltarActive = false;
            cir.setReturnValue(true);
        }
    }
}
