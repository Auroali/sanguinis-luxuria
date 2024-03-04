package com.auroali.bloodlust.common.blockentities;

import com.auroali.bloodlust.common.registry.BLBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class PedestalBlockEntity extends BlockEntity {
    ItemStack stack = ItemStack.EMPTY;
    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BLBlockEntities.PEDESTAL, pos, state);
    }

    public ItemStack getItem() {
        return stack;
    }

    public void setItem(ItemStack stack) {
        this.stack = stack;
        markDirty();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        stack = ItemStack.fromNbt(nbt.getCompound("Item"));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put("Item", stack.writeNbt(new NbtCompound()));
    }
}
