package com.auroali.bloodlust;

import com.auroali.bloodlust.common.abilities.VampireAbility;
import com.auroali.bloodlust.common.commands.BloodlustCommand;
import com.auroali.bloodlust.common.commands.arguments.VampireAbilityArgument;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.BloodComponent;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.auroali.bloodlust.common.items.BloodStorageItem;
import com.auroali.bloodlust.common.registry.*;
import com.auroali.bloodlust.config.BLConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;
import net.minecraft.village.VillagerProfession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bloodlust implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "bloodlust";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static final ItemGroup BLOODLUST_TAB = FabricItemGroupBuilder.create(BLResources.ITEM_GROUP_ID)
			.icon(() -> {
				ItemStack stack = new ItemStack(BLItems.BLOOD_BOTTLE);
				BloodStorageItem.setStoredBlood(stack, BLItems.BLOOD_BOTTLE.getMaxBlood());
				return stack;
			})
			.build();

	@Override
	public void onInitialize() {
		BLRegistry.init();
		BLConfig.INSTANCE.load();

		ArgumentTypeRegistry.registerArgumentType(
				BLResources.VAMPIRE_ABILITY_ARGUMENT_ID,
				VampireAbilityArgument.class,
				ConstantArgumentSerializer.of(VampireAbilityArgument::argument)
		);

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(BloodlustCommand.register()));

		registerNetworkHandlers();

		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack stack = player.getStackInHand(hand);
			if(VampireHelper.isVampire(player) && stack.isFood() && ! stack.isIn(BLTags.Items.VAMPIRE_EDIBLE))
				return TypedActionResult.fail(stack);

			return TypedActionResult.pass(stack);
		});

		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			if(!VampireHelper.isVampire(entity)
					&& entity.getType().isIn(BLTags.Entities.HAS_BLOOD)
					&& entity.getType().isIn(BLTags.Entities.CAN_DROP_BLOOD)
			) {
				BloodComponent blood = BLEntityComponents.BLOOD_COMPONENT.get(entity);
				if(blood.getBlood() < blood.getMaxBlood())
					return;

				BlockState state = entity.world.getBlockState(entity.getBlockPos());
				BlockState newState = BLBlocks.BLOOD_SPLATTER.getDefaultState();
				if(!state.isIn(BLTags.Blocks.BLOOD_SPLATTER_REPLACEABLE) || !newState.canPlaceAt(entity.world, entity.getBlockPos()))
					return;
				entity.world.setBlockState(entity.getBlockPos(), newState);
			}
		});

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 5, BLTradeOffers::registerClericTrades);

		BLBlocks.register();
		BLBlockEntities.register();
		BLRecipeSerializers.register();
		BLRecipeTypes.register();
		BLItems.register();
		BLSounds.register();
		BLStatusEffects.register();
		BLVampireAbilities.register();
		BLAdvancementCriterion.register();
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
			VampireAbility ability = buf.readRegistryValue(BLRegistry.VAMPIRE_ABILITIES);
			boolean isAbilityBind = buf.readBoolean();
			int abilitySlot = isAbilityBind ? buf.readInt() : 0;
			server.execute(() -> {
				VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(player);
				if(!isAbilityBind) {
					if (vampire.getAbilties().hasAbility(ability) || !vampire.getAbilties().hasAbility(ability.getParent()) || vampire.getSkillPoints() < ability.getRequiredSkillPoints())
						return;

					for(VampireAbility other : vampire.getAbilties()) {
						if(ability.incompatibleWith(other))
							return;
					}
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
				VampireAbility ability = vampire.getAbilties().getBoundAbility(slot);
				if(ability != null)
					ability.activate(player, vampire);
			});
		});
	}
}