package com.auroali.sanguinisluxuria.common.rituals.types;

import com.auroali.sanguinisluxuria.common.registry.SLRitualTypes;
import com.auroali.sanguinisluxuria.common.rituals.ItemCreatingRitual;
import com.auroali.sanguinisluxuria.common.rituals.Ritual;
import com.auroali.sanguinisluxuria.common.rituals.RitualParameters;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class ItemRitual implements Ritual, ItemCreatingRitual {
    public static final Codec<ItemRitual> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(
        ItemStack.CODEC.fieldOf("result").forGetter(ItemRitual::getOutput),
        Codec.BOOL.optionalFieldOf("preserveCatalystNbt", false).forGetter(ritual -> ritual.preserveCatalystNbt)
      ).apply(instance, ItemRitual::new)
    );

    protected final ItemStack output;
    protected final boolean preserveCatalystNbt;

    protected ItemRitual(ItemStack stack, boolean preserveCatalystNbt) {
        this.output = stack;
        this.preserveCatalystNbt = preserveCatalystNbt;
    }

    @Override
    public void onCompleted(RitualParameters parameters) {
        this.spawnResultItem(parameters, this.createResultItem(parameters));
    }

    protected ItemStack createResultItem(RitualParameters parameters) {
        ItemStack outItem = this.getOutput();
        ItemStack catalyst = parameters.inventory().getStack(0);
        if (this.preserveCatalystNbt && catalyst.hasNbt()) {
            outItem.getOrCreateNbt().copyFrom(catalyst.getNbt());
        }

        return outItem;
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
        return SLRitualTypes.ITEM_RITUAL_TYPE;
    }

    @Override
    public ItemStack getOutput() {
        return this.output.copy();
    }

    public static ItemRitual create(ItemConvertible item, boolean preserveCatalystNbt) {
        return create(new ItemStack(item), preserveCatalystNbt);
    }

    public static ItemRitual create(ItemConvertible item) {
        return create(item, false);
    }

    public static ItemRitual create(ItemStack stack) {
        return create(stack, false);
    }

    public static ItemRitual create(ItemStack stack, boolean preserveCatalystNbt) {
        return new ItemRitual(stack, preserveCatalystNbt);
    }
}
