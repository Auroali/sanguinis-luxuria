package com.auroali.sanguinisluxuria;

import com.auroali.sanguinisluxuria.client.BLHud;
import com.auroali.sanguinisluxuria.client.particles.AltarBeatParticle;
import com.auroali.sanguinisluxuria.client.particles.DrippingBloodParticle;
import com.auroali.sanguinisluxuria.client.render.blocks.ItemDisplayingBlockEntityRenderer;
import com.auroali.sanguinisluxuria.client.render.entities.VampireMerchantRenderer;
import com.auroali.sanguinisluxuria.client.render.entities.VampireVillagerRenderer;
import com.auroali.sanguinisluxuria.common.events.BloodStorageFillEvents;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.network.BLClientNetwork;
import com.auroali.sanguinisluxuria.common.network.packets.ActivateAbilityC2S;
import com.auroali.sanguinisluxuria.common.network.packets.DrainBloodC2S;
import com.auroali.sanguinisluxuria.common.network.packets.FillBloodItemC2S;
import com.auroali.sanguinisluxuria.common.registry.*;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

public class BloodlustClient implements ClientModInitializer {
    public static final KeyBinding SUCK_BLOOD = new KeyBinding(
      "key.sanguinisluxuria.drain_blood",
      InputUtil.Type.KEYSYM,
      GLFW.GLFW_KEY_R,
      "category.sanguinisluxuria.sanguinisluxuria"
    );
    public static final KeyBinding ACTIVATE_BITE = new KeyBinding(
      "key.sanguinisluxuria.activate_bite",
      InputUtil.Type.KEYSYM,
      GLFW.GLFW_KEY_X,
      "category.sanguinisluxuria.sanguinisluxuria"
    );
    public static final KeyBinding ACTIVATE_BLINK = new KeyBinding(
      "key.sanguinisluxuria.activate_blink",
      InputUtil.Type.KEYSYM,
      GLFW.GLFW_KEY_Z,
      "category.sanguinisluxuria.sanguinisluxuria"
    );
    public static final KeyBinding ACTIVATE_MIST = new KeyBinding(
      "key.sanguinisluxuria.activate_mist",
      InputUtil.Type.KEYSYM,
      GLFW.GLFW_KEY_C,
      "category.sanguinisluxuria.sanguinisluxuria"
    );

    public static boolean isAltarActive = false;

    public boolean drainingBlood;

