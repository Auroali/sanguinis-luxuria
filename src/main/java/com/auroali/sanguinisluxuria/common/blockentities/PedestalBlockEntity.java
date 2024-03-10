package com.auroali.sanguinisluxuria.common.blockentities;

import com.auroali.sanguinisluxuria.common.registry.BLBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class PedestalBlockEntity extends BlockEntity {
    SimpleInventory inv = new SimpleInventory(ItemStack.EMPTY);

    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BLBlockEntities.PEDESTAL, pos, state);
        inv.addListener(inv -> {
            if(world != null && !world.isClient)
                world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
        });
    }

    public ItemStack getItem() {
        return inv.getStack(0);
    }

    public void setItem(ItemStack stack) {
        inv.setStack(0, stack);
        if(world != null) {
            BlockState state = world.getBlockState(pos);
            world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
        }
        markDirty();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inv.setStack(0, ItemStack.fromNbt(nbt.getCompound("Item")));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put("Item", inv.getStack(0).writeNbt(new NbtCompound()));
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound data = new NbtCompound();
        data.put("Item", inv.getStack(0).writeNbt(new NbtCompound()));
        return data;
    }

    public Inventory getInventory() {
        return inv;
    }
}
