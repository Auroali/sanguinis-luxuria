package com.auroali.bloodlust.common.blocks;

import com.auroali.bloodlust.common.blockentities.SkillUpgraderBlockEntity;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.VampireComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SkillUpgraderBlock extends Block implements BlockEntityProvider {
    public SkillUpgraderBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(player);
        if(!vampire.isVampire())
            return ActionResult.FAIL;

        vampire.setSkillPoints(vampire.getSkillPoints() + 1);
        return ActionResult.success(world.isClient);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SkillUpgraderBlockEntity(pos, state);
    }
}
