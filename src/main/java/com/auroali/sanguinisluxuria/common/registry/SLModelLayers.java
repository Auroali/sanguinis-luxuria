package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.VillagerResemblingModel;

public class SLModelLayers {
    public static final EntityModelLayer VAMPIRE_VILLAGER = new EntityModelLayer(SLResources.VAMPIRE_VILLAGER, "main");

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(VAMPIRE_VILLAGER, () -> TexturedModelData.of(VillagerResemblingModel.getModelData(), 64, 64));
    }
}
