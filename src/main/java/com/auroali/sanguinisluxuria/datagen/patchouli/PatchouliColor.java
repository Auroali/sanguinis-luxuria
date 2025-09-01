package com.auroali.sanguinisluxuria.datagen.patchouli;

import java.util.regex.Pattern;

public class PatchouliColor {
    protected static final Pattern INVALID_CHARS = Pattern.compile("[^0-9a-fA-F]");
    protected static final int MASK = 0xFFFFFF;

    private final int value;

    protected PatchouliColor(int value) {
        this.value = value;
    }

    public static PatchouliColor fromHex(String hex) {
        if (hex.startsWith("#")) {
            String newHex = hex.substring(1, 7);
            if (newHex.contains("#"))
                throw new IllegalArgumentException("invalid character in " + newHex + ": valid chars are [0-9a-FA-F]");
            return fromHex(newHex);
        }

        if (hex.length() > 6) {
            return fromHex(hex.substring(0, 6));
        }

        if (INVALID_CHARS.matcher(hex).find())
            throw new IllegalArgumentException("invalid character in " + hex + ": valid chars are [0-9a-FA-F]");

        return new PatchouliColor(Integer.parseInt(hex, 16) & 0xFFFFFF);
    }

    public static PatchouliColor fromRGB(int r, int g, int b) {
        return new PatchouliColor((r << 16 | g << 8 | b) & MASK);
    }

    public static PatchouliColor from(int value) {
        return new PatchouliColor(value & MASK);
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof PatchouliColor p && p.value == this.value;
    }

    @Override
    public String toString() {
        return Integer.toHexString(this.value);
    }
}
