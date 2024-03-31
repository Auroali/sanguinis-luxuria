package com.auroali.sanguinisluxuria.client.render;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.entities.VampireVillagerEntity;
import com.auroali.sanguinisluxuria.common.registry.BLModelLayers;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;
import net.minecraft.util.Identifier;

public class VampireVillagerRenderer extends MobEntityRenderer<VampireVillagerEntity, VillagerResemblingModel<VampireVillagerEntity>> {
    public VampireVillagerRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new VillagerResemblingModel<>(ctx.getModelLoader().getModelPart(BLModelLayers.VAMPIRE_VILLAGER)), 0.6f);
    }

    @Override
    public Identifier getTexture(VampireVillagerEntity entity) {
        return BLResources.VAMPIRE_VILLAGER_TEXTURE;
    }
}
