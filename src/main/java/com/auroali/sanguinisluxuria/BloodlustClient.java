package com.auroali.sanguinisluxuria;

import com.auroali.sanguinisluxuria.client.BLHud;
import com.auroali.sanguinisluxuria.client.render.PedestalBlockRenderer;
import com.auroali.sanguinisluxuria.client.render.VampireVillagerRenderer;
import com.auroali.sanguinisluxuria.client.screen.VampireAbilitiesScreen;
import com.auroali.sanguinisluxuria.common.abilities.SyncableVampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.registry.*;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class BloodlustClient implements ClientModInitializer {
    public static KeyBinding SUCK_BLOOD = new KeyBinding(
            "key.sanguinisluxuria.bite",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "category.sanguinisluxuria.sanguinisluxuria"
    );
    public static KeyBinding OPEN_ABILITIES = new KeyBinding(
            "key.sanguinisluxuria.open_abilities",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Y,
            "category.sanguinisluxuria.sanguinisluxuria"
    );
    public static KeyBinding ABILITY_1 = new KeyBinding(
            "key.sanguinisluxuria.ability_1",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "category.sanguinisluxuria.sanguinisluxuria"
    );
    public static KeyBinding ABILITY_2 = new KeyBinding(
            "key.sanguinisluxuria.ability_2",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "category.sanguinisluxuria.sanguinisluxuria"
    );

    public boolean drainingBlood;

    @Override
    public void onInitializeClient() {
        registerBindings();

        BLModelLayers.register();

        TrinketRendererRegistry.registerRenderer(BLItems.MASK_1, BLItems.MASK_1);
        TrinketRendererRegistry.registerRenderer(BLItems.MASK_2, BLItems.MASK_2);
        TrinketRendererRegistry.registerRenderer(BLItems.MASK_3, BLItems.MASK_3);

        ModelPredicateProviderRegistry.register(BLItems.BLOOD_BAG, BLResources.BLOOD_STORAGE_ITEM_MODEL_PREDICATE, BloodStorageItem::modelPredicate);
        ModelPredicateProviderRegistry.register(BLItems.BLOOD_BOTTLE, BLResources.BLOOD_STORAGE_ITEM_MODEL_PREDICATE, BloodStorageItem::modelPredicate);

        BlockRenderLayerMap.INSTANCE.putBlock(BLBlocks.BLOOD_SPLATTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLBlocks.PEDESTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLBlocks.SKILL_UPGRADER, RenderLayer.getCutout());

        EntityRendererRegistry.register(BLEntities.VAMPIRE_VILLAGER, VampireVillagerRenderer::new);

        BlockEntityRendererFactories.register(BLBlockEntities.PEDESTAL, ctx -> new PedestalBlockRenderer(ctx.getItemRenderer()));

        HudRenderCallback.EVENT.register(BLHud::render);

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), BLFluids.BLOOD_STILL, BLFluids.BLOOD_FLOWING);

        FluidRenderHandlerRegistry.INSTANCE.register(BLFluids.BLOOD_STILL, BLFluids.BLOOD_FLOWING, new SimpleFluidRenderHandler(
                BLResources.BLOOD_STILL_TEXTURE,
                BLResources.BLOOD_FLOWING_TEXTURE
        ));

        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
            registry.register(BLResources.BLOOD_FLOWING_TEXTURE);
            registry.register(BLResources.BLOOD_STILL_TEXTURE);
        });

        ClientPlayNetworking.registerGlobalReceiver(BLResources.ABILITY_SYNC_CHANNEL, (client, handler, buf, responseSender) -> {
            int id = buf.readVarInt();
            VampireAbility ability = buf.readRegistryValue(BLRegistry.VAMPIRE_ABILITIES);
            if(client.world != null && client.world.getEntityById(id) instanceof LivingEntity entity && ability instanceof SyncableVampireAbility<?> s)
                s.handlePacket(entity, buf, client::execute);
        });
    }

    public void registerBindings() {
        SUCK_BLOOD = KeyBindingHelper.registerKeyBinding(SUCK_BLOOD);
        OPEN_ABILITIES = KeyBindingHelper.registerKeyBinding(OPEN_ABILITIES);
        ABILITY_1 = KeyBindingHelper.registerKeyBinding(ABILITY_1);
        ABILITY_2 = KeyBindingHelper.registerKeyBinding(ABILITY_2);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(OPEN_ABILITIES.wasPressed() && VampireHelper.isVampire(client.player)) {
                client.setScreen(new VampireAbilitiesScreen());
            }
            while(ABILITY_1.wasPressed()) {
                sendAbilityKeyPress(0);
            }
            while(ABILITY_2.wasPressed()) {
                sendAbilityKeyPress(1);
            }
            if (SUCK_BLOOD.isPressed()) {
                if (isLookingAtValidTarget() || BloodStorageItem.isHoldingBloodFillableItem(client.player)) {
                    sendBloodDrainPacket(true);
                    drainingBlood = true;
                }
            } else if (drainingBlood) {
                drainingBlood = false;
                sendBloodDrainPacket(false);
            }
        });

    }

    public static void sendAbilityKeyPress(int i) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(i);
        ClientPlayNetworking.send(BLResources.ABILITY_KEY_CHANNEL, buf);
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
