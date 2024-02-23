package com.auroali.bloodlust.common.registry;

import com.auroali.bloodlust.BLResources;
import com.auroali.bloodlust.Bloodlust;
import com.auroali.bloodlust.common.items.BloodStorageItem;
import com.auroali.bloodlust.common.items.MaskItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
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

    public static void register() {
        Registry.register(Registry.ITEM, BLResources.MASK_ONE_ID, MASK_1);
        Registry.register(Registry.ITEM, BLResources.MASK_TWO_ID, MASK_2);
        Registry.register(Registry.ITEM, BLResources.MASK_THREE_ID, MASK_3);
        Registry.register(Registry.ITEM, BLResources.BLOOD_BAG_ID, BLOOD_BAG);
        Registry.register(Registry.ITEM, BLResources.BLOOD_BOTTLE_ID, BLOOD_BOTTLE);
    }
}
