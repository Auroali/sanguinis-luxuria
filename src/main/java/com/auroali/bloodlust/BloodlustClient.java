package com.auroali.bloodlust;

import com.auroali.bloodlust.client.BLHud;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.registry.BLTags;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.hit.EntityHitResult;
import org.lwjgl.glfw.GLFW;

public class BloodlustClient implements ClientModInitializer {
    public static KeyBinding SUCK_BLOOD = new KeyBinding(
            "key.bloodlust.bite",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Y,
            "category.bloodlust.bloodlust"
    );

    public static final int BLOOD_TIMER_LENGTH = 10;

    public static int suckBloodTimer = 0;
    public static LivingEntity targetEntity;

    @Override
    public void onInitializeClient() {
        registerBindings();

        HudRenderCallback.EVENT.register(BLHud::render);
    }

    public void registerBindings() {
        SUCK_BLOOD = KeyBindingHelper.registerKeyBinding(SUCK_BLOOD);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            updateTarget();
            if(SUCK_BLOOD.isPressed())
                trySuckBlood();
            else {
                suckBloodTimer = 0;
            }
        });

    }

    public void updateTarget() {
        MinecraftClient client = MinecraftClient.getInstance();
        if(client.player == null)
            return;

        EntityHitResult result = client.crosshairTarget instanceof EntityHitResult hit ? hit : null;

        if(!BLEntityComponents.VAMPIRE_COMPONENT.get(client.player).isVampire()) {
            targetEntity = null;
            suckBloodTimer = 0;
            return;
        }

        if(result == null) {
            targetEntity = null;
            suckBloodTimer = 0;
            return;
        }

        if(targetEntity != null && targetEntity != result.getEntity()) {
            suckBloodTimer = 0;
        }

        if((((LivingEntity)result.getEntity()).isDead() || result.getEntity().isRemoved() || BLEntityComponents.BLOOD_COMPONENT.get(result.getEntity()).getBlood() == 0) || !result.getEntity().getType().isIn(BLTags.Entities.HAS_BLOOD)) {
            targetEntity = null;
            suckBloodTimer = 0;
            return;
        }

        targetEntity = (LivingEntity) result.getEntity();
    }

    // its probably not good to have all the timer and validation logic on the client but i'll fix it later
    public void trySuckBlood() {
        if(targetEntity == null)
            return;

        suckBloodTimer++;
        if(suckBloodTimer < BLOOD_TIMER_LENGTH)
            return;

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(targetEntity.getId());
        ClientPlayNetworking.send(BLResources.KEYBIND_CHANNEL, buf);
        targetEntity = null;
        suckBloodTimer = 0;
    }
}
