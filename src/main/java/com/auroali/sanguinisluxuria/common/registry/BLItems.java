package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.common.blood.BloodConstants;
import com.auroali.sanguinisluxuria.common.items.DrinkableBloodItem;
import com.auroali.sanguinisluxuria.common.items.EmptyingDrinkableBloodItem;
import com.auroali.sanguinisluxuria.common.items.MaskItem;
import com.auroali.sanguinisluxuria.common.items.PendantOfPiercingItem;
import com.auroali.sanguinisluxuria.common.items.tools.*;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

public class BLItems {
    public static final MaskItem MASK_1 = new MaskItem(new Item.Settings().maxCount(1));
    public static final MaskItem MASK_2 = new MaskItem(new Item.Settings().maxCount(1));
    public static final MaskItem MASK_3 = new MaskItem(new Item.Settings().maxCount(1));
    public static final Item BLOOD_BAG = new DrinkableBloodItem(20, new Item.Settings().maxCount(1).food(DrinkableBloodItem.BLOOD_FOOD_COMPONENT));
    public static final Item BLOOD_BOTTLE = new EmptyingDrinkableBloodItem(
      BloodConstants.BLOOD_PER_BOTTLE,
      Items.GLASS_BOTTLE,
      new Item.Settings().maxCount(1).recipeRemainder(Items.GLASS_BOTTLE).food(DrinkableBloodItem.BLOOD_FOOD_COMPONENT)
    );
    public static final Item TWISTED_BLOOD = new Item(new Item.Settings().maxCount(1));
    public static final Item VAMPIRE_VILLAGER_SPAWN_EGG = new SpawnEggItem(BLEntities.VAMPIRE_VILLAGER, 0xFF1E1C1B, 0xFFF9f8EF, new Item.Settings());
    public static final Item PENDANT_OF_PIERCING = new PendantOfPiercingItem(new Item.Settings().maxCount(1));
    public static final Item BLOOD_PETAL = new Item(new Item.Settings());
    public static final Item SILVER_INGOT = new Item(new Item.Settings());
    public static final Item RAW_SILVER = new Item(new Item.Settings());
    public static final Item SILVER_SWORD = new BlessedSwordItem(BLToolMaterials.SILVER, 3, -2.4f, 4f, new Item.Settings());
    public static final Item SILVER_PICKAXE = new BlessedPickaxeItem(BLToolMaterials.SILVER, 1, -2.8f, 2, new Item.Settings());
    public static final Item SILVER_AXE = new BlessedAxeItem(BLToolMaterials.SILVER, 6, -3.1f, 2.5f, new Item.Settings());
    public static final Item SILVER_SHOVEL = new BlessedShovelItem(BLToolMaterials.SILVER, 1.5f, -3.f, 2, new Item.Settings());
    public static final Item SILVER_HOE = new BlessedHoeItem(BLToolMaterials.SILVER, 0, -1.f, 2, new Item.Settings());

