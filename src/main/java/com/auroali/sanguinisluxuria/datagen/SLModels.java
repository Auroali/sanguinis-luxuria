package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;

import java.util.Optional;

public class SLModels {
    public static final Model MASK = new Model(Optional.of(SLResources.id("item/mask_base")), Optional.empty(), TextureKey.LAYER0);
}
