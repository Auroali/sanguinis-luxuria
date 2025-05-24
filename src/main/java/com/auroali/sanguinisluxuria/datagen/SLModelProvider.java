package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.blocks.AltarBlock;
import com.auroali.sanguinisluxuria.common.blocks.HungryDecayedLogBlock;
import com.auroali.sanguinisluxuria.common.registry.SLBlocks;
import com.auroali.sanguinisluxuria.common.registry.SLItems;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SLModelProvider extends FabricModelProvider {
    public SLModelProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        createCauldron(blockStateModelGenerator, SLBlocks.BLOOD_CAULDRON, SLResources.BLOOD_STILL_TEXTURE);

        createBloodSplatter(blockStateModelGenerator);

        blockStateModelGenerator.blockStateCollector
          .accept(VariantsBlockStateSupplier
            .create(SLBlocks.ALTAR)
            .coordinate(BlockStateModelGenerator.createBooleanModelMap(AltarBlock.ACTIVE, SLResources.id("block/altar_active"), SLResources.id("block/altar")))
          );

        blockStateModelGenerator.blockStateCollector
          .accept(BlockStateModelGenerator.createSingletonBlockState(SLBlocks.PEDESTAL, SLResources.id("block/pedestal")));
        blockStateModelGenerator.registerSingleton(SLBlocks.SILVER_BLOCK, TexturedModel.CUBE_ALL);
        blockStateModelGenerator.registerSingleton(SLBlocks.SILVER_ORE, TexturedModel.CUBE_ALL);
        blockStateModelGenerator.registerSingleton(SLBlocks.DEEPSLATE_SILVER_ORE, TexturedModel.CUBE_ALL);
        blockStateModelGenerator.registerSingleton(SLBlocks.RAW_SILVER_BLOCK, TexturedModel.CUBE_ALL);

        blockStateModelGenerator.registerLog(SLBlocks.DECAYED_LOG).log(SLBlocks.DECAYED_LOG).wood(SLBlocks.DECAYED_WOOD);
        blockStateModelGenerator.registerLog(SLBlocks.STRIPPED_DECAYED_LOG).log(SLBlocks.STRIPPED_DECAYED_LOG).wood(SLBlocks.STRIPPED_DECAYED_WOOD);

        blockStateModelGenerator.blockStateCollector.accept(generateHungryDecayedLog(SLBlocks.HUNGRY_DECAYED_LOG, blockStateModelGenerator.modelCollector));
        blockStateModelGenerator.blockStateCollector.accept(generateHungryDecayedLog(SLBlocks.STRIPPED_HUNGRY_DECAYED_LOG, blockStateModelGenerator.modelCollector));

        blockStateModelGenerator.registerItemModel(SLBlocks.DECAYED_TWIGS);
        createDecayedTwigs(SLBlocks.DECAYED_TWIGS, blockStateModelGenerator.blockStateCollector, blockStateModelGenerator.modelCollector);

        registerPressurePlate(blockStateModelGenerator, SLBlocks.SILVER_PRESSURE_PLATE, SLBlocks.SILVER_BLOCK);

        blockStateModelGenerator.registerCubeAllModelTexturePool(SLBlocks.DECAYED_PLANKS)
          .family(SLBlockFamilies.DECAYED_WOOD_FAMILY);

        blockStateModelGenerator.registerHangingSign(SLBlocks.STRIPPED_DECAYED_LOG, SLBlocks.DECAYED_HANGING_SIGN, SLBlocks.DECAYED_WALL_HANGING_SIGN);
//          .pressurePlate(BLBlocks.DECAYED_PRESSURE_PLATE)
//          .stairs(BLBlocks.DECAYED_STAIRS)
//          .fence(BLBlocks.DECAYED_FENCE)
//          .fenceGate(BLBlocks.DECAYED_FENCE_GATE)
//          .slab(BLBlocks.DECAYED_SLAB)
//          .sign(BLBlocks.DECAYED_SIGN);
        blockStateModelGenerator.registerFlowerPotPlant(SLBlocks.GRAFTED_SAPLING, SLBlocks.POTTED_GRAFTED_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);

        registerBars(blockStateModelGenerator, SLBlocks.SILVER_BARS);
    }

    // net.minecraft.data.client.BlockStateModelGenerator#registerIronBars
    private static void registerBars(BlockStateModelGenerator generator, Block barsBlock) {

        Identifier identifier = ModelIds.getBlockSubModelId(barsBlock, "_post_ends");
        Identifier identifier2 = ModelIds.getBlockSubModelId(barsBlock, "_post");
        Identifier identifier3 = ModelIds.getBlockSubModelId(barsBlock, "_cap");
        Identifier identifier4 = ModelIds.getBlockSubModelId(barsBlock, "_cap_alt");
        Identifier identifier5 = ModelIds.getBlockSubModelId(barsBlock, "_side");
        Identifier identifier6 = ModelIds.getBlockSubModelId(barsBlock, "_side_alt");
        generator.blockStateCollector
          .accept(
            MultipartBlockStateSupplier.create(barsBlock)
              .with(BlockStateVariant.create().put(VariantSettings.MODEL, identifier))
              .with(
                When.create().set(Properties.NORTH, false).set(Properties.EAST, false).set(Properties.SOUTH, false).set(Properties.WEST, false),
                BlockStateVariant.create().put(VariantSettings.MODEL, identifier2)
              )
              .with(
                When.create().set(Properties.NORTH, true).set(Properties.EAST, false).set(Properties.SOUTH, false).set(Properties.WEST, false),
                BlockStateVariant.create().put(VariantSettings.MODEL, identifier3)
              )
              .with(
                When.create().set(Properties.NORTH, false).set(Properties.EAST, true).set(Properties.SOUTH, false).set(Properties.WEST, false),
                BlockStateVariant.create().put(VariantSettings.MODEL, identifier3).put(VariantSettings.Y, VariantSettings.Rotation.R90)
              )
              .with(
                When.create().set(Properties.NORTH, false).set(Properties.EAST, false).set(Properties.SOUTH, true).set(Properties.WEST, false),
                BlockStateVariant.create().put(VariantSettings.MODEL, identifier4)
              )
              .with(
                When.create().set(Properties.NORTH, false).set(Properties.EAST, false).set(Properties.SOUTH, false).set(Properties.WEST, true),
                BlockStateVariant.create().put(VariantSettings.MODEL, identifier4).put(VariantSettings.Y, VariantSettings.Rotation.R90)
              )
              .with(When.create().set(Properties.NORTH, true), BlockStateVariant.create().put(VariantSettings.MODEL, identifier5))
              .with(
                When.create().set(Properties.EAST, true),
                BlockStateVariant.create().put(VariantSettings.MODEL, identifier5).put(VariantSettings.Y, VariantSettings.Rotation.R90)
              )
              .with(When.create().set(Properties.SOUTH, true), BlockStateVariant.create().put(VariantSettings.MODEL, identifier6))
              .with(
                When.create().set(Properties.WEST, true),
                BlockStateVariant.create().put(VariantSettings.MODEL, identifier6).put(VariantSettings.Y, VariantSettings.Rotation.R90)
              )
          );
        generator.registerItemModel(barsBlock);
    }

    private static void createDecayedTwigs(Block block, Consumer<BlockStateSupplier> blockStateCollector, BiConsumer<Identifier, Supplier<JsonElement>> modelCollector) {
        Identifier model = Models.CROSS.upload(block, TextureMap.cross(block), modelCollector);
        blockStateCollector.accept(
          VariantsBlockStateSupplier.create(SLBlocks.DECAYED_TWIGS)
            .coordinate(BlockStateVariantMap
              .create(Properties.WALL_MOUNT_LOCATION, Properties.HORIZONTAL_FACING)
              .register(WallMountLocation.WALL, Direction.EAST, BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
              )
              .register(WallMountLocation.WALL, Direction.WEST, BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
              )
              .register(WallMountLocation.WALL, Direction.NORTH, BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                .put(VariantSettings.Y, VariantSettings.Rotation.R0)
              )
              .register(WallMountLocation.WALL, Direction.SOUTH, BlockStateVariant.create()
                .put(VariantSettings.MODEL, model)
                .put(VariantSettings.X, VariantSettings.Rotation.R90)
                .put(VariantSettings.Y, VariantSettings.Rotation.R180)
              )
              .register(WallMountLocation.CEILING, Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, model).put(VariantSettings.X, VariantSettings.Rotation.R180))
              .register(WallMountLocation.CEILING, Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, model).put(VariantSettings.X, VariantSettings.Rotation.R180))
              .register(WallMountLocation.CEILING, Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, model).put(VariantSettings.X, VariantSettings.Rotation.R180))
              .register(WallMountLocation.CEILING, Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, model).put(VariantSettings.X, VariantSettings.Rotation.R180))
              .register(WallMountLocation.FLOOR, Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, model))
              .register(WallMountLocation.FLOOR, Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, model))
              .register(WallMountLocation.FLOOR, Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, model))
              .register(WallMountLocation.FLOOR, Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, model))
            )
        );
    }

    private static void registerPressurePlate(BlockStateModelGenerator modelGenerator, Block pressurePlate, Block source) {
        TextureMap textureMap = TextureMap.texture(source);
        Identifier up = Models.PRESSURE_PLATE_UP.upload(pressurePlate, textureMap, modelGenerator.modelCollector);
        Identifier down = Models.PRESSURE_PLATE_DOWN.upload(pressurePlate, textureMap, modelGenerator.modelCollector);
        modelGenerator.blockStateCollector
          .accept(VariantsBlockStateSupplier.create(pressurePlate).coordinate(BlockStateModelGenerator.createBooleanModelMap(Properties.POWERED, down, up)));
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
          MultipartBlockStateSupplier.create(SLBlocks.BLOOD_SPLATTER)
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
              BlockStateVariant.create().put(VariantSettings.MODEL, SLResources.id("block/blood_splatter_dot"))
            )
            .with(
              When.create().set(Properties.NORTH_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP),
              BlockStateVariant.create().put(VariantSettings.MODEL, SLResources.id("block/blood_splatter_side0"))
            )
            .with(
              When.create().set(Properties.SOUTH_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP),
              BlockStateVariant.create().put(VariantSettings.MODEL, SLResources.id("block/blood_splatter_alt0"))
            )
            .with(
              When.create().set(Properties.EAST_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP),
              BlockStateVariant.create()
                .put(VariantSettings.MODEL, SLResources.id("block/blood_splatter_alt1"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
            )
            .with(
              When.create().set(Properties.WEST_WIRE_CONNECTION, WireConnection.SIDE, WireConnection.UP),
              BlockStateVariant.create()
                .put(VariantSettings.MODEL, SLResources.id("block/blood_splatter_side1"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
            )
            .with(
              When.create().set(Properties.NORTH_WIRE_CONNECTION, WireConnection.UP),
              BlockStateVariant.create().put(VariantSettings.MODEL, SLResources.id("block/blood_splatter_up"))
            )
            .with(
              When.create().set(Properties.EAST_WIRE_CONNECTION, WireConnection.UP),
              BlockStateVariant.create()
                .put(VariantSettings.MODEL, SLResources.id("block/blood_splatter_up"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R90)
            )
            .with(
              When.create().set(Properties.SOUTH_WIRE_CONNECTION, WireConnection.UP),
              BlockStateVariant.create()
                .put(VariantSettings.MODEL, SLResources.id("block/blood_splatter_up"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R180)
            )
            .with(
              When.create().set(Properties.WEST_WIRE_CONNECTION, WireConnection.UP),
              BlockStateVariant.create()
                .put(VariantSettings.MODEL, SLResources.id("block/blood_splatter_up"))
                .put(VariantSettings.Y, VariantSettings.Rotation.R270)
            )
        );
    }

    public static VariantsBlockStateSupplier generateHungryDecayedLog(Block block, BiConsumer<Identifier, Supplier<JsonElement>> modelCollector) {
        BlockStateVariantMap.DoubleProperty<Integer, Direction.Axis> map = BlockStateVariantMap.create(HungryDecayedLogBlock.BLOOD_LEVEL, PillarBlock.AXIS);
        Identifier[] horizontalModels = new Identifier[4];
        Identifier[] verticalModels = new Identifier[4];
        for (int i = 0; i < 4; i++) {
            String suffix = "_%d".formatted(i);
            horizontalModels[i] = Models.CUBE_COLUMN_HORIZONTAL.upload(block, i == 0 ? "" : suffix, sideAndEndForTopSuffixed(block, suffix), modelCollector);
            verticalModels[i] = Models.CUBE_COLUMN.upload(block, i == 0 ? "" : suffix, sideAndEndForTopSuffixed(block, suffix), modelCollector);
        }

        for (BlockState state : block.getStateManager().getStates()) {
            int bloodLevel = state.get(HungryDecayedLogBlock.BLOOD_LEVEL);
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
            map.register(state.get(HungryDecayedLogBlock.BLOOD_LEVEL), state.get(PillarBlock.AXIS), variant);
        }
        return VariantsBlockStateSupplier.create(block).coordinate(map);
    }

    private static TextureMap sideAndEndForTopSuffixed(Block block, String suffix) {
        return TextureMap.sideAndEndForTop(block).put(TextureKey.SIDE, TextureMap.getId(block).withSuffixedPath(suffix));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(SLItems.MASK_1, SLModels.MASK);
        itemModelGenerator.register(SLItems.MASK_2, SLModels.MASK);
        itemModelGenerator.register(SLItems.MASK_3, SLModels.MASK);
        itemModelGenerator.register(SLItems.MASK_1, "_inventory", Models.GENERATED);
        itemModelGenerator.register(SLItems.MASK_2, "_inventory", Models.GENERATED);
        itemModelGenerator.register(SLItems.MASK_3, "_inventory", Models.GENERATED);
        itemModelGenerator.register(SLItems.BLOOD_BAG, "_1", Models.GENERATED);
        itemModelGenerator.register(SLItems.BLOOD_BAG, "_2", Models.GENERATED);
        itemModelGenerator.register(SLItems.BLOOD_BAG, "_3", Models.GENERATED);
        itemModelGenerator.register(SLItems.BLOOD_BOTTLE, "_2", Models.GENERATED);
        itemModelGenerator.register(SLItems.TWISTED_BLOOD, Models.GENERATED);
        itemModelGenerator.register(SLItems.PENDANT_OF_PIERCING, Models.GENERATED);
        itemModelGenerator.register(SLItems.BLOOD_PETAL, Models.GENERATED);
        itemModelGenerator.register(SLItems.SILVER_INGOT, Models.GENERATED);
        itemModelGenerator.register(SLItems.RAW_SILVER, Models.GENERATED);
        itemModelGenerator.register(SLItems.SILVER_SWORD, Models.HANDHELD);
        itemModelGenerator.register(SLItems.SILVER_AXE, Models.HANDHELD);
        itemModelGenerator.register(SLItems.SILVER_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(SLItems.SILVER_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(SLItems.SILVER_HOE, Models.HANDHELD);
        itemModelGenerator.register(SLItems.VAMPIRE_FANG, Models.GENERATED);
    }
}
