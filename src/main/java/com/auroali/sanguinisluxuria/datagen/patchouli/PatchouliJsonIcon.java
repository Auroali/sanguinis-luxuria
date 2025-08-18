package com.auroali.sanguinisluxuria.datagen.patchouli;

import com.mojang.datafixers.util.Either;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IVariable;

public class PatchouliJsonIcon {
    private final Either<ItemStack, Identifier> icon;

    private PatchouliJsonIcon(Either<ItemStack, Identifier> icon) {
        this.icon = icon;
    }

    public static PatchouliJsonIcon stack(ItemStack stack) {
        return new PatchouliJsonIcon(Either.left(stack));
    }

    public static PatchouliJsonIcon texture(Identifier texture) {
        return new PatchouliJsonIcon(Either.right(texture));
    }

    @Override
    public String toString() {
        return this.icon.map(
          PatchouliJsonIcon::toStackString,
          Identifier::toString
        );
    }

    public static String toStackString(ItemStack stack) {
        if (stack.isEmpty())
            return "minecraft:air";
        String result = Registries.ITEM.getId(stack.getItem()).toString();
        if (stack.getCount() != 1)
            result += "#" + stack.getCount();

        if (stack.hasNbt()) {
            NbtCompound nbt = stack.getNbt();
            result += nbt.asString();
        }
        return result;
    }
}
