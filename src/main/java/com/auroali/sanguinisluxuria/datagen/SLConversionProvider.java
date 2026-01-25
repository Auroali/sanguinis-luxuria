package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.conversions.conditions.ConversionContextCondition;
import com.auroali.sanguinisluxuria.common.conversions.conditions.VampireConversionCondition;
import com.auroali.sanguinisluxuria.common.conversions.transformers.ConditionalTransformer;
import com.auroali.sanguinisluxuria.common.conversions.transformers.CopyConversionTransformer;
import com.auroali.sanguinisluxuria.common.conversions.transformers.SetTransformer;
import com.auroali.sanguinisluxuria.common.registry.SLConversions;
import com.auroali.sanguinisluxuria.common.registry.SLEntities;
import com.auroali.sanguinisluxuria.datagen.builders.ConversionJsonBuilder;
import com.auroali.sanguinisluxuria.datagen.generators.SanguinisLuxuriaConversionsProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.village.VillagerType;

public class SLConversionProvider extends SanguinisLuxuriaConversionsProvider {
    public SLConversionProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generateConversions(ConversionExporter exporter) {
        ConversionJsonBuilder.create(EntityType.VILLAGER, SLEntities.VAMPIRE_ILLAGER)
          .type(SLConversions.SPAWN_TYPE)
          .transformer(CopyConversionTransformer.create("Offers"))
          .transformer(CopyConversionTransformer.create("Xp"))
          .transformer(CopyConversionTransformer.create("VillagerData"))
          .condition(ConversionContextCondition.converting())
          .offerTo(exporter);

        ConversionJsonBuilder.create(SLEntities.VAMPIRE_ILLAGER, EntityType.VILLAGER)
          .type(SLConversions.SPAWN_TYPE)
          .transformer(CopyConversionTransformer.create("Offers"))
          .transformer(CopyConversionTransformer.create("Xp"))
          .transformer(CopyConversionTransformer.create("VillagerData"))
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter);

        ConversionJsonBuilder.create(EntityType.PLAYER, EntityType.PLAYER)
          .type(SLConversions.SET_VAMPIRE_TYPE)
          .condition(ConversionContextCondition.converting())
          .condition(VampireConversionCondition.nonVampire())
          .offerTo(exporter, SLResources.id("convert_player_to_vampire"));

        ConversionJsonBuilder.create(EntityType.PLAYER, EntityType.PLAYER)
          .type(SLConversions.REVERT_VAMPIRE_TYPE)
          .condition(ConversionContextCondition.deconverting())
          .condition(VampireConversionCondition.vampire())
          .offerTo(exporter, SLResources.id("convert_vampire_to_player"));

        ConversionJsonBuilder.create(EntityType.WITCH, EntityType.VILLAGER)
          .type(SLConversions.SPAWN_TYPE)
          .sortedTransformers(
            VillagerType.BIOME_TO_TYPE.entrySet(),
            entry -> ConditionalTransformer.biome(
              SetTransformer.create("VillagerData.type", Registries.VILLAGER_TYPE.getId(entry.getValue()).toString()),
              entry.getKey()
            ),
            e -> e.getKey().getValue()
          )
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter);

        ConversionJsonBuilder.create(EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN)
          .type(SLConversions.SPAWN_TYPE)
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter);

        ConversionJsonBuilder.create(EntityType.ZOGLIN, EntityType.HOGLIN)
          .type(SLConversions.SPAWN_TYPE)
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter);

        ConversionJsonBuilder.create(EntityType.VEX, EntityType.ALLAY)
          .type(SLConversions.SPAWN_TYPE)
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter);

        ConversionJsonBuilder.create(EntityType.ZOMBIE_VILLAGER, EntityType.VILLAGER)
          .type(SLConversions.SPAWN_TYPE)
          .transformer(CopyConversionTransformer.create("VillagerData"))
          .transformer(CopyConversionTransformer.create("Xp"))
          .transformer(CopyConversionTransformer.create("Offers"))
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter);

        ConversionJsonBuilder.create(EntityType.ZOMBIE_HORSE, EntityType.HORSE)
          .type(SLConversions.SPAWN_TYPE)
          .transformers(CopyConversionTransformer.create("Owner"))
          .transformers(CopyConversionTransformer.create("Bred"))
          .transformers(CopyConversionTransformer.create("EatingHaystack"))
          .transformers(CopyConversionTransformer.create("SaddleItem"))
          .transformers(CopyConversionTransformer.create("Tame"))
          .transformers(CopyConversionTransformer.create("Temper"))
          .transformers(CopyConversionTransformer.create("attributes"))
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter);

        ConversionJsonBuilder.create(EntityType.WANDERING_TRADER, SLEntities.VAMPIRE_MERCHANT)
          .type(SLConversions.SPAWN_TYPE)
          .condition(ConversionContextCondition.converting())
          .offerTo(exporter);

        ConversionJsonBuilder.create(SLEntities.VAMPIRE_MERCHANT, EntityType.WANDERING_TRADER)
          .type(SLConversions.SPAWN_TYPE)
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter);
    }
}
