package com.auroali.sanguinisluxuria;

import com.auroali.sanguinisluxuria.client.SLHud;
import com.auroali.sanguinisluxuria.client.particles.AltarBeatParticle;
import com.auroali.sanguinisluxuria.client.particles.AltarParticle;
import com.auroali.sanguinisluxuria.client.particles.DrippingBloodParticle;
import com.auroali.sanguinisluxuria.client.render.blocks.ItemDisplayingBlockEntityRenderer;
import com.auroali.sanguinisluxuria.client.render.entities.VampireIllagerRenderer;
import com.auroali.sanguinisluxuria.client.render.entities.VampireMerchantRenderer;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.abilities.VampireAbilityContainer;
import com.auroali.sanguinisluxuria.common.components.BloodDrainComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.events.BloodEvents;
import com.auroali.sanguinisluxuria.common.events.BloodStorageFillEvents;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.network.SLClientNetwork;
import com.auroali.sanguinisluxuria.common.network.packets.ActivateAbilityC2S;
import com.auroali.sanguinisluxuria.common.network.packets.DrainBloodC2S;
import com.auroali.sanguinisluxuria.common.network.packets.FillBloodItemC2S;
import com.auroali.sanguinisluxuria.common.registry.*;
import com.auroali.sanguinisluxuria.util.ItemUtil;
import com.auroali.sanguinisluxuria.util.VampireHelper;
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

public class SanguinisLuxuriaClient implements ClientModInitializer {
    public static final KeyBinding SUCK_BLOOD = new KeyBinding(
      "key.sanguinisluxuria.drain_blood",
      InputUtil.Type.KEYSYM,
      InputUtil.GLFW_KEY_R,
      "category.sanguinisluxuria.sanguinisluxuria"
    );
    public static final KeyBinding ACTIVATE_BITE = new KeyBinding(
      "key.sanguinisluxuria.activate_bite",
      InputUtil.Type.KEYSYM,
      InputUtil.GLFW_KEY_G,
      "category.sanguinisluxuria.sanguinisluxuria"
    );
    public static final KeyBinding ACTIVATE_BLINK = new KeyBinding(
      "key.sanguinisluxuria.activate_blink",
      InputUtil.Type.KEYSYM,
      InputUtil.GLFW_KEY_V,
      "category.sanguinisluxuria.sanguinisluxuria"
    );
    public static final KeyBinding ACTIVATE_MIST = new KeyBinding(
      "key.sanguinisluxuria.activate_mist",
      InputUtil.Type.KEYSYM,
      InputUtil.GLFW_KEY_B,
      "category.sanguinisluxuria.sanguinisluxuria"
    );

    public static boolean isAltarActive = false;

