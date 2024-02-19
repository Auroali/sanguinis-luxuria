package com.auroali.bloodlust;

import com.auroali.bloodlust.client.BLHud;
import com.auroali.bloodlust.common.registry.BLItems;
import com.auroali.bloodlust.common.registry.BLTags;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class BloodlustClient implements ClientModInitializer {
    public static KeyBinding SUCK_BLOOD = new KeyBinding(
            "key.bloodlust.bite",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "category.bloodlust.bloodlust"
    );

    public boolean drainingBlood;

    @Override
    public void onInitializeClient() {
        registerBindings();

        HudRenderCallback.EVENT.register(BLHud::render);
    }

    public void registerBindings() {
        SUCK_BLOOD = KeyBindingHelper.registerKeyBinding(SUCK_BLOOD);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (SUCK_BLOOD.isPressed()) {
                if (isLookingAtValidTarget()) {
                    sendBloodDrainPacket(true);
                    drainingBlood = true;
                }
            } else if (drainingBlood) {
                drainingBlood = false;
                sendBloodDrainPacket(false);
            }
        });

        TrinketRendererRegistry.registerRenderer(BLItems.MASK_1, BLItems.MASK_1);
        TrinketRendererRegistry.registerRenderer(BLItems.MASK_2, BLItems.MASK_2);
        TrinketRendererRegistry.registerRenderer(BLItems.MASK_3, BLItems.MASK_3);

    }

    public static boolean isLookingAtValidTarget() {
        MinecraftClient client = MinecraftClient.getInstance();
        if(!VampireHelper.isVampire(client.player))
            return false;

        HitResult result = client.crosshairTarget;
        LivingEntity target = result != null && result.getType() == HitResult.Type.ENTITY && ((EntityHitResult)result).getEntity() instanceof LivingEntity living ? living : null;

        return target != null && target.getType().isIn(BLTags.Entities.HAS_BLOOD);
    }

    public void sendBloodDrainPacket(boolean drain) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(drain);
        ClientPlayNetworking.send(BLResources.KEYBIND_CHANNEL, buf);
    }
}
