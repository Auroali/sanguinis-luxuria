package com.auroali.bloodlust;

import com.auroali.bloodlust.common.commands.BloodlustCommand;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.auroali.bloodlust.common.registry.BLTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bloodlust implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "bloodlust";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(BloodlustCommand.register());
		});

		ServerPlayNetworking.registerGlobalReceiver(BLResources.KEYBIND_CHANNEL, (server, player, handler, buf, responseSender) -> {
			int entityId = buf.readInt();
			server.execute(() -> {
				Entity entity = player.world.getEntityById(entityId);
				if(entity == null)
					return;
				if(!entity.getType().isIn(BLTags.Entity.HAS_BLOOD))
					return;

				VampireComponent vampire = BLEntityComponents.VAMPIRE_COMPONENT.get(player);
				if(vampire.isVampire())
					vampire.drainBloodFrom((LivingEntity) entity);
			});
		});
	}
}