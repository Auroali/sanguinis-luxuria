package com.auroali.sanguinisluxuria.common.blocks;

import com.auroali.sanguinisluxuria.common.BloodConstants;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.registry.BLItems;
import com.auroali.sanguinisluxuria.common.registry.BLSounds;
import com.auroali.sanguinisluxuria.common.registry.BLTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class BloodDecayedLogBlock extends PillarBlock {
    public static final IntProperty BLOOD_LEVEL = IntProperty.of("blood", 0, 3);

    public BloodDecayedLogBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
          this.getStateManager().getDefaultState().with(BLOOD_LEVEL, 0)
        );
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(Items.GLASS_BOTTLE) && state.get(BLOOD_LEVEL) >= 3) {
            stack.decrement(1);
            ItemStack bloodBottle = BloodStorageItem.setStoredBlood(new ItemStack(BLItems.BLOOD_BOTTLE), BloodConstants.BLOOD_PER_BOTTLE);
            if (stack.isEmpty())
                player.setStackInHand(hand, bloodBottle);
            else if (!player.getInventory().insertStack(bloodBottle))
                player.dropItem(bloodBottle, false, false);

            world.setBlockState(pos, state.with(BLOOD_LEVEL, 0));

            return ActionResult.success(world.isClient);
        }
        return ActionResult.FAIL;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(BLOOD_LEVEL) < 3 && super.hasRandomTicks(state);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(5) == 0) {
            int newLevel = state.get(BLOOD_LEVEL) + 1;
            Box boundingBox = new Box(pos).expand(5);

            List<LivingEntity> entities = world.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), boundingBox, entity -> entity.getType().isIn(BLTags.Entities.HAS_BLOOD));

            for (LivingEntity entity : entities) {
                BloodComponent component = BLEntityComponents.BLOOD_COMPONENT.get(entity);
                if (component.drainBlood()) {
                    world.playSound(null, pos, BLSounds.DRAIN_BLOOD, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    world.setBlockState(pos, state.with(BLOOD_LEVEL, newLevel));
                    return;
                }
            }

        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(BLOOD_LEVEL);
    }
}
