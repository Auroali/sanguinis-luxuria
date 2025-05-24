package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.blood.BloodConstants;
import com.auroali.sanguinisluxuria.common.items.DrinkableBloodItem;
import com.auroali.sanguinisluxuria.common.items.EmptyingDrinkableBloodItem;
import com.auroali.sanguinisluxuria.common.items.MaskItem;
import com.auroali.sanguinisluxuria.common.items.PendantOfPiercingItem;
import com.auroali.sanguinisluxuria.common.items.tools.*;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.BiFunction;

public class SLItems {
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
    public static final Item VAMPIRE_VILLAGER_SPAWN_EGG = new SpawnEggItem(SLEntities.VAMPIRE_VILLAGER, 0xFF1E1C1B, 0xFFF9f8EF, new Item.Settings());
    public static final Item PENDANT_OF_PIERCING = new PendantOfPiercingItem(new Item.Settings().maxCount(1));
    public static final Item BLOOD_PETAL = new Item(new Item.Settings());
    public static final Item SILVER_INGOT = new Item(new Item.Settings());
    public static final Item RAW_SILVER = new Item(new Item.Settings());
    public static final Item SILVER_SWORD = new BlessedSwordItem(SLToolMaterials.SILVER, 3, -2.4f, 4f, new Item.Settings());
    public static final Item SILVER_PICKAXE = new BlessedPickaxeItem(SLToolMaterials.SILVER, 1, -2.8f, 2, new Item.Settings());
    public static final Item SILVER_AXE = new BlessedAxeItem(SLToolMaterials.SILVER, 6, -3.1f, 2.5f, new Item.Settings());
    public static final Item SILVER_SHOVEL = new BlessedShovelItem(SLToolMaterials.SILVER, 1.5f, -3.f, 2, new Item.Settings());
    public static final Item SILVER_HOE = new BlessedHoeItem(SLToolMaterials.SILVER, 0, -1.f, 2, new Item.Settings());
    public static final Item VAMPIRE_FANG = new Item(new Item.Settings());

    // blocks
    public static final Item ALTAR = createBlock(SLBlocks.ALTAR);
    public static final Item PEDESTAL = createBlock(SLBlocks.PEDESTAL);
    public static final Item SILVER_BLOCK = createBlock(SLBlocks.SILVER_BLOCK);
    public static final Item SILVER_ORE = createBlock(SLBlocks.SILVER_ORE);
    public static final Item DEEPSLATE_SILVER_ORE = createBlock(SLBlocks.DEEPSLATE_SILVER_ORE);
    public static final Item RAW_SILVER_BLOCK = createBlock(SLBlocks.RAW_SILVER_BLOCK);
    public static final Item HUNGRY_DECAYED_LOG = createBlock(SLBlocks.HUNGRY_DECAYED_LOG);
    public static final Item DECAYED_LOG = createBlock(SLBlocks.DECAYED_LOG);
    public static final Item DECAYED_WOOD = createBlock(SLBlocks.DECAYED_WOOD);
    public static final Item STRIPPED_DECAYED_LOG = createBlock(SLBlocks.STRIPPED_DECAYED_LOG);
    public static final Item STRIPPED_HUNGRY_DECAYED_LOG = createBlock(SLBlocks.STRIPPED_HUNGRY_DECAYED_LOG);
    public static final Item STRIPPED_DECAYED_WOOD = createBlock(SLBlocks.STRIPPED_DECAYED_WOOD);
    public static final Item DECAYED_TWIGS = createBlock(SLBlocks.DECAYED_TWIGS);
    public static final Item GRAFTED_SAPLING = createBlock(SLBlocks.GRAFTED_SAPLING);
    public static final Item DECAYED_PRESSURE_PLATE = createBlock(SLBlocks.DECAYED_PRESSURE_PLATE);
    public static final Item DECAYED_PLANKS = createBlock(SLBlocks.DECAYED_PLANKS);
    public static final Item DECAYED_STAIRS = createBlock(SLBlocks.DECAYED_STAIRS);
    public static final Item DECAYED_SLAB = createBlock(SLBlocks.DECAYED_SLAB);
    public static final Item DECAYED_BUTTON = createBlock(SLBlocks.DECAYED_BUTTON);
    public static final Item DECAYED_FENCE = createBlock(SLBlocks.DECAYED_FENCE);
    public static final Item DECAYED_FENCE_GATE = createBlock(SLBlocks.DECAYED_FENCE_GATE);
    public static final Item DECAYED_DOOR = createBlock(SLBlocks.DECAYED_DOOR);
    public static final Item DECAYED_TRAPDOOR = createBlock(SLBlocks.DECAYED_TRAPDOOR);
    public static final Item DECAYED_SIGN = new SignItem(new Item.Settings().maxCount(16), SLBlocks.DECAYED_SIGN, SLBlocks.DECAYED_WALL_SIGN);
    public static final Item DECAYED_HANGING_SIGN = new HangingSignItem(SLBlocks.DECAYED_HANGING_SIGN, SLBlocks.DECAYED_WALL_HANGING_SIGN, new Item.Settings().maxCount(16));
    public static final Item SILVER_PRESSURE_PLATE = createBlock(SLBlocks.SILVER_PRESSURE_PLATE);
    public static final Item SILVER_BARS = createBlock(SLBlocks.SILVER_BARS);

