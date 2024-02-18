package com.auroali.bloodlust.datagen;

import com.auroali.bloodlust.common.registry.BLTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

public class BLEntityTagsProvider extends FabricTagProvider<EntityType<?>> {
    public BLEntityTagsProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator, Registry.ENTITY_TYPE);
    }

    @Override
    protected void generateTags() {
        getOrCreateTagBuilder(BLTags.Entities.HAS_BLOOD)
                .add(EntityType.SHEEP)
                .add(EntityType.COW)
                .add(EntityType.PLAYER)
                .add(EntityType.FOX)
                .add(EntityType.WOLF)
                .add(EntityType.CAT)
                .add(EntityType.OCELOT)
                .add(EntityType.DONKEY)
                .add(EntityType.HORSE)
                .add(EntityType.MULE)
                .add(EntityType.VILLAGER)
                .add(EntityType.EVOKER)
                .add(EntityType.PILLAGER)
                .add(EntityType.VINDICATOR)
                .add(EntityType.ILLUSIONER)
                .add(EntityType.WITCH)
                .add(EntityType.WANDERING_TRADER)
                .add(EntityType.LLAMA)
                .add(EntityType.PANDA)
                .add(EntityType.PIG)
                .add(EntityType.PIGLIN)
                .add(EntityType.PIGLIN_BRUTE)
                .add(EntityType.HOGLIN)
                .add(EntityType.GOAT)
                .add(EntityType.POLAR_BEAR)
                .add(EntityType.RAVAGER);
    }
}
