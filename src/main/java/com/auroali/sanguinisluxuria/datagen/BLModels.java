package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.BLResources;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.TextureKey;

import java.util.Optional;

public class BLModels {
    public static final Model MASK = new Model(Optional.of(BLResources.id("item/mask_base")), Optional.empty(), TextureKey.LAYER0);
    public static final Model REDSTONE_DUST_DOT = new Model(Optional.of(ModelIds.getMinecraftNamespacedBlock("redstone_dust_dot")), Optional.empty(),
            TextureKey.PARTICLE,
            BLTextureKeys.LINE,
            BLTextureKeys.OVERLAY
    );
}