    @Override
    public void onInitializeClient() {
        this.registerBindings();

        BLModelLayers.register();
        BLClientNetwork.init();

        TrinketRendererRegistry.registerRenderer(BLItems.MASK_1, BLItems.MASK_1);
        TrinketRendererRegistry.registerRenderer(BLItems.MASK_2, BLItems.MASK_2);
        TrinketRendererRegistry.registerRenderer(BLItems.MASK_3, BLItems.MASK_3);

        ModelLoadingPlugin.register(pluginContext -> {
            pluginContext.addModels(BLResources.MASK_ONE_ID.withPrefixedPath("item/").withSuffixedPath("_inventory"));
            pluginContext.addModels(BLResources.MASK_TWO_ID.withPrefixedPath("item/").withSuffixedPath("_inventory"));
            pluginContext.addModels(BLResources.MASK_THREE_ID.withPrefixedPath("item/").withSuffixedPath("_inventory"));
        });

        BloodStorageItem.registerModelPredicate(BLItems.BLOOD_BAG);
        BloodStorageItem.registerModelPredicate(BLItems.BLOOD_BOTTLE);

        BlockRenderLayerMap.INSTANCE.putBlock(BLBlocks.BLOOD_SPLATTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLBlocks.PEDESTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLBlocks.ALTAR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLBlocks.DECAYED_TWIGS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLBlocks.GRAFTED_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLBlocks.DECAYED_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLBlocks.DECAYED_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLBlocks.POTTED_GRAFTED_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLBlocks.SILVER_BARS, RenderLayer.getCutout());

        EntityRendererRegistry.register(BLEntities.VAMPIRE_VILLAGER, VampireVillagerRenderer::new);
        EntityRendererRegistry.register(BLEntities.VAMPIRE_MERCHANT, VampireMerchantRenderer::new);

        BlockEntityRendererFactories.register(BLBlockEntities.PEDESTAL, ctx -> new ItemDisplayingBlockEntityRenderer<>(ctx.getItemRenderer()));
        BlockEntityRendererFactories.register(BLBlockEntities.ALTAR, ctx -> new ItemDisplayingBlockEntityRenderer<>(ctx.getItemRenderer()));

        HudRenderCallback.EVENT.register(BLHud::render);

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), BLFluids.BLOOD);

        FluidRenderHandlerRegistry.INSTANCE.register(BLFluids.BLOOD, new SimpleFluidRenderHandler(
          BLResources.BLOOD_STILL_TEXTURE,
          BLResources.BLOOD_FLOWING_TEXTURE
        ));

        ParticleFactoryRegistry.getInstance().register(BLParticles.DRIPPING_BLOOD, sprite -> (type, world, x, y, z, velocityX, velocityY, velocityZ) -> {
            SpriteBillboardParticle particle = DrippingBloodParticle.createDrippingBlood(type, world, x, y, z, velocityX, velocityY, velocityZ);
            particle.setSprite(sprite);
            return particle;
        });
        ParticleFactoryRegistry.getInstance().register(BLParticles.FALLING_BLOOD, sprite -> (type, world, x, y, z, velocityX, velocityY, velocityZ) -> {
            SpriteBillboardParticle particle = DrippingBloodParticle.createFallingBlood(type, world, x, y, z, velocityX, velocityY, velocityZ);
            particle.setSprite(sprite);
            return particle;
        });
        ParticleFactoryRegistry.getInstance().register(BLParticles.LANDING_BLOOD, sprite -> (type, world, x, y, z, velocityX, velocityY, velocityZ) -> {
            SpriteBillboardParticle particle = DrippingBloodParticle.createLandingBlood(type, world, x, y, z, velocityX, velocityY, velocityZ);
            particle.setSprite(sprite);
            return particle;
        });
        ParticleFactoryRegistry.getInstance().register(BLParticles.ALTAR_BEAT, AltarBeatParticle.Factory::new);
    }

    public void registerBindings() {
        KeyBindingHelper.registerKeyBinding(SUCK_BLOOD);
        KeyBindingHelper.registerKeyBinding(ACTIVATE_BITE);
        KeyBindingHelper.registerKeyBinding(ACTIVATE_BLINK);
        KeyBindingHelper.registerKeyBinding(ACTIVATE_MIST);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ACTIVATE_BITE.wasPressed()) {
                ClientPlayNetworking.send(new ActivateAbilityC2S(BLVampireAbilities.BITE));
            }
            while (ACTIVATE_BLINK.wasPressed()) {
                ClientPlayNetworking.send(new ActivateAbilityC2S(BLVampireAbilities.TELEPORT));
            }
            while (ACTIVATE_MIST.wasPressed()) {
                ClientPlayNetworking.send(new ActivateAbilityC2S(BLVampireAbilities.MIST));
            }
            if (SUCK_BLOOD.isPressed()) {
                this.handeBloodDrainPress(client);
            } else if (this.drainingBlood) {
                this.cancelBloodDrain();
            }
        });
    }

    private void handeBloodDrainPress(MinecraftClient client) {
        // handle draining blood from entities
        if (isLookingAtValidTarget()) {
            if (!this.drainingBlood) {
                ClientPlayNetworking.send(new DrainBloodC2S(true));
                this.drainingBlood = true;
            }
            return;
        }

        // otherwise, handle filling blood storing items
        if (this.drainingBlood)
            this.cancelBloodDrain();
        // if the player is holding a fillable item, send the packet as long as the key is held down
        ItemStack toFill = VampireHelper.getItemInHand(
          client.player,
          Hand.MAIN_HAND,
          stack -> stack.getItem() instanceof BloodStorageItem
            || BloodStorageFillEvents.ALLOW_ITEM.invoker().allowItem(client.player, stack)
        );

        if (!toFill.isEmpty())
            ClientPlayNetworking.send(new FillBloodItemC2S(VampireHelper.getHandForStack(client.player, toFill)));
    }

    private void cancelBloodDrain() {
        this.drainingBlood = false;
        ClientPlayNetworking.send(new DrainBloodC2S(false));
    }

    public static boolean isLookingAtValidTarget() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!VampireHelper.isVampire(client.player))
            return false;

        HitResult result = client.crosshairTarget;
        LivingEntity target = result != null && result.getType() == HitResult.Type.ENTITY && ((EntityHitResult) result).getEntity() instanceof LivingEntity living ? living : null;

        return VampireHelper.hasBlood(target);
    }
}
