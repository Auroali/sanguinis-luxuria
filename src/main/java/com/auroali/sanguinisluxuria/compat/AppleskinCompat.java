package com.auroali.sanguinisluxuria.compat;

import com.auroali.sanguinisluxuria.SLResources;
import com.auroali.sanguinisluxuria.common.registry.SLTags;
import com.auroali.sanguinisluxuria.util.VampireHelper;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.util.Identifier;
import squeek.appleskin.api.AppleSkinApi;
import squeek.appleskin.api.event.HUDOverlayEvent;

public class AppleskinCompat implements AppleSkinApi {
    private static final Identifier ICONS = SLResources.id("textures/gui/appleskin_icons.png");

    @Override
    public void registerEvents() {
        HUDOverlayEvent.Saturation.EVENT.addPhaseOrdering(Event.DEFAULT_PHASE, SLResources.AFTER_EVENT_PHASE);
        HUDOverlayEvent.Saturation.EVENT.register(SLResources.AFTER_EVENT_PHASE, this::cancelEventIfVampire);
        HUDOverlayEvent.HungerRestored.EVENT.register(this::cancelEventIfVampire);
        HUDOverlayEvent.HealthRestored.EVENT.register(event -> {
            if (!event.itemStack.isIn(SLTags.Items.VAMPIRES_GET_HUNGER_FROM))
                this.cancelEventIfVampire(event);
        });

    }

    public void cancelEventIfVampire(HUDOverlayEvent event) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (VampireHelper.consumesBlood(client.player)) {
            event.isCanceled = true;
            // based off https://github.com/squeek502/AppleSkin/blob/8af8a059e9c34fe4a175f1f17e3d2689206e197c/java/squeek/appleskin/client/HUDOverlayHandler.java#L178
            HungerManager manager = client.player.getHungerManager();
            int endSaturation = (int) Math.ceil(manager.getSaturationLevel() / 2.f);

            for (int i = 0; i < endSaturation; ++i) {
                int x = event.x - i * 8 - 9;
                int y = event.y;

                int u = 0;
                int v = 0;

                float effectiveSaturationOfBar = (manager.getSaturationLevel() / 2.f) - i;

                if (effectiveSaturationOfBar >= 1)
                    u = 27;
                else if (effectiveSaturationOfBar > 0.5)
                    u = 18;
                else if (effectiveSaturationOfBar > 0.25)
                    u = 9;

                event.context.drawTexture(ICONS, x, y, u, v, 9, 9);
            }
        }
    }
}
