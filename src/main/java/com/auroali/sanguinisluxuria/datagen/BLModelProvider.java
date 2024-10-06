package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.blocks.AltarBlock;
import com.auroali.sanguinisluxuria.common.registry.BLBlocks;
import com.auroali.sanguinisluxuria.common.registry.BLItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;

public class BLModelProvider extends FabricModelProvider {
    public BLModelProvider(FabricDataOutput dataGenerator) {
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

        blockStateModelGenerator.blockStateCollector.accept(
          MultipartBlockStateSupplier.create(BLBlocks.BLOOD_SPLATTER)
            .with(
              When.anyOf(
                When.create()
                  .set(Properties.NORTH_WIRE_CONNECTION, WireConnection.NONE)
                  .set(Properties.EAST_WIRE_CONNECTION, WireConnection.NONE)
                  .set(Properties.SOUTH_WIRE_CONNECTION, WireConnection.NONE)
                  .set(Properties.WEST_WIRE_CONNECTION, WireConnection.NONE),
                When.create()
                  .set(Properties.NORTH_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP)
                  .set(Properties.EAST_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP),
                When.create()
                  .set(Properties.EAST_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP)
                  .set(Properties.SOUTH_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP),
                When.create()
                  .set(Properties.SOUTH_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP)
                  .set(Properties.WEST_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP),
                When.create()
                  .set(Properties.WEST_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP)
                  .set(Properties.NORTH_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP)
              ),
              BlockStateVariant.create().put(VariantSettings.MODEL, BLResources.id("block/blood_splatter_dot"))
            )
            .with(
              When.create().set(Properties.NORTH_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP),
              BlockStateVariant.create().put(VariantSettings.MODEL, BLResources.id("block/blood_splatter_side0"))
            )
            .with(
              When.create().set(Properties.SOUTH_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP),
              BlockStateVariant.create().put(VariantSettings.MODEL, BLResources.id("block/blood_splatter_alt0"))
            )
            .with(
              When.create().set(Properties.EAST_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP),
              BlockStateVariant.create()
                .put(VariantSettings.MODEL, BLResources.id("block/blood_splatter_alt1"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
            )
            .with(
              When.create().set(Properties.WEST_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP),
              BlockStateVariant.create()
                .put(VariantSettings.MODEL, BLResources.id("block/blood_splatter_side1"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
            )
            .with(
              When.create().set(Properties.NORTH_WIRE_CONNECTION, WireConnection.UP),
              BlockStateVariant.create().put(VariantSettings.MODEL, BLResources.id("block/blood_splatter_up"))
            )
            .with(
              When.create().set(Properties.EAST_WIRE_CONNECTION, WireConnection.UP),
              BlockStateVariant.create()
                .put(VariantSettings.MODEL, BLResources.id("block/blood_splatter_up"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
            )
            .with(
              When.create().set(Properties.SOUTH_WIRE_CONNECTION, WireConnection.UP),
              BlockStateVariant.create()
                .put(VariantSettings.MODEL, BLResources.id("block/blood_splatter_up"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R180)
            )
            .with(
              When.create().set(Properties.WEST_WIRE_CONNECTION, WireConnection.UP),
              BlockStateVariant.create()
                .put(VariantSettings.MODEL, BLResources.id("block/blood_splatter_up"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
            )
        );
        blockStateModelGenerator.blockStateCollector
          .accept(VariantsBlockStateSupplier.create(BLBlocks.ALTAR)
            .coordinate(BlockStateVariantMap.create(AltarBlock.ACTIVE)
              .register(false, BlockStateVariant.create()
                .put(VariantSettings.MODEL, BLResources.id("block/altar")))
              .register(true, BlockStateVariant.create()
                .put(VariantSettings.MODEL, BLResources.id("block/altar_active")))
            )
          );
        blockStateModelGenerator.blockStateCollector
          .accept(BlockStateModelGenerator.createSingletonBlockState(BLBlocks.PEDESTAL, BLResources.id("block/pedestal")));
        blockStateModelGenerator.registerSingleton(BLBlocks.SILVER_BLOCK, TextureMap.all(BLBlocks.SILVER_BLOCK), Models.CUBE_ALL);
        blockStateModelGenerator.registerSingleton(BLBlocks.SILVER_ORE, TexturedModel.CUBE_ALL);
        blockStateModelGenerator.registerSingleton(BLBlocks.DEEPSLATE_SILVER_ORE, TexturedModel.CUBE_ALL);
        blockStateModelGenerator.registerSingleton(BLBlocks.RAW_SILVER_BLOCK, TexturedModel.CUBE_ALL);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(BLItems.MASK_1, BLModels.MASK);
        itemModelGenerator.register(BLItems.MASK_2, BLModels.MASK);
        itemModelGenerator.register(BLItems.MASK_3, BLModels.MASK);
        itemModelGenerator.register(BLItems.MASK_1, "_inventory", Models.GENERATED);
        itemModelGenerator.register(BLItems.MASK_2, "_inventory", Models.GENERATED);
        itemModelGenerator.register(BLItems.MASK_3, "_inventory", Models.GENERATED);
        itemModelGenerator.register(BLItems.BLOOD_BAG, "_1", Models.GENERATED);
        itemModelGenerator.register(BLItems.BLOOD_BAG, "_2", Models.GENERATED);
        itemModelGenerator.register(BLItems.BLOOD_BAG, "_3", Models.GENERATED);
        itemModelGenerator.register(BLItems.BLOOD_BOTTLE, "_2", Models.GENERATED);
        itemModelGenerator.register(BLItems.TWISTED_BLOOD, Models.GENERATED);
        itemModelGenerator.register(BLItems.BLESSED_BLOOD, Models.GENERATED);
        itemModelGenerator.register(BLItems.PENDANT_OF_PIERCING, Models.GENERATED);
        itemModelGenerator.register(BLItems.PENDANT_OF_TRANSFUSION, Models.GENERATED);
        itemModelGenerator.register(BLItems.BLOOD_PETAL, Models.GENERATED);
        itemModelGenerator.register(BLItems.SILVER_INGOT, Models.GENERATED);
        itemModelGenerator.register(BLItems.RAW_SILVER, Models.GENERATED);
        itemModelGenerator.register(BLItems.SILVER_SWORD, Models.HANDHELD);
        itemModelGenerator.register(BLItems.SILVER_AXE, Models.HANDHELD);
        itemModelGenerator.register(BLItems.SILVER_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(BLItems.SILVER_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(BLItems.SILVER_HOE, Models.HANDHELD);
    }
}