    @Override
    public void onInitializeClient() {
        this.registerBindings();

        SLModelLayers.register();
        SLClientNetwork.init();

        TrinketRendererRegistry.registerRenderer(SLItems.MASK_1, SLItems.MASK_1);
        TrinketRendererRegistry.registerRenderer(SLItems.MASK_2, SLItems.MASK_2);
        TrinketRendererRegistry.registerRenderer(SLItems.MASK_3, SLItems.MASK_3);

        ModelLoadingPlugin.register(pluginContext -> {
            pluginContext.addModels(SLResources.MASK_ONE_ID.withPrefixedPath("item/").withSuffixedPath("_inventory"));
            pluginContext.addModels(SLResources.MASK_TWO_ID.withPrefixedPath("item/").withSuffixedPath("_inventory"));
            pluginContext.addModels(SLResources.MASK_THREE_ID.withPrefixedPath("item/").withSuffixedPath("_inventory"));
        });

        BloodStorageItem.registerModelPredicate(SLItems.BLOOD_BAG);
        BloodStorageItem.registerModelPredicate(SLItems.BLOOD_BOTTLE);

        BlockRenderLayerMap.INSTANCE.putBlock(SLBlocks.BLOOD_SPLATTER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SLBlocks.PEDESTAL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SLBlocks.ALTAR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SLBlocks.DECAYED_TWIGS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SLBlocks.GRAFTED_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SLBlocks.DECAYED_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SLBlocks.DECAYED_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SLBlocks.POTTED_GRAFTED_SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SLBlocks.SILVER_BARS, RenderLayer.getCutout());

        EntityRendererRegistry.register(SLEntities.VAMPIRE_ILLAGER, VampireIllagerRenderer::new);
        EntityRendererRegistry.register(SLEntities.VAMPIRE_MERCHANT, VampireMerchantRenderer::new);

        BlockEntityRendererFactories.register(SLBlockEntities.PEDESTAL, ctx -> new ItemDisplayingBlockEntityRenderer<>(ctx.getItemRenderer()));
        BlockEntityRendererFactories.register(SLBlockEntities.ALTAR, ctx -> new ItemDisplayingBlockEntityRenderer<>(ctx.getItemRenderer()));

        HudRenderCallback.EVENT.register(SLHud::render);

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), SLFluids.BLOOD);

        FluidRenderHandlerRegistry.INSTANCE.register(SLFluids.BLOOD, new SimpleFluidRenderHandler(
          SLResources.BLOOD_STILL_TEXTURE,
          SLResources.BLOOD_FLOWING_TEXTURE
        ));

        ParticleFactoryRegistry.getInstance().register(SLParticles.DRIPPING_BLOOD, sprite -> (type, world, x, y, z, velocityX, velocityY, velocityZ) -> {
            SpriteBillboardParticle particle = DrippingBloodParticle.createDrippingBlood(type, world, x, y, z, velocityX, velocityY, velocityZ);
            particle.setSprite(sprite);
            return particle;
        });
        ParticleFactoryRegistry.getInstance().register(SLParticles.FALLING_BLOOD, sprite -> (type, world, x, y, z, velocityX, velocityY, velocityZ) -> {
            SpriteBillboardParticle particle = DrippingBloodParticle.createFallingBlood(type, world, x, y, z, velocityX, velocityY, velocityZ);
            particle.setSprite(sprite);
            return particle;
        });
        ParticleFactoryRegistry.getInstance().register(SLParticles.LANDING_BLOOD, sprite -> (type, world, x, y, z, velocityX, velocityY, velocityZ) -> {
            SpriteBillboardParticle particle = DrippingBloodParticle.createLandingBlood(type, world, x, y, z, velocityX, velocityY, velocityZ);
            particle.setSprite(sprite);
            return particle;
        });
        ParticleFactoryRegistry.getInstance().register(SLParticles.ALTAR_BEAT, AltarBeatParticle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(SLParticles.ALTAR, AltarParticle.Factory::new);
    }

    public void registerBindings() {
        KeyBindingHelper.registerKeyBinding(SUCK_BLOOD);
        KeyBindingHelper.registerKeyBinding(ACTIVATE_BITE);
        KeyBindingHelper.registerKeyBinding(ACTIVATE_BLINK);
        KeyBindingHelper.registerKeyBinding(ACTIVATE_MIST);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (VampireHelper.isVampire(client.player)) {
                VampireAbilityContainer container = VampireComponent.KEY.get(client.player).getAbilityContainer();
                this.handleAbilityKey(container, SLVampireAbilities.BITE, ACTIVATE_BITE);
                this.handleAbilityKey(container, SLVampireAbilities.TELEPORT, ACTIVATE_BLINK);
                this.handleAbilityKey(container, SLVampireAbilities.MIST, ACTIVATE_MIST);
            }
            if (SUCK_BLOOD.isPressed()) {
                this.handeBloodDrainPress(client);
            } else if (client.player != null && BloodDrainComponent.KEY.get(client.player).isDraining()) {
                ClientPlayNetworking.send(new DrainBloodC2S(false));
            }
        });
    }

    private void handeBloodDrainPress(MinecraftClient client) {
        // handle draining blood from entities
        if (isLookingAtValidTarget()) {
            if (client.player != null && !BloodDrainComponent.KEY.get(client.player).isDraining()) {
                ClientPlayNetworking.send(new DrainBloodC2S(true));
            }
            return;
        }

        // otherwise, handle filling blood storing items
        if (client.player != null && BloodDrainComponent.KEY.get(client.player).isDraining())
            ClientPlayNetworking.send(new DrainBloodC2S(false));

        if (VampireHelper.isVampire(client.player)) {
            // if the player is holding a fillable item, send the packet as long as the key is held down
            ItemStack toFill = ItemUtil.getItemInHand(
              client.player,
              Hand.MAIN_HAND,
              stack -> stack.getItem() instanceof BloodStorageItem
                || BloodStorageFillEvents.ALLOW_ITEM.invoker().allowItem(client.player, stack)
            );

            if (!toFill.isEmpty())
                ClientPlayNetworking.send(new FillBloodItemC2S(ItemUtil.getHandForStack(client.player, toFill)));
        }
    }

    private void handleAbilityKey(VampireAbilityContainer container, VampireAbility ability, KeyBinding keyBinding) {
        if (container.has(ability)) {
            while (keyBinding.wasPressed()) {
                ClientPlayNetworking.send(new ActivateAbilityC2S(ability));
            }
        }
    }

    public static boolean isLookingAtValidTarget() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!VampireHelper.isVampire(client.player))
            return false;

        HitResult result = client.crosshairTarget;
        LivingEntity target = result != null && result.getType() == HitResult.Type.ENTITY && ((EntityHitResult) result).getEntity() instanceof LivingEntity living ? living : null;

        return VampireHelper.hasBlood(target) && BloodEvents.ALLOW_BLOOD_DRAIN.invoker().allowBloodDrain(client.player, target);
    }
}
