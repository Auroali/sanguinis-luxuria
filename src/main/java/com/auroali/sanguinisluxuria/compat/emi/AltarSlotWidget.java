package com.auroali.sanguinisluxuria.compat.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.SlotWidget;
import net.minecraft.client.gui.DrawContext;

public class AltarSlotWidget extends SlotWidget {
    public AltarSlotWidget(EmiIngredient stack, int x, int y) {
        super(stack, x, y);
    }

    @Override
    public void drawStack(DrawContext draw, int mouseX, int mouseY, float delta) {
        if (!(this.stack instanceof RitualEmiStack))
            super.drawStack(draw, mouseX, mouseY, delta);
    }
}
