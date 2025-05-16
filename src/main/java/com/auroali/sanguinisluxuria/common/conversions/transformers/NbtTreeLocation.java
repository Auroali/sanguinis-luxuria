package com.auroali.sanguinisluxuria.common.conversions.transformers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.Arrays;

public sealed class NbtTreeLocation permits NbtTreeLocation.Empty {
    public static final Codec<NbtTreeLocation> CODEC = Codec.STRING
      .flatXmap(
        str -> {
            NbtTreeLocation location = NbtTreeLocation.fromString(str);
            return location == empty() ? DataResult.error(() -> "Invalid NBT path " + str) : DataResult.success(location);
        },
        tree -> tree == empty() ? DataResult.error(() -> "Cannot serialize empty path") : DataResult.success(tree.toString())
      );

    private static NbtTreeLocation EMPTY;

    private final String[] nodes;

    private NbtTreeLocation(String[] pathEntries) {
        this.nodes = pathEntries;
    }

    public static NbtTreeLocation fromString(String path) {
        String[] nodes = path.split("\\.");
        if (nodes.length == 0)
            return empty();
        return new NbtTreeLocation(nodes);
    }

    public static NbtTreeLocation empty() {
        return EMPTY == null ? EMPTY = new Empty() : EMPTY;
    }

    protected NbtCompound getParent(NbtCompound tag) {
        NbtCompound parent = tag;
        for (int i = 0; i < this.nodes.length - 1; i++) {
            NbtElement element = tag.get(this.nodes[i]);
            if (element == null || element.getType() != NbtElement.COMPOUND_TYPE)
                return null;
            parent = (NbtCompound) element;
        }
        return parent;
    }

    public NbtElement get(NbtCompound tag) {
        NbtCompound parent = this.getParent(tag);
        if (parent == null)
            return null;
        return parent.get(this.nodes[this.nodes.length - 1]);
    }

    public void insertInto(NbtCompound tag, NbtElement element) {
        NbtCompound parent = this.getParent(tag);
        if (parent == null)
            parent = this.createParent(tag);

        parent.put(this.nodes[this.nodes.length - 1], element);
    }

    private NbtCompound createParent(NbtCompound tag) {
        NbtCompound current = tag;
        for (int i = 0; i < this.nodes.length - 1; i++) {
            if (!current.contains(this.nodes[i], NbtElement.COMPOUND_TYPE))
                current.put(this.nodes[i], new NbtCompound());

            current = current.getCompound(this.nodes[i]);
        }
        return current;
    }

    @Override
    public String toString() {
        return String.join(".", this.nodes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.nodes);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof NbtTreeLocation path && Arrays.equals(this.nodes, path.nodes);
    }

    protected static final class Empty extends NbtTreeLocation {
        private Empty() {
            super(null);
        }

        @Override
        protected NbtCompound getParent(NbtCompound tag) {
            return null;
        }

        @Override
        public NbtElement get(NbtCompound tag) {
            return null;
        }

        @Override
        public void insertInto(NbtCompound tag, NbtElement element) {

        }

        @Override
        public String toString() {
            return "";
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }
    }
}
