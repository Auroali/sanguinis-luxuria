package com.auroali.sanguinisluxuria.datagen.patchouli;

import com.mojang.datafixers.util.Either;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IVariable;

import java.util.Arrays;
import java.util.List;

public class PatchouliJsonIcon {
    private static final NbtCompound EXCLUDED_NBT;

    static {
        EXCLUDED_NBT = new NbtCompound();
        EXCLUDED_NBT.putInt("Damage", 0);
    }

    private final Identifier textureIcon;
    private final List<Either<ItemStack, TagKey<Item>>> itemstackIcon;

    private PatchouliJsonIcon(Identifier textureIcon, List<Either<ItemStack, TagKey<Item>>> itemstackIcon) {
        if (textureIcon != null && itemstackIcon != null)
            throw new IllegalStateException("must be either an itemstack icon or a texture icon, not both!");
        if (textureIcon == null && itemstackIcon == null)
            throw new IllegalStateException("cannot create empty icon");
        this.textureIcon = textureIcon;
        this.itemstackIcon = itemstackIcon;
    }

    public static PatchouliJsonIcon stack(ItemStack stack) {
        return new PatchouliJsonIcon(null, List.of(Either.left(stack)));
    }

    public static PatchouliJsonIcon stacks(ItemStack... stacks) {
        return new PatchouliJsonIcon(null, Arrays.stream(stacks).map(Either::<ItemStack, TagKey<Item>>left).toList());
    }

    public static PatchouliJsonIcon item(ItemConvertible item) {
        return new PatchouliJsonIcon(null, List.of(Either.left(new ItemStack(item))));
    }

    public static PatchouliJsonIcon items(ItemConvertible... items) {
        return new PatchouliJsonIcon(
          null,
          Arrays.stream(items)
            .map(ItemStack::new)
            .map(Either::<ItemStack, TagKey<Item>>left)
            .toList()
        );
    }

    public static PatchouliJsonIcon tag(TagKey<Item> tag) {
        return new PatchouliJsonIcon(null, List.of(Either.right(tag)));
    }

    @SafeVarargs
    public static PatchouliJsonIcon tags(TagKey<Item>... tags) {
        return new PatchouliJsonIcon(null, Arrays.stream(tags).map(Either::<ItemStack, TagKey<Item>>right).toList());
    }

    public static PatchouliJsonIcon texture(Identifier texture) {
        return new PatchouliJsonIcon(texture, null);
    }

    public Type type() {
        return this.textureIcon != null ? Type.TEXTURE : Type.STACK;
    }

    @Override
    public String toString() {
        return switch (this.type()) {
            case STACK -> toStackString(this.itemstackIcon);
            case TEXTURE -> this.textureIcon.toString();
        };
    }

    public static String toStackString(List<Either<ItemStack, TagKey<Item>>> stacks) {
        StringBuilder result = new StringBuilder();
        for (Either<ItemStack, TagKey<Item>> entry : stacks) {
            entry.ifLeft(stack -> {
                  if (!result.isEmpty())
                      result.append(',');
                  if (stack.isEmpty())
                      result.append("minecraft:air");
                  result.append(Registries.ITEM.getId(stack.getItem()));
                  if (stack.getCount() != 1)
                      result.append("#" + stack.getCount());

                  if (stack.hasNbt() && !NbtHelper.matches(EXCLUDED_NBT, stack.getNbt(), true)) {
                      NbtCompound nbt = stack.getNbt();
                      result.append(nbt.asString());
                  }
              })
              .ifRight(tag -> {
                  if (!result.isEmpty())
                      result.append(',');

                  result.append("tag:");
                  result.append(tag.id());
              });
        }
        return result.toString();
    }

    public static String toStackString(ItemStack stack) {
        return toStackString(List.of(Either.left(stack)));
    }

    public enum Type {
        TEXTURE,
        STACK
    }
}
