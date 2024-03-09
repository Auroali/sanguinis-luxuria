package com.auroali.sanguinisluxuria.common.commands;

import com.auroali.sanguinisluxuria.Bloodlust;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class BloodlustCommand {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal(Bloodlust.MODID.toLowerCase())
                .then(VampireCommand.register())
                .then(AbilityCommand.register());
    }
}
