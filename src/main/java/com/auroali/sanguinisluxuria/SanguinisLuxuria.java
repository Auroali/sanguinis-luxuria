package com.auroali.sanguinisluxuria;

import com.auroali.sanguinisluxuria.common.blockentities.AltarBlockEntity;
import com.auroali.sanguinisluxuria.common.blockentities.PedestalBlockEntity;
import com.auroali.sanguinisluxuria.common.commands.BloodlustCommand;
import com.auroali.sanguinisluxuria.common.commands.arguments.ConversionArgument;
import com.auroali.sanguinisluxuria.common.commands.arguments.VampireAbilityArgument;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.events.BloodStorageFillEvents;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.items.EntityTrackingItem;
import com.auroali.sanguinisluxuria.common.items.storage.BloodItemFluidStorage;
import com.auroali.sanguinisluxuria.common.network.SLNetwork;
import com.auroali.sanguinisluxuria.common.registry.*;
import com.auroali.sanguinisluxuria.config.SLConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.transfer.v1.fluid.CauldronFluidContent;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.EmptyItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SanguinisLuxuria implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MODID = "sanguinisluxuria";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitialize() {
        SLRegistries.init();
        SLConfig.INSTANCE.load();

        SLEntityAttributes.register();
        SLCauldronBehaviours.register();
        SLFluids.register();
        SLBlocks.register();
        SLBlockEntities.register();
        SLRecipeSerializers.register();
        SLRecipeTypes.register();
        SLItems.register();
        SLItemGroups.register();
        SLSounds.register();
        SLStatusEffects.register();
        SLVampireAbilities.register();
        SLAdvancementCriterion.register();
        SLEnchantments.register();
        SLEntities.register();
        SLWorldgen.register();
        SLParticles.register();
        SLRitualTypes.register();
        SLBloodDrainEffects.init();
        SLConversions.register();
        SLLootFunctions.register();
        SLNetwork.init();

        ArgumentTypeRegistry.registerArgumentType(
          SLResources.VAMPIRE_ABILITY_ARGUMENT_ID,
          VampireAbilityArgument.class,
          ConstantArgumentSerializer.of(VampireAbilityArgument::argument)
        );

        ArgumentTypeRegistry.registerArgumentType(
          SLResources.CONVERSION_ARGUMENT_ID,
          ConversionArgument.class,
          ConstantArgumentSerializer.of(ConversionArgument::conversion)
        );

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(BloodlustCommand.register()));

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            if (VampireHelper.isVampire(newPlayer)) {
                VampireComponent vampire = VampireComponent.KEY.get(newPlayer);
                vampire.setDowned(false);
            }
        });


        // run the sleep event after other mods, to allow things like
        // spectrum's somnolence effect to allow sleep during night
        EntitySleepEvents.ALLOW_SLEEP_TIME.addPhaseOrdering(Event.DEFAULT_PHASE, SLResources.AFTER_EVENT_PHASE);
        EntitySleepEvents.ALLOW_SLEEP_TIME.register(SLResources.AFTER_EVENT_PHASE, (player, pos, vanilla) -> {
            if (VampireHelper.isVampire(player)) {
                // invert the vanilla check (can only sleep at night)
                // also makes sure to allow sleeping while thundering
                return !player.getWorld().isThundering() && vanilla ? ActionResult.FAIL : ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });

        BloodStorageFillEvents.ALLOW_ITEM.register((entity, stack) ->
          stack.isIn(SLTags.Items.BLOOD_STORING_BOTTLES)
            || (stack.getItem() instanceof BloodStorageItem item && item.canFill())
        );
        BloodStorageFillEvents.TRANSFORM_STACK.register((entity, stack) ->
          stack.isIn(SLTags.Items.BLOOD_STORING_BOTTLES)
            ? new ItemStack(SLItems.BLOOD_BOTTLE)
            : ItemStack.EMPTY
        );

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 3, SLTradeOffers::registerClericTrades);

        ItemStorage.SIDED.registerForBlockEntities((blockEntity, context) -> {
            if (blockEntity instanceof PedestalBlockEntity e)
                return InventoryStorage.of(e.getInventory(), null);
            if (blockEntity instanceof AltarBlockEntity e)
                return InventoryStorage.of(e, null);
            return null;
        }, SLBlockEntities.PEDESTAL);

        CauldronFluidContent.registerCauldron(SLBlocks.BLOOD_CAULDRON, SLFluids.BLOOD, FluidConstants.BOTTLE, LeveledCauldronBlock.LEVEL);

        FluidStorage.combinedItemApiProvider(Items.GLASS_BOTTLE).register(
          ctx ->
            new EmptyItemFluidStorage(
              ctx,
              var -> ItemVariant.of(BloodStorageItem.createStack(SLItems.BLOOD_BOTTLE)),
              SLFluids.BLOOD,
              FluidConstants.BOTTLE
            )
        );
        FluidStorage.combinedItemApiProvider(SLItems.BLOOD_BOTTLE).register(BloodItemFluidStorage::new);
        FluidStorage.combinedItemApiProvider(SLItems.BLOOD_BAG).register(BloodItemFluidStorage::new);

        FluidVariantAttributes.register(SLFluids.BLOOD, SLFluids.BLOOD_ATTRIBUTE_HANDLER);

        UseBlockCallback.EVENT.register(EntityTrackingItem::setAltarTarget);
    }
}