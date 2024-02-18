package com.auroali.bloodlust.datagen;

import com.auroali.bloodlust.BLResources;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;

import java.util.Optional;

public class BLModels {
    public static final Model MASK = new Model(Optional.of(BLResources.id("item/mask_base")), Optional.empty(), TextureKey.LAYER0);
}
