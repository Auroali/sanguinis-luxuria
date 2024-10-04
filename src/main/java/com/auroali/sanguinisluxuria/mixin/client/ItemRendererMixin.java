package com.auroali.sanguinisluxuria.mixin.client;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.registry.BLItems;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow
    @Final
    private ItemModels models;

    @Unique
    private static final Identifier MASK_ONE_INVENTORY_MODEL = BLResources.MASK_ONE_ID.withPrefixedPath("item/").withSuffixedPath("_inventory");
    @Unique
    private static final Identifier MASK_TWO_INVENTORY_MODEL = BLResources.MASK_TWO_ID.withPrefixedPath("item/").withSuffixedPath("_inventory");
    @Unique
    private static final Identifier MASK_THREE_INVENTORY_MODEL = BLResources.MASK_THREE_ID.withPrefixedPath("item/").withSuffixedPath("_inventory");

    @Inject(
      method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", ordinal = 0)
    )
    public void sanguinisluxuria$makeMasksUseInventoryModel(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci, @Local(argsOnly = true) LocalRef<BakedModel> modelRef) {
        if (renderMode == ModelTransformationMode.HEAD)
            return;

        if (stack.isOf(BLItems.MASK_1)) {
            modelRef.set(this.models.getModelManager().getModel(MASK_ONE_INVENTORY_MODEL));
            return;
        }
        if (stack.isOf(BLItems.MASK_2)) {
            modelRef.set(this.models.getModelManager().getModel(MASK_TWO_INVENTORY_MODEL));
            return;
        }
        if (stack.isOf(BLItems.MASK_3)) {
            modelRef.set(this.models.getModelManager().getModel(MASK_THREE_INVENTORY_MODEL));
        }
    }
}
