package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.TexturedModel;

import java.util.Optional;

public class SLModels {
    public static final Model MASK = new Model(Optional.of(SLResources.id("item/mask_base")), Optional.empty(), TextureKey.LAYER0);
    public static final Model ALTAR_MODEL = new Model(Optional.of(SLResources.id("block/altar_base")), Optional.empty(), TextureKey.TEXTURE);
    public static final TexturedModel.Factory ALTAR = TexturedModel.makeFactory(TextureMap::texture, ALTAR_MODEL);
}
