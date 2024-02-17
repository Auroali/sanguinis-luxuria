package com.auroali.bloodlust;

import com.auroali.bloodlust.datagen.BLEntityTagsProvider;
import com.auroali.bloodlust.datagen.BLLangProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.util.registry.Registry;

public class BloodlustDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		fabricDataGenerator.addProvider(new BLLangProvider(fabricDataGenerator));
		fabricDataGenerator.addProvider(new BLEntityTagsProvider(fabricDataGenerator, Registry.ENTITY_TYPE));
	}
}
