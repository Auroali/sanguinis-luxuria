package com.auroali.sanguinisluxuria;

import com.auroali.sanguinisluxuria.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.loot.context.LootContextTypes;

public class BloodlustDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		fabricDataGenerator.addProvider(new BLLangProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLEntityTagsProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLItemTagsProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLModelProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLRecipeProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLBlockTagsProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLBiomeTagsProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLVampireAbiltyTagsProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLAdvancementsProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLBlockLootTableProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLEntityLootTableProvider(fabricDataGenerator, LootContextTypes.ENTITY));
	}
}
