package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.Bloodlust;
import com.auroali.sanguinisluxuria.common.conversions.conditions.ConversionContextCondition;
import com.auroali.sanguinisluxuria.common.conversions.conditions.VampireConversionCondition;
import com.auroali.sanguinisluxuria.common.conversions.transformers.ConditionalTransformer;
import com.auroali.sanguinisluxuria.common.conversions.transformers.CopyConversionTransformer;
import com.auroali.sanguinisluxuria.common.conversions.transformers.SetTransformer;
import com.auroali.sanguinisluxuria.common.registry.BLConversions;
import com.auroali.sanguinisluxuria.common.registry.BLEntities;
import com.auroali.sanguinisluxuria.datagen.builders.ConversionJsonBuilder;
import com.auroali.sanguinisluxuria.datagen.generators.SanguinisLuxuriaConversionsProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.BiomeKeys;

import java.util.function.Consumer;

public class BLConversionProvider extends SanguinisLuxuriaConversionsProvider {
    public BLConversionProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    protected void generateConversions(Consumer<ConversionJsonBuilder.Provider> exporter) {
        ConversionJsonBuilder.create(EntityType.VILLAGER, BLEntities.VAMPIRE_VILLAGER)
          .type(BLConversions.SPAWN_TYPE)
          .transformer(CopyConversionTransformer.create("Offers"))
          .transformer(CopyConversionTransformer.create("Xp"))
          .transformer(CopyConversionTransformer.create("VillagerData"))
          .condition(ConversionContextCondition.converting())
          .offerTo(exporter, Bloodlust.MODID);

        ConversionJsonBuilder.create(BLEntities.VAMPIRE_VILLAGER, EntityType.VILLAGER)
          .type(BLConversions.SPAWN_TYPE)
          .transformer(CopyConversionTransformer.create("Offers"))
          .transformer(CopyConversionTransformer.create("Xp"))
          .transformer(CopyConversionTransformer.create("VillagerData"))
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter, Bloodlust.MODID);

        ConversionJsonBuilder.create(EntityType.PLAYER, EntityType.PLAYER)
          .type(BLConversions.SET_VAMPIRE_TYPE)
          .condition(ConversionContextCondition.converting())
          .condition(VampireConversionCondition.nonVampire())
          .offerTo(exporter, BLResources.id("convert_player_to_vampire"));

        ConversionJsonBuilder.create(EntityType.PLAYER, EntityType.PLAYER)
          .type(BLConversions.REVERT_VAMPIRE_TYPE)
          .condition(ConversionContextCondition.deconverting())
          .condition(VampireConversionCondition.vampire())
          .offerTo(exporter, BLResources.id("convert_vampire_to_player"));

        ConversionJsonBuilder.create(EntityType.WITCH, EntityType.VILLAGER)
          .type(BLConversions.SPAWN_TYPE)
          .transformers(
            VillagerType.BIOME_TO_TYPE.entrySet(),
            entry -> ConditionalTransformer.biome(
              SetTransformer.create("VillagerData.type", Registries.VILLAGER_TYPE.getId(entry.getValue()).toString()),
              entry.getKey()
            )
          )
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter, Bloodlust.MODID);

        ConversionJsonBuilder.create(EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN)
          .type(BLConversions.SPAWN_TYPE)
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter, Bloodlust.MODID);

        ConversionJsonBuilder.create(EntityType.ZOGLIN, EntityType.HOGLIN)
          .type(BLConversions.SPAWN_TYPE)
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter, Bloodlust.MODID);

        ConversionJsonBuilder.create(EntityType.VEX, EntityType.ALLAY)
          .type(BLConversions.SPAWN_TYPE)
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter, Bloodlust.MODID);

        ConversionJsonBuilder.create(EntityType.ZOMBIE_VILLAGER, EntityType.VILLAGER)
          .type(BLConversions.SPAWN_TYPE)
          .transformer(CopyConversionTransformer.create("VillagerData"))
          .transformer(CopyConversionTransformer.create("Xp"))
          .transformer(CopyConversionTransformer.create("Offers"))
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter, Bloodlust.MODID);

        ConversionJsonBuilder.create(EntityType.ZOMBIE_HORSE, EntityType.HORSE)
          .type(BLConversions.SPAWN_TYPE)
          .transformers(CopyConversionTransformer.create("Owner"))
          .transformers(CopyConversionTransformer.create("Bred"))
          .transformers(CopyConversionTransformer.create("EatingHaystack"))
          .transformers(CopyConversionTransformer.create("SaddleItem"))
          .transformers(CopyConversionTransformer.create("Tame"))
          .transformers(CopyConversionTransformer.create("Temper"))
          .transformers(CopyConversionTransformer.create("attributes"))
          .condition(ConversionContextCondition.deconverting())
          .offerTo(exporter, Bloodlust.MODID);
    }
}
