package com.auroali.sanguinisluxuria.compat.emi;

import com.auroali.sanguinisluxuria.common.rituals.Ritual;
import com.auroali.sanguinisluxuria.common.rituals.RitualType;
import com.auroali.sanguinisluxuria.common.rituals.types.ItemRitual;
import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RitualEmiStack extends EmiStack {
    private final Ritual ritual;

    protected RitualEmiStack(Ritual ritual) {
        this.ritual = ritual;
    }

    public static EmiStack of(Ritual ritual) {
        if (ritual instanceof ItemRitual item && !item.getOutput().isEmpty())
            return EmiStack.of(item.getOutput());

        return new RitualEmiStack(ritual);
    }

    @Override
    public EmiStack copy() {
        return new RitualEmiStack(this.ritual);
    }

    @Override
    public void render(DrawContext draw, int x, int y, float delta, int flags) {
        draw.drawTexture(EmiCompat.TEXTURES, x, y, 0, 58, 16, 16);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public NbtCompound getNbt() {
        return null;
    }

    @Override
    public Object getKey() {
        return this.ritual;
    }

    @Override
    public Identifier getId() {
        return RitualType.getId(this.ritual.getType());
    }

    @Override
    public List<Text> getTooltipText() {
        List<Text> tooltips = new ArrayList<>();
        tooltips.add(Text.translatable(this.ritual.getType().getTranslationKey()).formatted(Formatting.GOLD));
        this.ritual.appendTooltips(tooltips);
        return tooltips;
    }

    @Override
    public List<TooltipComponent> getTooltip() {
        List<TooltipComponent> tooltips = this.getTooltipText().stream().map(EmiTooltipComponents::of).collect(Collectors.toList());
        tooltips.addAll(super.getTooltip());
        return tooltips;
    }

    @Override
    public Text getName() {
        return Text.translatable(this.ritual.getType().getTranslationKey());
    }

    @Override
    public ItemStack getItemStack() {
        return ItemStack.EMPTY;
    }
}
