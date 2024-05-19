package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.item.ItemStack;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;

public class BLVampireVillagerTrades {
    public static ImmutableMap<Integer, TradeOffers.Factory[]> TRADES = new ImmutableMap.Builder<Integer, TradeOffers.Factory[]>()
            .put(1, new TradeOffers.Factory[] {
                    new TradeOffers.SellItemFactory(BloodStorageItem.setStoredBlood(new ItemStack(BLItems.BLOOD_BOTTLE), 2), 2, 1, 4, 2),
                    new TradeOffers.SellItemFactory(BLItems.MASK_1, 3, 1, 1, 2)
            })
            .build();
}
