package com.auroali.sanguinisluxuria.common.blocks;

import com.auroali.sanguinisluxuria.VampireHelper;
import com.auroali.sanguinisluxuria.common.blood.BloodConstants;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.registry.*;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class HungryDecayedLogBlock extends PillarBlock {
    public static final IntProperty BLOOD_LEVEL = IntProperty.of("blood", 0, 3);

    public HungryDecayedLogBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
          this.getStateManager().getDefaultState()
            .with(BLOOD_LEVEL, 0)
            .with(AXIS, Direction.Axis.Y)
        );
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(BLOOD_LEVEL);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        // fill glass bottles
        if (stack.isOf(Items.GLASS_BOTTLE) && state.get(BLOOD_LEVEL) >= 3) {
            stack.decrement(1);
            ItemStack bloodBottle = BloodStorageItem.createStack(SLItems.BLOOD_BOTTLE);
            if (stack.isEmpty())
                player.setStackInHand(hand, bloodBottle);
            else if (!player.getInventory().insertStack(bloodBottle))
                player.dropItem(bloodBottle, false, false);

            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.setBlockState(pos, state.with(BLOOD_LEVEL, 0));

            return ActionResult.success(world.isClient);
        }
        // fill blood storing items
        if (stack.getItem() instanceof BloodStorageItem bloodStoringItem && bloodStoringItem.canFill() && state.get(BLOOD_LEVEL) >= 3) {
            if (BloodStorageItem.getItemBlood(stack) > BloodStorageItem.getItemMaxBlood(stack) - BloodConstants.BLOOD_PER_BOTTLE)
                return ActionResult.FAIL;

            BloodStorageItem.incrementItemBlood(stack, BloodConstants.BLOOD_PER_BOTTLE);

            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.setBlockState(pos, state.with(BLOOD_LEVEL, 0));

            return ActionResult.success(world.isClient);
        }
        return ActionResult.FAIL;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return (state.getBlock() == SLBlocks.STRIPPED_HUNGRY_DECAYED_LOG || state.get(BLOOD_LEVEL) < 3) && super.hasRandomTicks(state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(BLOOD_LEVEL) >= 3) {
            BlockPos lowerPosition = pos.down();
            for (Direction direction : Direction.Type.HORIZONTAL) {
                BlockState lowerState = world.getBlockState(lowerPosition.offset(direction));
                BlockState neighbourState = world.getBlockState(pos.offset(direction));

                if (!neighbourState.isAir())
                    continue;

                if (lowerState.isOf(SLBlocks.BLOOD_CAULDRON) && lowerState.get(LeveledCauldronBlock.LEVEL) < 3) {
                    int newLevel = lowerState.get(LeveledCauldronBlock.LEVEL) + 1;
                    world.setBlockState(lowerPosition.offset(direction), lowerState.with(LeveledCauldronBlock.LEVEL, newLevel));
                }
                if (lowerState.isOf(Blocks.CAULDRON)) {
                    world.setBlockState(lowerPosition.offset(direction), SLBlocks.BLOOD_CAULDRON.getDefaultState());
                }
            }
            world.setBlockState(pos, state.with(BLOOD_LEVEL, 0));
            return;
        }
        if (random.nextInt(13) == 0) {
            drainNearbyBlood(state, world, pos);
        }
    }

    private static void drainNearbyBlood(BlockState state, ServerWorld world, BlockPos pos) {
        int newLevel = state.get(BLOOD_LEVEL) + 1;
        Box boundingBox = new Box(pos).expand(5);

        List<LivingEntity> entities = world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), boundingBox, VampireHelper::hasBlood);
        if (entities.isEmpty())
            return;

        int start = world.getRandom().nextInt(entities.size());
        int end = start + entities.size();
        for (int i = start; i < end; i++) {
            LivingEntity entity = entities.get(i % entities.size());
            BloodComponent component = BloodComponent.KEY.get(entity);
            if (component.getBlood() <= 1 && !entity.getType().isIn(SLTags.Entities.IMMUNE_TO_BLOOD_LOSS))
                continue;
            if (component.drainBlood(1)) {
                world.playSound(null, pos, SLSounds.DRAIN_BLOOD, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.setBlockState(pos, state.with(BLOOD_LEVEL, newLevel));
                Box entityBox = entity.getBoundingBox();

                world.spawnParticles(
                  SLParticles.DRIPPING_BLOOD,
                  entityBox.getCenter().getX(),
                  entityBox.getCenter().getY(),
                  entityBox.getCenter().getZ(),
                  20,
                  entityBox.getXLength() / 2.d,
                  entityBox.getYLength() / 2.d,
                  entityBox.getZLength() / 2.d,
                  0.d
                );
                break;
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(BLOOD_LEVEL);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(BLOOD_LEVEL) < 3 || this != SLBlocks.STRIPPED_HUNGRY_DECAYED_LOG || random.nextInt(7) != 0)
            return;

        for (Direction direction : Direction.values()) {
            if (random.nextInt(2) != 0 || direction.getAxis() == state.get(AXIS))
                continue;

            Direction.Axis axis = direction.getAxis();
            double x = axis == Direction.Axis.X ? 0.5 + 0.5625 * direction.getOffsetX() : random.nextFloat();
            double y = axis == Direction.Axis.Y ? 0.5 + 0.5625 * direction.getOffsetY() : random.nextFloat();
            double z = axis == Direction.Axis.Z ? 0.5 + 0.5625 * direction.getOffsetZ() : random.nextFloat();

            world.addParticle(SLParticles.DRIPPING_BLOOD, pos.getX() + x, pos.getY() + y, pos.getZ() + z, 0, 0, 0);
        }
    }
}
