package com.auroali.sanguinisluxuria.mixin;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BedBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BedBlock.class)
public class BedBlockMixin {
    @Unique
    private static final Text CANNOT_SLEEP_DAY = Text.translatable("block.sanguinisluxuria.bed.no_sleep");

    @WrapOperation(method = "method_19283", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity$SleepFailureReason;getMessage()Lnet/minecraft/text/Text;", ordinal = 1))
    private static Text sanguinisluxuria$modifySleepMsg(PlayerEntity.SleepFailureReason instance, Operation<Text> original, @Local(argsOnly = true) PlayerEntity entity) {
        if (instance == PlayerEntity.SleepFailureReason.NOT_POSSIBLE_NOW && VampireHelper.isVampire(entity))
            return CANNOT_SLEEP_DAY;

        return original.call(instance);
    }
}
