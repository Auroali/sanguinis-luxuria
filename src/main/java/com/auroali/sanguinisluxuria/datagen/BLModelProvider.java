package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.blocks.AltarBlock;
import com.auroali.sanguinisluxuria.common.blocks.BloodDecayedLogBlock;
import com.auroali.sanguinisluxuria.common.registry.BLBlocks;
import com.auroali.sanguinisluxuria.common.registry.BLItems;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BLModelProvider extends FabricModelProvider {
    public BLModelProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        createCauldron(blockStateModelGenerator, BLBlocks.BLOOD_CAULDRON, BLResources.BLOOD_STILL_TEXTURE);

        createBloodSplatter(blockStateModelGenerator);

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
        blockStateModelGenerator.registerSingleton(BLBlocks.SILVER_BLOCK, TexturedModel.CUBE_ALL);
        blockStateModelGenerator.registerSingleton(BLBlocks.SILVER_ORE, TexturedModel.CUBE_ALL);
        blockStateModelGenerator.registerSingleton(BLBlocks.DEEPSLATE_SILVER_ORE, TexturedModel.CUBE_ALL);
        blockStateModelGenerator.registerSingleton(BLBlocks.RAW_SILVER_BLOCK, TexturedModel.CUBE_ALL);

        blockStateModelGenerator.registerLog(BLBlocks.DECAYED_LOG).log(BLBlocks.DECAYED_LOG).wood(BLBlocks.DECAYED_WOOD);
        blockStateModelGenerator.registerLog(BLBlocks.STRIPPED_DECAYED_LOG).log(BLBlocks.STRIPPED_DECAYED_LOG).wood(BLBlocks.STRIPPED_DECAYED_WOOD);

        blockStateModelGenerator.blockStateCollector.accept(generateHungryDecayedLog(BLBlocks.HUNGRY_DECAYED_LOG, blockStateModelGenerator.modelCollector));
        blockStateModelGenerator.blockStateCollector.accept(generateHungryDecayedLog(BLBlocks.STRIPPED_HUNGRY_DECAYED_LOG, blockStateModelGenerator.modelCollector));
    }

    private static void createCauldron(BlockStateModelGenerator blockStateModelGenerator, Block block, Identifier fillTexture) {
        blockStateModelGenerator.blockStateCollector
          .accept(
            VariantsBlockStateSupplier.create(block)
              .coordinate(
                BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL)
                  .register(
                    1,
                    BlockStateVariant.create()
                      .put(
                        VariantSettings.MODEL,
                        Models.TEMPLATE_CAULDRON_LEVEL1
                          .upload(block, "_level1", TextureMap.cauldron(fillTexture), blockStateModelGenerator.modelCollector)
                      )
                  )
                  .register(
                    2,
                    BlockStateVariant.create()
                      .put(
                        VariantSettings.MODEL,
                        Models.TEMPLATE_CAULDRON_LEVEL2
                          .upload(block, "_level2", TextureMap.cauldron(fillTexture), blockStateModelGenerator.modelCollector)
                      )
                  )
                  .register(
                    3,
                    BlockStateVariant.create()
                      .put(
                        VariantSettings.MODEL,
                        Models.TEMPLATE_CAULDRON_FULL
                          .upload(block, "_full", TextureMap.cauldron(fillTexture), blockStateModelGenerator.modelCollector)
                      )
                  )
              )
          );
    }

    private static void createBloodSplatter(BlockStateModelGenerator blockStateModelGenerator) {
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
    }

    public static VariantsBlockStateSupplier generateHungryDecayedLog(Block block, BiConsumer<Identifier, Supplier<JsonElement>> modelCollector) {
        BlockStateVariantMap.DoubleProperty<Integer, Direction.Axis> map = BlockStateVariantMap.create(BloodDecayedLogBlock.BLOOD_LEVEL, PillarBlock.AXIS);
        Identifier[] horizontalModels = new Identifier[4];
        Identifier[] verticalModels = new Identifier[4];
        for (int i = 0; i < 4; i++) {
            String suffix = "_%d".formatted(i);
            horizontalModels[i] = Models.CUBE_COLUMN_HORIZONTAL.upload(block, i == 0 ? "" : suffix, sideAndEndForTopSuffixed(block, suffix), modelCollector);
            verticalModels[i] = Models.CUBE_COLUMN.upload(block, i == 0 ? "" : suffix, sideAndEndForTopSuffixed(block, suffix), modelCollector);
        }

        for (BlockState state : block.getStateManager().getStates()) {
            int bloodLevel = state.get(BloodDecayedLogBlock.BLOOD_LEVEL);
            Direction.Axis axis = state.get(Properties.AXIS);
            BlockStateVariant variant = BlockStateVariant.create();
            switch (axis) {
                case X -> variant
                  .put(VariantSettings.X, VariantSettings.Rotation.R90)
                  .put(VariantSettings.Y, VariantSettings.Rotation.R90)
                  .put(VariantSettings.MODEL, horizontalModels[bloodLevel]);
                case Z -> variant.put(VariantSettings.X, VariantSettings.Rotation.R90)
                  .put(VariantSettings.MODEL, horizontalModels[bloodLevel]);
                case Y -> variant
                  .put(VariantSettings.MODEL, verticalModels[bloodLevel]);
            }
            map.register(state.get(BloodDecayedLogBlock.BLOOD_LEVEL), state.get(PillarBlock.AXIS), variant);
        }
        return VariantsBlockStateSupplier.create(block).coordinate(map);
    }

    private static TextureMap sideAndEndForTopSuffixed(Block block, String suffix) {
        return TextureMap.sideAndEndForTop(block).put(TextureKey.SIDE, TextureMap.getId(block).withSuffixedPath(suffix));
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
