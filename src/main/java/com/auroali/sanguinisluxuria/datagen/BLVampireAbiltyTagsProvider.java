package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.registry.BLRegistry;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import com.auroali.sanguinisluxuria.common.registry.BLVampireAbilities;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

public class BLVampireAbiltyTagsProvider extends FabricTagProvider<VampireAbility> {
    public BLVampireAbiltyTagsProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator, BLRegistry.VAMPIRE_ABILITIES);
    }

    @Override
    protected void generateTags() {
        getOrCreateTagBuilder(BLTags.VampireAbilities.TELEPORT_RANGE)
                .add(BLVampireAbilities.TELEPORT_RANGE_1)
                .add(BLVampireAbilities.TELEPORT_RANGE_2);
    }
}
