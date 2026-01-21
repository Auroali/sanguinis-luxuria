package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.registry.SLBlocks;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import com.auroali.sanguinisluxuria.common.registry.SLWorldgen;
import com.auroali.sanguinisluxuria.common.worldgen.DecayedTwigsDecorator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.BiasedToBottomIntProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.UpwardsBranchingTrunkPlacer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SLWorldgenProvider extends FabricDynamicRegistryProvider {
    private static final RegistryKey<ConfiguredFeature<?, ?>> SILVER_ORE_CONFIGURED_FEATURE = RegistryKey.of(
      RegistryKeys.CONFIGURED_FEATURE,
      SLResources.id("silver_ore")
    );

    public SLWorldgenProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        // silver ore
        entries.add(SILVER_ORE_CONFIGURED_FEATURE,
          new ConfiguredFeature<>(
            Feature.ORE,
            new OreFeatureConfig(
              List.of(
                OreFeatureConfig.createTarget(
                  new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                  SLBlocks.DEEPSLATE_SILVER_ORE.getDefaultState()
                ),
                OreFeatureConfig.createTarget(
                  new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES),
                  SLBlocks.SILVER_ORE.getDefaultState()
                )
              ),
              5,
              0
            )
          )
        );

        entries.add(SLWorldgen.SILVER_ORE,
          new PlacedFeature(
            entries.ref(SILVER_ORE_CONFIGURED_FEATURE),
            List.of(
              CountPlacementModifier.of(16),
              HeightRangePlacementModifier.trapezoid(
                YOffset.aboveBottom(20),
                YOffset.fixed(40)
              ),
              BiomePlacementModifier.of()
            )
          )
        );

        // decayed tree
        entries.add(SLWorldgen.DECAYED_TREE,
          new ConfiguredFeature<>(
            Feature.TREE,
            new TreeFeatureConfig.Builder(
              new WeightedBlockStateProvider(
                DataPool.<BlockState>builder()
                  .add(SLBlocks.DECAYED_LOG.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y), 23)
                  .add(SLBlocks.HUNGRY_DECAYED_LOG.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y), 2)
              ),
              new UpwardsBranchingTrunkPlacer(
                4,
                1,
                0,
                BiasedToBottomIntProvider.create(1, 5),
                0.35f,
                BiasedToBottomIntProvider.create(0, 3),
                RegistryEntryList.of(Registries.BLOCK::getEntry, Blocks.AIR)
              ),
              BlockStateProvider.of(Blocks.AIR.getDefaultState()),
              new BlobFoliagePlacer(ConstantIntProvider.ZERO, ConstantIntProvider.ZERO, 0),
              new TwoLayersFeatureSize(1, 0, 1)
            )
              .dirtProvider(BlockStateProvider.of(Blocks.DIRT.getDefaultState()))
              .decorators(List.of(new DecayedTwigsDecorator(0.23f)))
              .build()
          )
        );
    }

    @Override
    public String getName() {
        return "Worldgen";
    }
}
