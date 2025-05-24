package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.blood.BloodConstants;
import com.auroali.sanguinisluxuria.common.loot.SetBloodLootFunction;
import com.auroali.sanguinisluxuria.common.registry.SLItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class SLEntityLootTableProvider extends SimpleFabricLootTableProvider {
    public SLEntityLootTableProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator, LootContextTypes.ENTITY);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> identifierBuilderBiConsumer) {
        identifierBuilderBiConsumer.accept(SLResources.id("entities/vampire_villager"), LootTable.builder()
          .type(LootContextTypes.ENTITY)
          .pool(LootPool.builder()
            .rolls(ConstantLootNumberProvider.create(1))
            .with(ItemEntry.builder(SLItems.VAMPIRE_FANG)
              .apply(SetCountLootFunction.builder(BinomialLootNumberProvider.create(2, 0.5f)))
            )
            .with(ItemEntry.builder(SLItems.BLOOD_BOTTLE)
              .apply(SetBloodLootFunction.builder(BloodConstants.BLOOD_PER_BOTTLE))
            )
            .build()
          )
        );
    }
}
