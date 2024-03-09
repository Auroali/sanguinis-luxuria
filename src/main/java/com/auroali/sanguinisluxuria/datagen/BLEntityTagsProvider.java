package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.BLTags;
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
                .add(EntityType.RAVAGER)
                .add(EntityType.ZOMBIE)
                .add(EntityType.HUSK)
                .add(EntityType.DROWNED)
                .add(EntityType.ZOGLIN)
                .add(EntityType.ZOMBIFIED_PIGLIN);

        getOrCreateTagBuilder(BLTags.Entities.GOOD_BLOOD)
                .add(EntityType.VILLAGER)
                .add(EntityType.PILLAGER)
                .add(EntityType.VINDICATOR)
                .add(EntityType.EVOKER)
                .add(EntityType.WITCH)
                .add(EntityType.WANDERING_TRADER)
                .add(EntityType.PLAYER);

        getOrCreateTagBuilder(BLTags.Entities.TOXIC_BLOOD)
                .add(EntityType.ZOMBIE)
                .add(EntityType.HUSK)
                .add(EntityType.DROWNED)
                .add(EntityType.ZOGLIN)
                .add(EntityType.ZOMBIFIED_PIGLIN);

        getOrCreateTagBuilder(BLTags.Entities.CAN_DROP_BLOOD)
                .add(EntityType.VILLAGER);
    }
}