    // blocks
    public static final Item ALTAR = createBlock(BLBlocks.ALTAR);
    public static final Item PEDESTAL = createBlock(BLBlocks.PEDESTAL);
    public static final Item SILVER_BLOCK = createBlock(BLBlocks.SILVER_BLOCK);
    public static final Item SILVER_ORE = createBlock(BLBlocks.SILVER_ORE);
    public static final Item DEEPSLATE_SILVER_ORE = createBlock(BLBlocks.DEEPSLATE_SILVER_ORE);
    public static final Item RAW_SILVER_BLOCK = createBlock(BLBlocks.RAW_SILVER_BLOCK);
    public static final Item HUNGRY_DECAYED_LOG = createBlock(BLBlocks.HUNGRY_DECAYED_LOG);
    public static final Item DECAYED_LOG = createBlock(BLBlocks.DECAYED_LOG);
    public static final Item DECAYED_WOOD = createBlock(BLBlocks.DECAYED_WOOD);
    public static final Item STRIPPED_DECAYED_LOG = createBlock(BLBlocks.STRIPPED_DECAYED_LOG);
    public static final Item STRIPPED_HUNGRY_DECAYED_LOG = createBlock(BLBlocks.STRIPPED_HUNGRY_DECAYED_LOG);
    public static final Item STRIPPED_DECAYED_WOOD = createBlock(BLBlocks.STRIPPED_DECAYED_WOOD);
    public static final Item DECAYED_TWIGS = createBlock(BLBlocks.DECAYED_TWIGS);
    public static final Item GRAFTED_SAPLING = createBlock(BLBlocks.GRAFTED_SAPLING);
    public static final Item DECAYED_PRESSURE_PLATE = createBlock(BLBlocks.DECAYED_PRESSURE_PLATE);
    public static final Item DECAYED_PLANKS = createBlock(BLBlocks.DECAYED_PLANKS);
    public static final Item DECAYED_STAIRS = createBlock(BLBlocks.DECAYED_STAIRS);
    public static final Item DECAYED_SLAB = createBlock(BLBlocks.DECAYED_SLAB);
    public static final Item DECAYED_BUTTON = createBlock(BLBlocks.DECAYED_BUTTON);
    public static final Item DECAYED_FENCE = createBlock(BLBlocks.DECAYED_FENCE);
    public static final Item DECAYED_FENCE_GATE = createBlock(BLBlocks.DECAYED_FENCE_GATE);
    public static final Item DECAYED_DOOR = createBlock(BLBlocks.DECAYED_DOOR);
    public static final Item DECAYED_TRAPDOOR = createBlock(BLBlocks.DECAYED_TRAPDOOR);
    public static final Item DECAYED_SIGN = new SignItem(new Item.Settings().maxCount(16), BLBlocks.DECAYED_SIGN, BLBlocks.DECAYED_WALL_SIGN);
    public static final Item DECAYED_HANGING_SIGN = new HangingSignItem(BLBlocks.DECAYED_HANGING_SIGN, BLBlocks.DECAYED_WALL_HANGING_SIGN, new Item.Settings().maxCount(16));
    public static final Item SILVER_PRESSURE_PLATE = createBlock(BLBlocks.SILVER_PRESSURE_PLATE);

