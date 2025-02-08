package com.auroali.sanguinisluxuria.common.rituals;

import com.auroali.sanguinisluxuria.common.registry.BLRitualTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class ItemRitual implements Ritual, ItemCreatingRitual {
    public static final Codec<ItemRitual> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(
        ItemStack.CODEC.fieldOf("result").forGetter(ItemRitual::getOutput)
      ).apply(instance, ItemRitual::new)
    );

    protected final ItemStack output;

    public ItemRitual(ItemStack stack) {
        this.output = stack;
    }

    public ItemRitual(ItemConvertible item, int output) {
        this(new ItemStack(item, output));
    }

    public ItemRitual(ItemConvertible item) {
        this(item, 1);
    }

    @Override
    public void onCompleted(RitualParameters parameters) {
        this.spawnResultItem(parameters, this.createResultItem(parameters));
    }

    protected ItemStack createResultItem(RitualParameters parameters) {
        return this.getOutput();
    }

    protected void spawnResultItem(RitualParameters parameters, ItemStack stack) {
        Vec3d centerPos = parameters.pos().toCenterPos();
        ItemEntity entity = new ItemEntity(
          parameters.world(),
          centerPos.getX(),
          centerPos.getY() + 1,
          centerPos.getZ(),
          stack.copy());
        parameters.world().spawnEntity(entity);
    }

    @Override
    public RitualType<?> getType() {
        return BLRitualTypes.ITEM_RITUAL_TYPE;
    }

    @Override
    public ItemStack getOutput() {
        return this.output.copy();
    }
}
