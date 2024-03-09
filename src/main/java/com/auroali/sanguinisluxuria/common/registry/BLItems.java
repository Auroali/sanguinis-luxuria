package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.BLResources;
import com.auroali.sanguinisluxuria.Bloodlust;
import com.auroali.sanguinisluxuria.common.items.BlessedBloodItem;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.items.MaskItem;
import com.auroali.sanguinisluxuria.common.items.TwistedBloodItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

public class BLItems {
    public static final MaskItem MASK_1 = new MaskItem(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1));
    public static final MaskItem MASK_2 = new MaskItem(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1));
    public static final MaskItem MASK_3 = new MaskItem(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1));
    public static final BloodStorageItem BLOOD_BAG = new BloodStorageItem(
            new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1),
            10
    );
    public static final BloodStorageItem BLOOD_BOTTLE = new BloodStorageItem(
            new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1).recipeRemainder(Items.GLASS_BOTTLE),
            2
    ).emptyItem(Items.GLASS_BOTTLE);
    public static final Item TWISTED_BLOOD = new TwistedBloodItem(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1));
    public static final Item BLESSED_BLOOD = new BlessedBloodItem(new FabricItemSettings().group(Bloodlust.BLOODLUST_TAB).maxCount(1));

    public static void register() {
        Registry.register(Registry.ITEM, BLResources.MASK_ONE_ID, MASK_1);
        Registry.register(Registry.ITEM, BLResources.MASK_TWO_ID, MASK_2);
        Registry.register(Registry.ITEM, BLResources.MASK_THREE_ID, MASK_3);
        Registry.register(Registry.ITEM, BLResources.BLOOD_BAG_ID, BLOOD_BAG);
        Registry.register(Registry.ITEM, BLResources.BLOOD_BOTTLE_ID, BLOOD_BOTTLE);
        Registry.register(Registry.ITEM, BLResources.TWISTED_BLOOD_ID, TWISTED_BLOOD);
        Registry.register(Registry.ITEM, BLResources.BLESSED_BLOOD_ID, BLESSED_BLOOD);

        // blocks
        Registry.register(Registry.ITEM, BLResources.SKILL_UPGRADER_ID, new BlockItem(BLBlocks.SKILL_UPGRADER, new FabricItemSettings()
                .group(Bloodlust.BLOODLUST_TAB))
        );
        Registry.register(Registry.ITEM, BLResources.PEDESTAL_ID, new BlockItem(BLBlocks.PEDESTAL, new FabricItemSettings()
                .group(Bloodlust.BLOODLUST_TAB))
        );
    }
}