    public static void register() {
        Registry.register(Registries.ITEM, BLResources.MASK_ONE_ID, MASK_1);
        Registry.register(Registries.ITEM, BLResources.MASK_TWO_ID, MASK_2);
        Registry.register(Registries.ITEM, BLResources.MASK_THREE_ID, MASK_3);
        Registry.register(Registries.ITEM, BLResources.BLOOD_BAG_ID, BLOOD_BAG);
        Registry.register(Registries.ITEM, BLResources.BLOOD_BOTTLE_ID, BLOOD_BOTTLE);
        Registry.register(Registries.ITEM, BLResources.TWISTED_BLOOD_ID, TWISTED_BLOOD);
        Registry.register(Registries.ITEM, BLResources.VAMPIRE_VILLAGER_SPAWN_EGG, VAMPIRE_VILLAGER_SPAWN_EGG);
        Registry.register(Registries.ITEM, BLResources.PENDANT_OF_PIERCING, PENDANT_OF_PIERCING);
        Registry.register(Registries.ITEM, BLResources.BLOOD_PETAL_ID, BLOOD_PETAL);
        Registry.register(Registries.ITEM, BLResources.SILVER_INGOT_ID, SILVER_INGOT);
        Registry.register(Registries.ITEM, BLResources.RAW_SILVER_ID, RAW_SILVER);
        Registry.register(Registries.ITEM, BLResources.SILVER_SWORD_ID, SILVER_SWORD);
        Registry.register(Registries.ITEM, BLResources.SILVER_PICKAXE_ID, SILVER_PICKAXE);
        Registry.register(Registries.ITEM, BLResources.SILVER_AXE_ID, SILVER_AXE);
        Registry.register(Registries.ITEM, BLResources.SILVER_SHOVEL_ID, SILVER_SHOVEL);
        Registry.register(Registries.ITEM, BLResources.SILVER_HOE_ID, SILVER_HOE);

        // blocks
        Registry.register(Registries.ITEM, BLResources.ALTAR_ID, ALTAR);
        Registry.register(Registries.ITEM, BLResources.PEDESTAL_ID, PEDESTAL);
        Registry.register(Registries.ITEM, BLResources.SILVER_BLOCK_ID, SILVER_BLOCK);
        Registry.register(Registries.ITEM, BLResources.SILVER_ORE_ID, SILVER_ORE);
        Registry.register(Registries.ITEM, BLResources.DEEPSLATE_SILVER_ORE_ID, DEEPSLATE_SILVER_ORE);
        Registry.register(Registries.ITEM, BLResources.RAW_SILVER_BLOCK_ID, RAW_SILVER_BLOCK);
        Registry.register(Registries.ITEM, BLResources.HUNGRY_DECAYED_LOG, HUNGRY_DECAYED_LOG);
        Registry.register(Registries.ITEM, BLResources.STRIPPED_HUNGRY_DECAYED_LOG, STRIPPED_HUNGRY_DECAYED_LOG);
        Registry.register(Registries.ITEM, BLResources.DECAYED_LOG, DECAYED_LOG);
        Registry.register(Registries.ITEM, BLResources.STRIPPED_DECAYED_LOG, STRIPPED_DECAYED_LOG);
        Registry.register(Registries.ITEM, BLResources.STRIPPED_DECAYED_WOOD, STRIPPED_DECAYED_WOOD);
        Registry.register(Registries.ITEM, BLResources.DECAYED_WOOD, DECAYED_WOOD);
        Registry.register(Registries.ITEM, BLResources.DECAYED_TWIGS, DECAYED_TWIGS);
        Registry.register(Registries.ITEM, BLResources.GRAFTED_SAPLING, GRAFTED_SAPLING);
        Registry.register(Registries.ITEM, BLResources.SILVER_PRESSURE_PLATE, SILVER_PRESSURE_PLATE);
        Registry.register(Registries.ITEM, BLResources.DECAYED_PRESSURE_PLATE, DECAYED_PRESSURE_PLATE);

        Registry.register(Registries.ITEM, BLResources.DECAYED_PLANKS, DECAYED_PLANKS);
        Registry.register(Registries.ITEM, BLResources.DECAYED_FENCE, DECAYED_FENCE);
        Registry.register(Registries.ITEM, BLResources.DECAYED_FENCE_GATE, DECAYED_FENCE_GATE);
        Registry.register(Registries.ITEM, BLResources.DECAYED_STAIRS, DECAYED_STAIRS);
        Registry.register(Registries.ITEM, BLResources.DECAYED_SLAB, DECAYED_SLAB);
        Registry.register(Registries.ITEM, BLResources.DECAYED_BUTTON, DECAYED_BUTTON);
        Registry.register(Registries.ITEM, BLResources.DECAYED_DOOR, DECAYED_DOOR);
        Registry.register(Registries.ITEM, BLResources.DECAYED_TRAPDOOR, DECAYED_TRAPDOOR);
        Registry.register(Registries.ITEM, BLResources.DECAYED_SIGN, DECAYED_SIGN);
        Registry.register(Registries.ITEM, BLResources.DECAYED_HANGING_SIGN, DECAYED_HANGING_SIGN);

        CompostingChanceRegistry.INSTANCE.add(BLBlocks.DECAYED_TWIGS, 0.15f);
        CompostingChanceRegistry.INSTANCE.add(BLBlocks.GRAFTED_SAPLING, 0.3f);
        CompostingChanceRegistry.INSTANCE.add(BLOOD_PETAL, 0.4f);
    }

    public static Item createBlock(Block block, Item.Settings settings, BiFunction<Block, Item.Settings, Item> factory) {
        return factory.apply(block, settings);
    }

    public static Item createBlock(Block block) {
        return createBlock(block, new Item.Settings(), BlockItem::new);
    }

    public static Item createBlock(Block block, Item.Settings settings) {
        return createBlock(block, settings, BlockItem::new);
    }
}
