package com.auroali.sanguinisluxuria.client.render;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.entities.VampireMerchant;
import com.auroali.sanguinisluxuria.common.entities.VampireVillagerEntity;
import com.auroali.sanguinisluxuria.common.registry.BLModelLayers;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.util.Identifier;

public class VampireMerchantRenderer extends MobEntityRenderer<VampireMerchant, VillagerResemblingModel<VampireMerchant>> {
    public VampireMerchantRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new VillagerResemblingModel<>(ctx.getModelLoader().getModelPart(BLModelLayers.VAMPIRE_VILLAGER)), 0.6f);
    }

    @Override
    public Identifier getTexture(VampireMerchant entity) {
        return BLResources.VAMPIRE_MERCHANT_TEXTURE;
    }
}
