package com.auroali.sanguinisluxuria.common.registry;

import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.google.common.collect.ImmutableMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SLVampireVillagerTrades {
    public static final ImmutableMap<Integer, TradeOffers.Factory[]> TRADES = new ImmutableMap.Builder<Integer, TradeOffers.Factory[]>()
      .put(1, new TradeOffers.Factory[]{
        new TradeOffers.SellItemFactory(SLItems.MASK_1, 3, 1, 1, 2),
        new TradeOffers.SellItemFactory(SLItems.MASK_2, 3, 1, 1, 2),
        new TradeOffers.SellItemFactory(SLItems.MASK_3, 3, 1, 1, 2),
        new TradeOffers.BuyForOneEmeraldFactory(SLItems.TWISTED_BLOOD, 1, 3, 4),
        new TradeOffers.BuyForOneEmeraldFactory(SLItems.BLOOD_PETAL, 1, 3, 1),
        new VampireEnchantedBookFactory(3)
      })
      .put(2, new TradeOffers.Factory[]{
        new SellPotionFactory(Items.POTION, Potions.FIRE_RESISTANCE, 16, 1, 1, 4),
        new TradeOffers.BuyForOneEmeraldFactory(SLItems.GRAFTED_SAPLING, 1, 1, 4),
        new RefillBloodItemFactory(SLItems.BLOOD_BAG, 4, 1, 4),
        new NbtAwareSellItemFactory(BloodStorageItem.createStack(SLItems.BLOOD_BOTTLE), 8, 1, 8, 2),
      })
      .build();

    public static class SellPotionFactory implements TradeOffers.Factory {
        final int cost;
        final int count;
        final int maxUses;
        final int experience;
        final Item potionItem;
        final Potion potion;

        public SellPotionFactory(Item item, Potion potion, int price, int count, int maxUses, int experience) {
            this.potionItem = item;
            this.potion = potion;
            this.cost = price;
            this.maxUses = maxUses;
            this.count = count;
            this.experience = experience;
        }

        @Nullable
        @Override
        public TradeOffer create(Entity entity, Random random) {
            return new TradeOffer(new ItemStack(Items.EMERALD, this.cost), PotionUtil.setPotion(new ItemStack(this.potionItem, this.count), this.potion), this.maxUses, this.experience, 0.2f);
        }
    }

    // modified version of TradeOffers.EnchantBook that only offers vampire enchantments
    public static class VampireEnchantedBookFactory implements TradeOffers.Factory {
        final int experience;

        public VampireEnchantedBookFactory(int experience) {
            this.experience = experience;
        }

        @Nullable
        @Override
        public TradeOffer create(Entity entity, Random random) {
            List<Enchantment> validEnchantments = SLTags.getAllEntriesInTag(SLTags.Enchantments.VAMPIRE_MERCHANT_OFFERS, Registries.ENCHANTMENT);
            Enchantment enchantment = validEnchantments.get(random.nextInt(validEnchantments.size()));
            int level = MathHelper.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack book = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, level));
            int cost = 2 + random.nextInt(5 + level * 10) + 3 * level;
            if (enchantment.isTreasure())
                cost *= 2;


            return new TradeOffer(
              new ItemStack(Items.EMERALD, Math.min(cost, 64)),
              new ItemStack(Items.BOOK),
              book,
              12,
              this.experience,
              0.2f
            );
        }
    }

    public static class NbtAwareSellItemFactory implements TradeOffers.Factory {
        private final ItemStack sell;
        private final NbtCompound nbt;
        private final int cost;
        private final int maxUses;
        private final int experience;

        public NbtAwareSellItemFactory(ItemStack sell, NbtCompound nbt, int cost, int maxUses, int experience) {
            this.sell = sell;
            this.nbt = nbt;
            this.cost = cost;
            this.maxUses = maxUses;
            this.experience = experience;
        }

        public NbtAwareSellItemFactory(ItemStack sell, int cost, int maxUses, int experience) {
            this(new ItemStack(sell.getItem(), sell.getCount()), sell.getNbt(), cost, maxUses, experience);
        }

        public NbtAwareSellItemFactory(ItemStack sell, int cost, int count, int maxUses, int experience) {
            this(new ItemStack(sell.getItem(), count), sell.getNbt(), cost, maxUses, experience);
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            ItemStack sell = new ItemStack(this.sell.getItem(), this.sell.getCount());
            if (this.nbt != null)
                sell.setNbt(this.nbt);
            return new TradeOffer(
              new ItemStack(Items.EMERALD, this.cost),
              ItemStack.EMPTY,
              sell,
              this.maxUses,
              this.experience,
              0.05f
            );
        }
    }

    public static class RefillBloodItemFactory implements TradeOffers.Factory {
        private final ItemConvertible itemIn;
        private final int cost;
        private final int maxUses;
        private final int experience;

        public RefillBloodItemFactory(ItemConvertible itemIn, int cost, int maxUses, int experience) {
            this.itemIn = itemIn;
            this.cost = cost;
            this.maxUses = maxUses;
            this.experience = experience;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            return new TradeOffer(
              new ItemStack(this.itemIn),
              new ItemStack(Items.EMERALD, this.cost),
              BloodStorageItem.createStack(this.itemIn.asItem()),
              this.maxUses,
              this.experience,
              0.2f
            );
        }
    }
}
