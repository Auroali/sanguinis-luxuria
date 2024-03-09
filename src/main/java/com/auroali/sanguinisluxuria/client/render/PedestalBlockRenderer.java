package com.auroali.sanguinisluxuria.client.render;

import com.auroali.sanguinisluxuria.common.blockentities.PedestalBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;

public class PedestalBlockRenderer implements BlockEntityRenderer<PedestalBlockEntity> {
    ItemRenderer itemRenderer;
    Random random;

    public PedestalBlockRenderer(ItemRenderer itemRenderer) {
        this.itemRenderer = itemRenderer;
        this.random = Random.create();
    }

    private int getRenderedAmount(ItemStack stack) {
        int i = 1;
        if (stack.getCount() > 48) {
            i = 5;
        } else if (stack.getCount() > 32) {
            i = 4;
        } else if (stack.getCount() > 16) {
            i = 3;
        } else if (stack.getCount() > 1) {
            i = 2;
        }

        return i;
    }

    @Override
    public void render(PedestalBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        random.setSeed(entity.getItem().isEmpty() ? 187 : Item.getRawId(entity.getItem().getItem()) + entity.getItem().getDamage());
        matrices.translate(0.5, 0.8, 0.5);
        float bobbing = MathHelper.sin((entity.getWorld().getTime() + tickDelta) / 10.0F) * 0.1F + 0.1F;
        //matrices.translate(0, bobbing, 0);
        float rotation = (entity.getWorld().getTime() + tickDelta) / 20.0f;
        matrices.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(rotation));

        BakedModel model = itemRenderer.getModel(entity.getItem(), entity.getWorld(), null, entity.getPos().hashCode());
        boolean hasDepth = model.hasDepth();

        float yScale = model.getTransformation().getTransformation(ModelTransformation.Mode.GROUND).scale.getY();
        matrices.translate(0.0, (bobbing + 0.25F * yScale), 0.0);
        float xGroundScale = model.getTransformation().ground.scale.getX();
        float yGroundScale = model.getTransformation().ground.scale.getY();
        float zGroundScale = model.getTransformation().ground.scale.getZ();
        int amount = getRenderedAmount(entity.getItem());
        if (!hasDepth) {
            float r = -0.0F * (float)(amount - 1) * 0.5F * xGroundScale;
            float s = -0.0F * (float)(amount - 1) * 0.5F * yGroundScale;
            float t = -0.09375F * (float)(amount - 1) * 0.5F * zGroundScale;
            matrices.translate(r, s, t);
        }

        for(int i = 0; i < amount; ++i) {
            matrices.push();
            if (i > 0) {
                if (hasDepth) {
                    float s = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float t = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float v = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    matrices.translate(s, t, v);
                } else {
                    float s = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    float t = (random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                    matrices.translate(s, t, 0.0);
                }
            }

            this.itemRenderer
                    .renderItem(entity.getItem(), ModelTransformation.Mode.GROUND, false, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, model);
            matrices.pop();
            if (!hasDepth) {
                matrices.translate((double)(0.0F * xGroundScale), (double)(0.0F * yGroundScale), (double)(0.09375F * zGroundScale));
            }
        }
        matrices.pop();
    }
}
