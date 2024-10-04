package com.auroali.sanguinisluxuria.common.registry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;

import java.util.List;

public class BLTradeOffers {
    public static void registerClericTrades(List<TradeOffers.Factory> tradeOffers) {
        tradeOffers.add((entity, random) -> {
            ItemStack mundanePotion = new ItemStack(Items.POTION);
            PotionUtil.setPotion(mundanePotion, Potions.MUNDANE);
            ItemStack blessedWater = new ItemStack(Items.POTION);
            PotionUtil.setPotion(blessedWater, BLStatusEffects.BLESSED_WATER_POTION);
            return new TradeOffer(
              mundanePotion,
              new ItemStack(Items.EMERALD, 10),
              blessedWater,
              2,
              30,
              0.2f

            );
        });
    }
}
