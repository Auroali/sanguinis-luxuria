package com.auroali.sanguinisluxuria.datagen;

import com.auroali.sanguinisluxuria.common.registry.SLEntities;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class SLEntityTagsProvider extends FabricTagProvider<EntityType<?>> {
    public SLEntityTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ENTITY_TYPE, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(SLTags.Entities.HAS_BLOOD)
          .add(
            EntityType.SHEEP,
            EntityType.COW,
            EntityType.PLAYER,
            EntityType.FOX,
            EntityType.WOLF,
            EntityType.CAT,
            EntityType.OCELOT,
            EntityType.DONKEY,
            EntityType.HORSE,
            EntityType.MULE,
            EntityType.VILLAGER,
            EntityType.EVOKER,
            EntityType.PILLAGER,
            EntityType.VINDICATOR,
            EntityType.ILLUSIONER,
            EntityType.WITCH,
            EntityType.WANDERING_TRADER,
            EntityType.LLAMA,
            EntityType.PANDA,
            EntityType.PIG,
            EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE,
            EntityType.HOGLIN,
            EntityType.GOAT,
            EntityType.POLAR_BEAR,
            EntityType.RAVAGER,
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.ZOMBIE_HORSE,
            EntityType.HUSK,
            EntityType.DROWNED,
            EntityType.ZOGLIN,
            EntityType.ZOMBIFIED_PIGLIN,
            EntityType.ENDERMAN,
            EntityType.TRADER_LLAMA,
            EntityType.CAMEL,
            EntityType.SNIFFER,
            EntityType.MOOSHROOM,
            EntityType.RABBIT,
            EntityType.WITCH
          )
          .add(
            SLEntities.VAMPIRE_VILLAGER,
            SLEntities.VAMPIRE_MERCHANT
          )
          .addOptional(new Identifier("ratsmischief", "rat"))
          .addOptional(new Identifier("spectrum", "egg_laying_wooly_pig"))
          .addOptional(new Identifier("spectrum", "kindling"));

        this.getOrCreateTagBuilder(SLTags.Entities.GOOD_BLOOD)
          .add(
            EntityType.VILLAGER,
            EntityType.PILLAGER,
            EntityType.VINDICATOR,
            EntityType.EVOKER,
            EntityType.WITCH,
            EntityType.WANDERING_TRADER,
            EntityType.PLAYER
          )
          .add(
            SLEntities.VAMPIRE_MERCHANT,
            SLEntities.VAMPIRE_VILLAGER
          );

        this.getOrCreateTagBuilder(SLTags.Entities.TOXIC_BLOOD)
          .add(
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.ZOMBIE_HORSE,
            EntityType.HUSK,
            EntityType.DROWNED,
            EntityType.ZOGLIN,
            EntityType.ZOMBIFIED_PIGLIN
          );

        this.getOrCreateTagBuilder(SLTags.Entities.TELEPORTS_ON_DRAIN)
          .add(EntityType.ENDERMAN);
    }
}
