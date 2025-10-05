package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

public class SLItemGroups {
    public static final RegistryKey<ItemGroup> SANGUINIS_LUXURIA_TAB = RegistryKey.of(RegistryKeys.ITEM_GROUP, SLResources.ITEM_GROUP_ID);

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, SLResources.ITEM_GROUP_ID, FabricItemGroup.builder()
          .icon(() -> BloodStorageItem.createStack(SLItems.BLOOD_BOTTLE))
          .displayName(Text.translatable("itemGroup.sanguinisluxuria.sanguinisluxuria"))
          .entries((displayContext, entries) -> {
              entries.add(BloodStorageItem.createStack(SLItems.BLOOD_BOTTLE));
              entries.add(SLItems.TWISTED_BLOOD);
              entries.add(BloodStorageItem.createStack(SLItems.BLOOD_BAG, 0));
              entries.add(BloodStorageItem.createStack(SLItems.BLOOD_BAG));
              entries.add(SLItems.MASK_1);
              entries.add(SLItems.MASK_2);
              entries.add(SLItems.MASK_3);
              entries.add(SLItems.BLOOD_PETAL);
              entries.add(SLItems.PENDANT_OF_PIERCING);
              entries.add(SLItems.SILVER_SWORD);
              entries.add(SLItems.SILVER_PICKAXE);
              entries.add(SLItems.SILVER_AXE);
              entries.add(SLItems.SILVER_SHOVEL);
              entries.add(SLItems.SILVER_HOE);
              entries.add(SLItems.SILVER_INGOT);
              entries.add(SLItems.SILVER_BLOCK);
              entries.add(SLItems.SILVER_PRESSURE_PLATE);
              entries.add(SLItems.SILVER_BARS);
              entries.add(SLItems.RAW_SILVER);
              entries.add(SLItems.RAW_SILVER_BLOCK);
              entries.add(SLItems.SILVER_ORE);
              entries.add(SLItems.DEEPSLATE_SILVER_ORE);
              entries.add(SLItems.ALTAR);
              entries.add(SLItems.PEDESTAL);
              entries.add(SLItems.GRAFTED_SAPLING);
              entries.add(SLItems.DECAYED_TWIGS);
              entries.add(SLItems.HUNGRY_DECAYED_LOG);
              entries.add(SLItems.DECAYED_LOG);
              entries.add(SLItems.DECAYED_WOOD);
              entries.add(SLItems.STRIPPED_HUNGRY_DECAYED_LOG);
              entries.add(SLItems.STRIPPED_DECAYED_LOG);
              entries.add(SLItems.STRIPPED_DECAYED_WOOD);
              entries.add(SLItems.DECAYED_PLANKS);
              entries.add(SLItems.DECAYED_SLAB);
              entries.add(SLItems.DECAYED_STAIRS);
              entries.add(SLItems.DECAYED_FENCE);
              entries.add(SLItems.DECAYED_FENCE_GATE);
              entries.add(SLItems.DECAYED_TRAPDOOR);
              entries.add(SLItems.DECAYED_DOOR);
              entries.add(SLItems.DECAYED_SIGN);
              entries.add(SLItems.DECAYED_HANGING_SIGN);
              entries.add(SLItems.DECAYED_BUTTON);
              entries.add(SLItems.DECAYED_PRESSURE_PLATE);
              entries.add(SLItems.VAMPIRE_VILLAGER_SPAWN_EGG);
              entries.add(PotionUtil.setPotion(new ItemStack(Items.POTION), SLStatusEffects.BLESSED_WATER_POTION));
              entries.add(PotionUtil.setPotion(new ItemStack(Items.POTION), SLStatusEffects.BLESSED_WATER_POTION_TWO));
              entries.add(PotionUtil.setPotion(new ItemStack(Items.POTION), SLStatusEffects.BLOOD_LUST_POTION));
              entries.add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), SLStatusEffects.BLESSED_WATER_POTION));
              entries.add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), SLStatusEffects.BLESSED_WATER_POTION_TWO));
              entries.add(PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), SLStatusEffects.BLOOD_LUST_POTION));
              entries.add(PotionUtil.setPotion(new ItemStack(Items.LINGERING_POTION), SLStatusEffects.BLESSED_WATER_POTION));
              entries.add(PotionUtil.setPotion(new ItemStack(Items.LINGERING_POTION), SLStatusEffects.BLESSED_WATER_POTION_TWO));
              entries.add(PotionUtil.setPotion(new ItemStack(Items.LINGERING_POTION), SLStatusEffects.BLOOD_LUST_POTION));
              entries.add(PotionUtil.setPotion(new ItemStack(Items.TIPPED_ARROW), SLStatusEffects.BLESSED_WATER_POTION));
              entries.add(PotionUtil.setPotion(new ItemStack(Items.TIPPED_ARROW), SLStatusEffects.BLESSED_WATER_POTION_TWO));
              entries.add(PotionUtil.setPotion(new ItemStack(Items.TIPPED_ARROW), SLStatusEffects.BLOOD_LUST_POTION));
          })
          .build());

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS)
          .register(entries -> {
              entries.add(SLItems.VAMPIRE_VILLAGER_SPAWN_EGG);
          });
    }
}
