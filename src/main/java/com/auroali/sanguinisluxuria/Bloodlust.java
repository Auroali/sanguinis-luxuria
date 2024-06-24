package com.auroali.sanguinisluxuria;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.blockentities.PedestalBlockEntity;
import com.auroali.sanguinisluxuria.common.commands.BloodlustCommand;
import com.auroali.sanguinisluxuria.common.commands.arguments.VampireAbilityArgument;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.BloodComponent;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.items.BloodStorageItem;
import com.auroali.sanguinisluxuria.common.registry.*;
import com.auroali.sanguinisluxuria.config.BLConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.transfer.v1.fluid.CauldronFluidContent;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bloodlust implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "sanguinisluxuria";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		BLRegistries.init();
		BLConfig.INSTANCE.load();

		BLEntityAttributes.register();
		BLCauldronBehaviours.register();
		BLFluids.register();
		BLBlocks.register();
		BLBlockEntities.register();
		BLRecipeSerializers.register();
		BLRecipeTypes.register();
		BLItems.register();
		BLItemGroups.register();
		BLSounds.register();
		BLStatusEffects.register();
		BLVampireAbilities.register();
		BLAdvancementCriterion.register();
		BLEnchantments.register();
		BLEntities.register();
		BLFeatures.register();

		ArgumentTypeRegistry.registerArgumentType(
				BLResources.VAMPIRE_ABILITY_ARGUMENT_ID,
				VampireAbilityArgument.class,
				ConstantArgumentSerializer.of(VampireAbilityArgument::argument)
		);

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(BloodlustCommand.register()));

		registerNetworkHandlers();

		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			if(VampireHelper.isVampire(newPlayer)) {
				VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(newPlayer);
				vampire.setDowned(false);
			}
		});

		ServerLivingEntityEvents.AFTER_DEATH.register(Bloodlust::dropBlood);

		EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, pos, vanilla) -> {
			if(VampireHelper.isVampire(player)) {
				return vanilla ? ActionResult.FAIL : ActionResult.SUCCESS;
			}
			return ActionResult.PASS;
		});

		ServerEntityWorldChangeEvents.AFTER_ENTITY_CHANGE_WORLD.register(this::syncComponentsOnWorldChange);
		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(this::syncComponentsOnWorldChange);

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 5, BLTradeOffers::registerClericTrades);

		ItemStorage.SIDED.registerForBlockEntities((blockEntity, context) -> {
			if(blockEntity instanceof PedestalBlockEntity e)
				return InventoryStorage.of(e.getInventory(), null);
			return null;
		}, BLBlockEntities.PEDESTAL);

		CauldronFluidContent.registerCauldron(BLBlocks.BLOOD_CAULDRON, BLFluids.BLOOD_STILL, FluidConstants.BOTTLE, LeveledCauldronBlock.LEVEL);

		FluidStorage.combinedItemApiProvider(BLItems.BLOOD_BOTTLE).register(ctx -> new BloodStorageItem.FluidStorage(ctx, BLItems.BLOOD_BOTTLE));
		FluidStorage.combinedItemApiProvider(Items.GLASS_BOTTLE).register(ctx -> new BloodStorageItem.FluidStorage(ctx, BLItems.BLOOD_BOTTLE));
		FluidStorage.combinedItemApiProvider(BLItems.BLOOD_BAG).register(ctx -> new BloodStorageItem.FluidStorage(ctx, BLItems.BLOOD_BAG));

		FluidVariantAttributes.register(BLFluids.BLOOD_STILL, BLFluids.BLOOD_ATTRIBUTE_HANDLER);
		FluidVariantAttributes.register(BLFluids.BLOOD_FLOWING, BLFluids.BLOOD_ATTRIBUTE_HANDLER);
	}

	private static void dropBlood(LivingEntity entity, DamageSource source) {
		if(!VampireHelper.isVampire(entity)
				&& entity.getType().isIn(BLTags.Entities.HAS_BLOOD)
				&& (entity.getType().isIn(BLTags.Entities.CAN_DROP_BLOOD) || entity.hasStatusEffect(BLStatusEffects.BLEEDING))
		) {
			BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(entity);
			if(blood.getBlood() < blood.getMaxBlood())
				return;

			BlockState state = entity.getWorld().getBlockState(entity.getBlockPos());
			BlockState belowState = entity.getWorld().getBlockState(entity.getBlockPos().down());
			if(tryFillCauldron(entity.getWorld(), entity.getBlockPos(), state) || tryFillCauldron(entity.getWorld(), entity.getBlockPos().down(), belowState))
				return;

			BlockState newState = BLBlocks.BLOOD_SPLATTER.getDefaultState();
			if(!state.canReplace(new AutomaticItemPlacementContext(entity.getWorld(), entity.getBlockPos(), Direction.DOWN, ItemStack.EMPTY, Direction.UP)) || !newState.canPlaceAt(entity.getWorld(), entity.getBlockPos()))
				return;

			entity.getWorld().setBlockState(entity.getBlockPos(), newState);
		}
	}

	private static boolean tryFillCauldron(World world, BlockPos pos, BlockState state) {
		if(state.isOf(BLBlocks.BLOOD_CAULDRON) && state.get(LeveledCauldronBlock.LEVEL) < LeveledCauldronBlock.MAX_LEVEL) {
			world.setBlockState(pos, state.with(LeveledCauldronBlock.LEVEL, state.get(LeveledCauldronBlock.LEVEL) + 1));
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
			return true;
		}
		if(state.isOf(Blocks.CAULDRON)) {
			world.setBlockState(pos, BLBlocks.BLOOD_CAULDRON.getDefaultState());
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
			return true;
		}
		return false;
	}

	private void syncComponentsOnWorldChange(ServerPlayerEntity entity, ServerWorld serverWorld, ServerWorld serverWorld1) {
		syncComponentsOnWorldChange(entity, entity, serverWorld, serverWorld1);
	}

	private void syncComponentsOnWorldChange(Entity entity, Entity newEntity, ServerWorld from, ServerWorld to) {
		if(entity.getType().isIn(BLTags.Entities.HAS_BLOOD) && BLEntityComponents.BLOOD_COMPONENT.isProvidedBy(newEntity))
			BLEntityComponents.BLOOD_COMPONENT.sync(newEntity);
		if(BLEntityComponents.VAMPIRE_COMPONENT.isProvidedBy(newEntity)) {
			VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(newEntity);
			vampire.getAbilties().setShouldSync(true);
			BLEntityComponents.BLOOD_COMPONENT.sync(newEntity);
		}
	}

	public static void registerNetworkHandlers() {
		ServerPlayNetworking.registerGlobalReceiver(BLResources.KEYBIND_CHANNEL, (server, player, handler, buf, responseSender) -> {
			boolean draining = buf.readBoolean();
			server.execute(() -> {
				VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(player);
				if(!vampire.isVampire())
					return;

				if(draining)
					vampire.tryStartSuckingBlood();
				else
					vampire.stopSuckingBlood();
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(BLResources.SKILL_TREE_CHANNEL, (server, player, handler, buf, responseSender) -> {
			VampireAbility ability = buf.readRegistryValue(BLRegistries.VAMPIRE_ABILITIES);
			boolean isAbilityBind = buf.readBoolean();
			int abilitySlot = isAbilityBind ? buf.readInt() : 0;
			server.execute(() -> {
				if(ability == null) {
					LOGGER.warn("Failed to read ability from packet!");
					return;
				}
				VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(player);
				if(!isAbilityBind) {
					if (vampire.getAbilties().hasAbility(ability)
							|| !vampire.getAbilties().hasAbility(ability.getParent())
							|| vampire.getSkillPoints() < ability.getRequiredSkillPoints()
							|| ability.isHidden(player)
					)
						return;

					if(VampireHelper.hasIncompatibleAbility(vampire.getAbilties(), ability))
						return;

					vampire.unlockAbility(ability);
				} else {
					if(!vampire.getAbilties().hasAbility(ability))
						return;

					if(abilitySlot == -1) {
						int current = vampire.getAbilties().getAbilityBinding(ability);
						if(current != -1)
							vampire.getAbilties().setBoundAbility(null, current);

						BLEntityComponents.VAMPIRE_COMPONENT.sync(player);
						return;
					}
					vampire.getAbilties().setBoundAbility(ability, abilitySlot);
					BLEntityComponents.VAMPIRE_COMPONENT.sync(player);
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(BLResources.ABILITY_KEY_CHANNEL, (server, player, handler, buf, responseSender) -> {
			int slot = buf.readInt();
			server.execute(() -> {
				VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(player);
				if(!vampire.isVampire())
					return;
				VampireAbility ability = vampire.getAbilties().getBoundAbility(slot);
				if(ability != null)
					ability.activate(player, vampire);
			});
		});
	}
}