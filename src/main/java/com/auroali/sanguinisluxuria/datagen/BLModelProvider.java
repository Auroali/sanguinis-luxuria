package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.blocks.SkillUpgraderBlock;
import com.auroali.sanguinisluxuria.common.registry.BLBlocks;
import com.auroali.sanguinisluxuria.common.registry.BLItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.data.client.*;

public class BLModelProvider extends FabricModelProvider {
    public BLModelProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.blockStateCollector
                .accept(
                        VariantsBlockStateSupplier.create(BLBlocks.BLOOD_CAULDRON)
                                .coordinate(
                                        BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL)
                                                .register(
                                                        1,
                                                        BlockStateVariant.create()
                                                                .put(
                                                                        VariantSettings.MODEL,
                                                                        Models.TEMPLATE_CAULDRON_LEVEL1
                                                                                .upload(BLBlocks.BLOOD_CAULDRON, "_level1", TextureMap.cauldron(BLResources.BLOOD_STILL_TEXTURE), blockStateModelGenerator.modelCollector)
                                                                )
                                                )
                                                .register(
                                                        2,
                                                        BlockStateVariant.create()
                                                                .put(
                                                                        VariantSettings.MODEL,
                                                                        Models.TEMPLATE_CAULDRON_LEVEL2
                                                                                .upload(BLBlocks.BLOOD_CAULDRON, "_level2", TextureMap.cauldron(BLResources.BLOOD_STILL_TEXTURE), blockStateModelGenerator.modelCollector)
                                                                )
                                                )
                                                .register(
                                                        3,
                                                        BlockStateVariant.create()
                                                                .put(
                                                                        VariantSettings.MODEL,
                                                                        Models.TEMPLATE_CAULDRON_FULL
                                                                                .upload(BLBlocks.BLOOD_CAULDRON, "_full", TextureMap.cauldron(BLResources.BLOOD_STILL_TEXTURE), blockStateModelGenerator.modelCollector)
                                                                )
                                                )
                                )
                );
        blockStateModelGenerator.blockStateCollector
                .accept(BlockStateModelGenerator.createSingletonBlockState(
                        BLBlocks.BLOOD_SPLATTER,
                        BLModels.REDSTONE_DUST_DOT.upload(
                                BLBlocks.BLOOD_SPLATTER,
                                new TextureMap()
                                        .put(TextureKey.PARTICLE, BLResources.id("block/blood_splatter"))
                                        .put(BLTextureKeys.LINE, BLResources.id("block/blood_splatter"))
                                        .put(BLTextureKeys.OVERLAY, BLResources.id("block/blood_splatter")),
                                blockStateModelGenerator.modelCollector)
                        ));
        blockStateModelGenerator.blockStateCollector
                .accept(VariantsBlockStateSupplier.create(BLBlocks.SKILL_UPGRADER)
                        .coordinate(BlockStateVariantMap.create(SkillUpgraderBlock.ACTIVE)
                                .register(false, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, BLResources.id("block/altar")))
                                .register(true, BlockStateVariant.create()
                                        .put(VariantSettings.MODEL, BLResources.id("block/altar_active")))
                        )
                );
        blockStateModelGenerator.blockStateCollector
                .accept(BlockStateModelGenerator.createSingletonBlockState(BLBlocks.PEDESTAL, BLResources.id("block/pedestal")));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(BLItems.MASK_1, BLModels.MASK);
        itemModelGenerator.register(BLItems.MASK_2, BLModels.MASK);
        itemModelGenerator.register(BLItems.MASK_3, BLModels.MASK);

        itemModelGenerator.register(BLItems.BLOOD_BAG, "_1", Models.GENERATED);
        itemModelGenerator.register(BLItems.BLOOD_BAG, "_2", Models.GENERATED);
        itemModelGenerator.register(BLItems.BLOOD_BAG, "_3", Models.GENERATED);

        itemModelGenerator.register(BLItems.BLOOD_BOTTLE, "_2", Models.GENERATED);
        itemModelGenerator.register(BLItems.TWISTED_BLOOD, Models.GENERATED);

        itemModelGenerator.register(BLItems.BLESSED_BLOOD, Models.GENERATED);

        itemModelGenerator.register(BLItems.PENDANT_OF_PIERCING, Models.GENERATED);

        itemModelGenerator.register(BLItems.PENDANT_OF_TRANSFUSION, Models.GENERATED);
    }
}
