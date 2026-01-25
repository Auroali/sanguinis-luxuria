package com.auroali.sanguinisluxuria.common.items;

import com.auroali.sanguinisluxuria.common.VampireHungerManager;
import com.auroali.sanguinisluxuria.common.blood.BloodConstants;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.registry.SLBlocks;
import com.auroali.sanguinisluxuria.common.registry.SLSounds;
import com.auroali.sanguinisluxuria.util.VampireHelper;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DrinkableBloodItem extends Item implements BloodStorageItem, EntityTrackingItem {
    public static final FoodComponent BLOOD_FOOD_COMPONENT = new FoodComponent.Builder()
      .hunger(1)
      .saturationModifier(0.05f)
      .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 360), 0.4f)
      .alwaysEdible()
      .build();

    private final int maxBlood;
    private final int bloodPerDrink;

    /**
     * Constructs a DrinkableBloodItem
     *
     * @param maxBlood the maximum amount of blood this item can hold (by default)
     * @param settings the item's settings. It's recommended you set the food component to the blood one
     * @see DrinkableBloodItem#BLOOD_FOOD_COMPONENT
     */
    public DrinkableBloodItem(int maxBlood, Settings settings) {
        this(maxBlood, BloodConstants.BLOOD_PER_BOTTLE, settings);
    }

    /**
     * Constructs a DrinkableBloodItem
     *
     * @param maxBlood      the maximum amount of blood this item can hold (by default)
     * @param bloodPerDrink the amount of blood to drain when an entity drinks this item
     * @param settings      the item's settings. It's recommended you set the food component to the blood one
     * @see DrinkableBloodItem#BLOOD_FOOD_COMPONENT
     */
    public DrinkableBloodItem(int maxBlood, int bloodPerDrink, Settings settings) {
        super(settings.food(BLOOD_FOOD_COMPONENT));
        this.maxBlood = maxBlood;
        this.bloodPerDrink = bloodPerDrink;
    }

    @Override
    public int getDefaultMaxBlood() {
        return this.maxBlood;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        boolean creative = user instanceof PlayerEntity player && player.getAbilities().creativeMode;
        ItemStack usedStack = creative
          ? stack
          : stack.split(1);

        int bloodToDrain = Math.min(BloodStorageItem.getItemBlood(usedStack), this.maxBloodPerDrink());
        boolean drainedBlood = creative || BloodStorageItem.decrementItemBlood(
          usedStack,
          bloodToDrain
        );

        ItemStack result = BloodStorageItem.isItemEmpty(usedStack)
          ? BloodStorageItem.createEmptyStackFor(usedStack)
          : usedStack;
        if (VampireHelper.consumesBlood(user)) {
            // i wish we had java 21
            // would look so much nicer as a switch statement
            if (user instanceof PlayerEntity player) {
                ((VampireHungerManager) player.getHungerManager())
                  .sanguinisluxuria$addHunger(bloodToDrain, BloodConstants.SATURATION_PER_BLOOD);
            } else {
                BloodComponent.KEY.get(user)
                  .addBlood(bloodToDrain);
            }
        } else {
            if (drainedBlood)
                user.eatFood(world, usedStack.copy());
        }

        if (stack.isEmpty())
            return result;

        if (user instanceof PlayerEntity player && !creative) {
            if (!player.getInventory().insertStack(result))
                player.dropItem(result, false);
        }

        return stack;
    }

    @Override
    public SoundEvent getDrinkSound() {
        return SLSounds.DRAIN_BLOOD;
    }

    @Override
    public SoundEvent getEatSound() {
        return SLSounds.DRAIN_BLOOD;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return !BloodStorageItem.isItemEmpty(stack) ? UseAction.DRINK : UseAction.NONE;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (BloodStorageItem.isItemEmpty(user.getStackInHand(hand)))
            return TypedActionResult.pass(user.getStackInHand(hand));
        return super.use(world, user, hand);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        if (!BloodStorageItem.isItemDrainable(stack) || BloodStorageItem.getItemBlood(stack) < BloodConstants.BLOOD_PER_BOTTLE)
            return super.useOnBlock(context);

        ItemPlacementContext placementContext = new ItemPlacementContext(context);
        if (!placementContext.canPlace() || player == null || !player.isSneaking())
            return super.useOnBlock(context);

        BlockPos pos = placementContext.getBlockPos();
        BlockState bloodState = SLBlocks.BLOOD_SPLATTER.getPlacementState(placementContext);
        ShapeContext shapeContext = ShapeContext.of(player);
        // check to make sure the blockstate isn't null and that it can be placed at
        // the location. returns fail here if it can't be placed
        if (
          bloodState == null
            || !bloodState.canPlaceAt(world, pos)
            || !world.canPlace(bloodState, pos, shapeContext)
        ) {
            return ActionResult.FAIL;
        }

        // place the block
        world.setBlockState(pos, bloodState, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);

        // if the player is in survival, drain blood from the bottle
        if (!player.getAbilities().creativeMode) {
            ItemStack drained = stack.split(1);
            BloodStorageItem.decrementItemBlood(drained, BloodConstants.BLOOD_PER_BOTTLE);
            if (BloodStorageItem.isItemEmpty(drained))
                drained = BloodStorageItem.createEmptyStackFor(drained);

            if (stack.isEmpty())
                player.setStackInHand(context.getHand(), drained);
            else if (!player.getInventory().insertStack(drained)) {
                player.dropItem(drained, false);
            }
        }
        // play the bottle empty sound and emit the block place game event
        world.playSound(player, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
        world.emitGameEvent(
          GameEvent.BLOCK_PLACE,
          pos,
          GameEvent.Emitter.of(player, bloodState)
        );

        return ActionResult.success(world.isClient);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        int blood = BloodStorageItem.getItemBlood(stack);
        return blood > 0 && blood < BloodStorageItem.getItemMaxBlood(stack);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return Math.round(13.f * BloodStorageItem.getItemBlood(stack) / BloodStorageItem.getItemMaxBlood(stack));
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0xFFDF0000;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        Text entityName = this.getEntityName(stack);
        if (entityName != null) {
            tooltip.add(entityName.copy().formatted(Formatting.DARK_RED));
        }
    }

    @Override
    public void setBlood(ItemStack stack, int blood) {
        if (this.getBlood(stack) > blood)
            EntityTrackingItem.clearEntity(stack);
        BloodStorageItem.super.setBlood(stack, blood);
    }

    /**
     * @return the maximum amount of blood that can be drained in a single drink
     */
    protected int maxBloodPerDrink() {
        return this.bloodPerDrink;
    }
}