    public static void register() {
        Registry.register(Registries.ITEM, SLResources.MASK_ONE_ID, MASK_1);
        Registry.register(Registries.ITEM, SLResources.MASK_TWO_ID, MASK_2);
        Registry.register(Registries.ITEM, SLResources.MASK_THREE_ID, MASK_3);
        Registry.register(Registries.ITEM, SLResources.BLOOD_BAG_ID, BLOOD_BAG);
        Registry.register(Registries.ITEM, SLResources.BLOOD_BOTTLE_ID, BLOOD_BOTTLE);
        Registry.register(Registries.ITEM, SLResources.TWISTED_BLOOD_ID, TWISTED_BLOOD);
        Registry.register(Registries.ITEM, SLResources.VAMPIRE_VILLAGER_SPAWN_EGG, VAMPIRE_VILLAGER_SPAWN_EGG);
        Registry.register(Registries.ITEM, SLResources.PENDANT_OF_PIERCING, PENDANT_OF_PIERCING);
        Registry.register(Registries.ITEM, SLResources.BLOOD_PETAL_ID, BLOOD_PETAL);
        Registry.register(Registries.ITEM, SLResources.SILVER_INGOT_ID, SILVER_INGOT);
        Registry.register(Registries.ITEM, SLResources.RAW_SILVER_ID, RAW_SILVER);
        Registry.register(Registries.ITEM, SLResources.SILVER_SWORD_ID, SILVER_SWORD);
        Registry.register(Registries.ITEM, SLResources.SILVER_PICKAXE_ID, SILVER_PICKAXE);
        Registry.register(Registries.ITEM, SLResources.SILVER_AXE_ID, SILVER_AXE);
        Registry.register(Registries.ITEM, SLResources.SILVER_SHOVEL_ID, SILVER_SHOVEL);
        Registry.register(Registries.ITEM, SLResources.SILVER_HOE_ID, SILVER_HOE);
        Registry.register(Registries.ITEM, SLResources.VAMPIRE_FANG_ID, VAMPIRE_FANG);

        // blocks
        Registry.register(Registries.ITEM, SLResources.ALTAR_ID, ALTAR);
        Registry.register(Registries.ITEM, SLResources.PEDESTAL_ID, PEDESTAL);
        Registry.register(Registries.ITEM, SLResources.SILVER_BLOCK_ID, SILVER_BLOCK);
        Registry.register(Registries.ITEM, SLResources.SILVER_ORE_ID, SILVER_ORE);
        Registry.register(Registries.ITEM, SLResources.DEEPSLATE_SILVER_ORE_ID, DEEPSLATE_SILVER_ORE);
        Registry.register(Registries.ITEM, SLResources.RAW_SILVER_BLOCK_ID, RAW_SILVER_BLOCK);
        Registry.register(Registries.ITEM, SLResources.HUNGRY_DECAYED_LOG_ID, HUNGRY_DECAYED_LOG);
        Registry.register(Registries.ITEM, SLResources.STRIPPED_HUNGRY_DECAYED_LOG_ID, STRIPPED_HUNGRY_DECAYED_LOG);
        Registry.register(Registries.ITEM, SLResources.DECAYED_LOG_ID, DECAYED_LOG);
        Registry.register(Registries.ITEM, SLResources.STRIPPED_DECAYED_LOG_ID, STRIPPED_DECAYED_LOG);
        Registry.register(Registries.ITEM, SLResources.STRIPPED_DECAYED_WOOD_ID, STRIPPED_DECAYED_WOOD);
        Registry.register(Registries.ITEM, SLResources.DECAYED_WOOD_ID, DECAYED_WOOD);
        Registry.register(Registries.ITEM, SLResources.DECAYED_TWIGS_ID, DECAYED_TWIGS);
        Registry.register(Registries.ITEM, SLResources.GRAFTED_SAPLING_ID, GRAFTED_SAPLING);
        Registry.register(Registries.ITEM, SLResources.SILVER_PRESSURE_PLATE_ID, SILVER_PRESSURE_PLATE);
        Registry.register(Registries.ITEM, SLResources.DECAYED_PRESSURE_PLATE_ID, DECAYED_PRESSURE_PLATE);

        Registry.register(Registries.ITEM, SLResources.DECAYED_PLANKS_ID, DECAYED_PLANKS);
        Registry.register(Registries.ITEM, SLResources.DECAYED_FENCE_ID, DECAYED_FENCE);
        Registry.register(Registries.ITEM, SLResources.DECAYED_FENCE_GATE_ID, DECAYED_FENCE_GATE);
        Registry.register(Registries.ITEM, SLResources.DECAYED_STAIRS_ID, DECAYED_STAIRS);
        Registry.register(Registries.ITEM, SLResources.DECAYED_SLAB_ID, DECAYED_SLAB);
        Registry.register(Registries.ITEM, SLResources.DECAYED_BUTTON_ID, DECAYED_BUTTON);
        Registry.register(Registries.ITEM, SLResources.DECAYED_DOOR_ID, DECAYED_DOOR);
        Registry.register(Registries.ITEM, SLResources.DECAYED_TRAPDOOR_ID, DECAYED_TRAPDOOR);
        Registry.register(Registries.ITEM, SLResources.DECAYED_SIGN_ID, DECAYED_SIGN);
        Registry.register(Registries.ITEM, SLResources.DECAYED_HANGING_SIGN_ID, DECAYED_HANGING_SIGN);
        Registry.register(Registries.ITEM, SLResources.SILVER_BARS_ID, SILVER_BARS);

        CompostingChanceRegistry.INSTANCE.add(SLBlocks.DECAYED_TWIGS, 0.15f);
        CompostingChanceRegistry.INSTANCE.add(SLBlocks.GRAFTED_SAPLING, 0.3f);
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
