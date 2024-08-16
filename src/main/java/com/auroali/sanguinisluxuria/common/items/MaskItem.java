package com.auroali.sanguinisluxuria.common.items;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class MaskItem extends TrinketItem implements TrinketRenderer {
    public MaskItem(Settings settings) {
        super(settings);
    }

    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if(contextModel instanceof BipedEntityModel<? extends LivingEntity> model) {
            matrices.push();
            ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();
            model.head.rotate(matrices);
            matrices.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(MathHelper.PI));
            matrices.translate(0, 0.5, 0);
            renderer.renderItem(
                stack,
                ModelTransformation.Mode.HEAD,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                0
            );
            matrices.pop();
        }
    }
}
