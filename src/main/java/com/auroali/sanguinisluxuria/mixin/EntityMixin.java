package com.auroali.sanguinisluxuria.mixin;

import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.util.VampireHelper;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(
      method = "canBeHitByProjectile",
      at = @At("HEAD"),
      cancellable = true
    )
    public void sanguinisluxuria$mistVampiresCantBeHit(CallbackInfoReturnable<Boolean> cir) {
        if (VampireHelper.isVampire((Entity) (Object) this)) {
            VampireComponent vampire = VampireComponent.KEY.get(this);
            if (vampire.isMist())
                cir.setReturnValue(false);
        }
    }
}
