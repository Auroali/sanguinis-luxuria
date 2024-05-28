package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.Bloodlust;
import com.auroali.sanguinisluxuria.common.BloodConstants;
import com.auroali.sanguinisluxuria.common.items.*;
import com.auroali.sanguinisluxuria.common.items.tools.*;
import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.util.registry.Registry;

public class BLItems {
    public static final MaskItem MASK_1 = new MaskItem(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1));
    public static final MaskItem MASK_2 = new MaskItem(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1));
    public static final MaskItem MASK_3 = new MaskItem(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1));
    public static final BloodStorageItem BLOOD_BAG = new DrinkableBloodStorageItem(
            new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1),
            10 * BloodConstants.BLOOD_PER_BOTTLE
    );
    public static final BloodStorageItem BLOOD_BOTTLE = new DrinkableBloodStorageItem(
            new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1).recipeRemainder(Items.GLASS_BOTTLE),
            BloodConstants.BLOOD_PER_BOTTLE
    ).emptyItem(Items.GLASS_BOTTLE);
    public static final Item TWISTED_BLOOD = new TwistedBloodItem(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1));
    public static final Item BLESSED_BLOOD = new BlessedBloodItem(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1));
    public static final Item VAMPIRE_VILLAGER_SPAWN_EGG = new SpawnEggItem(BLEntities.VAMPIRE_VILLAGER, 0xFF1E1C1B, 0xFFF9f8EF, new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item PENDANT_OF_PIERCING = new TrinketItem(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1));
    public static final Item PENDANT_OF_TRANSFUSION = new TrinketItem(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1));
    public static final Item BLOOD_PETAL = new Item(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB));
    public static final Item SILVER_INGOT = new Item(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB));
    public static final Item RAW_SILVER = new Item(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB));
    public static final Item SILVER_SWORD = new BlessedSwordItem(BLToolMaterials.SILVER, 3, -2.4f, new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB));
    public static final Item SILVER_PICKAXE = new BlessedPickaxeItem(BLToolMaterials.SILVER, 1, -2.8f, new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB));
    public static final Item SILVER_AXE = new BlessedAxeItem(BLToolMaterials.SILVER, 6, -3.1f, new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB));
    public static final Item SILVER_SHOVEL = new BlessedShovelItem(BLToolMaterials.SILVER, 1.5f, -3.f, new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB));
    public static final Item SILVER_HOE = new BlessedHoeItem(BLToolMaterials.SILVER, -2, -1.f, new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB));

    public static void register() {
        Registry.register(Registry.ITEM, BLResources.MASK_ONE_ID, MASK_1);
        Registry.register(Registry.ITEM, BLResources.MASK_TWO_ID, MASK_2);
        Registry.register(Registry.ITEM, BLResources.MASK_THREE_ID, MASK_3);
        Registry.register(Registry.ITEM, BLResources.BLOOD_BAG_ID, BLOOD_BAG);
        Registry.register(Registry.ITEM, BLResources.BLOOD_BOTTLE_ID, BLOOD_BOTTLE);
        Registry.register(Registry.ITEM, BLResources.TWISTED_BLOOD_ID, TWISTED_BLOOD);
        Registry.register(Registry.ITEM, BLResources.BLESSED_BLOOD_ID, BLESSED_BLOOD);
        Registry.register(Registry.ITEM, BLResources.VAMPIRE_VILLAGER_SPAWN_EGG, VAMPIRE_VILLAGER_SPAWN_EGG);
        Registry.register(Registry.ITEM, BLResources.PENDANT_OF_PIERCING, PENDANT_OF_PIERCING);
        Registry.register(Registry.ITEM, BLResources.PENDANT_OF_TRANSFUSION, PENDANT_OF_TRANSFUSION);
        Registry.register(Registry.ITEM, BLResources.BLOOD_PETAL_ID, BLOOD_PETAL);
        Registry.register(Registry.ITEM, BLResources.SILVER_INGOT_ID, SILVER_INGOT);
        Registry.register(Registry.ITEM, BLResources.RAW_SILVER_ID, RAW_SILVER);
        Registry.register(Registry.ITEM, BLResources.SILVER_SWORD_ID, SILVER_SWORD);
        Registry.register(Registry.ITEM, BLResources.SILVER_PICKAXE_ID, SILVER_PICKAXE);
        Registry.register(Registry.ITEM, BLResources.SILVER_AXE_ID, SILVER_AXE);
        Registry.register(Registry.ITEM, BLResources.SILVER_SHOVEL_ID, SILVER_SHOVEL);
        Registry.register(Registry.ITEM, BLResources.SILVER_HOE_ID, SILVER_HOE);

        // blocks
        Registry.register(Registry.ITEM, BLResources.ALTAR_ID, new BlockItem(BLBlocks.ALTAR, new FabricItemSettings()
                .group(Bloodlust.BLOODLUST_TAB))
        );
        Registry.register(Registry.ITEM, BLResources.PEDESTAL_ID, new BlockItem(BLBlocks.PEDESTAL, new FabricItemSettings()
                .group(Bloodlust.BLOODLUST_TAB))
        );
        Registry.register(Registry.ITEM, BLResources.SILVER_BLOCK_ID, new BlockItem(BLBlocks.SILVER_BLOCK, new FabricItemSettings()
                .group(Bloodlust.BLOODLUST_TAB))
        );
        Registry.register(Registry.ITEM, BLResources.SILVER_ORE_ID, new BlockItem(BLBlocks.SILVER_ORE, new FabricItemSettings()
                .group(Bloodlust.BLOODLUST_TAB))
        );
        Registry.register(Registry.ITEM, BLResources.DEEPSLATE_SILVER_ORE_ID, new BlockItem(BLBlocks.DEEPSLATE_SILVER_ORE, new FabricItemSettings()
                .group(Bloodlust.BLOODLUST_TAB))
        );
    }
}
