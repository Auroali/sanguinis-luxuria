package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.SLEntities;
import com.auroali.sanguinisluxuria.common.registry.SLItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootingEnchantLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class SLEntityLootTableProvider extends SimpleFabricLootTableProvider {
    public SLEntityLootTableProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator, LootContextTypes.ENTITY);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        exporter.accept(
          tableId(SLEntities.VAMPIRE_ILLAGER),
          LootTable.builder()
            .pool(LootPool.builder()
              .rolls(ConstantLootNumberProvider.create(1))
              .with(ItemEntry.builder(SLItems.VAMPIRE_FANG)
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.f, 2.f)))
                .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.f, 2.f)))
              )
            )
        );

        exporter.accept(
          tableId(SLEntities.VAMPIRE_MERCHANT),
          LootTable.builder()
            .pool(LootPool.builder()
              .rolls(ConstantLootNumberProvider.create(1))
              .with(ItemEntry.builder(SLItems.VAMPIRE_FANG)
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(0.f, 1.f)))
                .apply(LootingEnchantLootFunction.builder(UniformLootNumberProvider.create(0.f, 1.f)))
              )
            )
            .pool(LootPool.builder()
              .rolls(ConstantLootNumberProvider.create(1))
              .conditionally(KilledByPlayerLootCondition.builder())
              .with(ItemEntry.builder(SLItems.MASK_1))
              .with(ItemEntry.builder(SLItems.MASK_2))
              .with(ItemEntry.builder(SLItems.MASK_3))
              .with(EmptyEntry.builder()
                .weight(15)
                .quality(-1)
              )
            )
        );
    }

    public static Identifier tableId(EntityType<?> type) {
        return EntityType.getId(type).withPrefixedPath("entities/");
    }
}
