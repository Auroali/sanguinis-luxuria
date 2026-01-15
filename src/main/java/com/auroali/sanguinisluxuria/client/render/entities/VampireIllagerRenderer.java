package com.auroali.sanguinisluxuria.client.render.entities;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.entities.VampireIllagerEntity;
import com.auroali.sanguinisluxuria.common.registry.SLModelLayers;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.util.Identifier;

public class VampireIllagerRenderer extends IllagerEntityRenderer<VampireIllagerEntity> {
    public VampireIllagerRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new IllagerEntityModel<>(ctx.getModelLoader().getModelPart(SLModelLayers.VAMPIRE_ILLAGER)), 0.6f);
        this.addFeature(new HeldItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(VampireIllagerEntity entity) {
        return SLResources.VAMPIRE_VILLAGER_TEXTURE;
    }
}
