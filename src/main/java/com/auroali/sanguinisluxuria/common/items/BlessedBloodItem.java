package com.auroali.sanguinisluxuria.common.items;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.BLAdvancementCriterion;
import com.auroali.sanguinisluxuria.common.registry.BLDamageSources;
import com.auroali.sanguinisluxuria.common.registry.BLSounds;
import com.auroali.sanguinisluxuria.config.BLConfig;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class BlessedBloodItem extends Item {
    public BlessedBloodItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        if (!world.isClient && VampireHelper.isVampire(user)) {
            if(!(user instanceof PlayerEntity player && player.isCreative())) {
                user.damage(BLDamageSources.BLESSED_BLOOD, 19);
                if (!user.isAlive())
                    return new ItemStack(Items.GLASS_BOTTLE);
            }

            VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(user);
            for(VampireAbility ability : vampire.getAbilties()) {
                ability.onAbilityRemoved(user, vampire);
                vampire.getAbilties().removeAbility(ability);
                if(user instanceof ServerPlayerEntity player)
                    BLAdvancementCriterion.RESET_ABILITIES.trigger(player);
            }

            vampire.setSkillPoints(vampire.getLevel() * BLConfig.INSTANCE.skillPointsPerLevel);
        }

        return new ItemStack(Items.GLASS_BOTTLE);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 40;
    }

    @Override
    public SoundEvent getDrinkSound() {
        return BLSounds.DRAIN_BLOOD;
    }
}
