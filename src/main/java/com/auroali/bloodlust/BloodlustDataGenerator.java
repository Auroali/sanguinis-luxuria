package com.auroali.bloodlust;

import com.auroali.bloodlust.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class BloodlustDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		fabricDataGenerator.addProvider(new BLLangProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLEntityTagsProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLItemTagsProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLModelProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLRecipeProvider(fabricDataGenerator));
	}
}
