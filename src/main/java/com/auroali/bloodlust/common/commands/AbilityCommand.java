package com.auroali.bloodlust.common.commands;

import com.auroali.bloodlust.common.abilities.VampireAbility;
import com.auroali.bloodlust.common.commands.arguments.VampireAbilityArgument;
import com.auroali.bloodlust.common.components.BLEntityComponents;
import com.auroali.bloodlust.common.components.VampireComponent;
import com.auroali.bloodlust.common.registry.BLRegistry;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.argument.RegistryKeyArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.registry.RegistryKey;

public class AbilityCommand {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("ability")
                .then(CommandManager.argument("ability", VampireAbilityArgument.argument())
                        .executes(ctx -> {
                            if(!ctx.getSource().isExecutedByPlayer())
                                return 1;
                            VampireAbility ability = VampireAbilityArgument.getAbility(ctx, "ability");
                            VampireComponent component = BLEntityComponents.VAMPIRE_COMPONENT.get(ctx.getSource().getPlayer());
                            component.getAbilties().addAbility(ability);
                            BLEntityComponents.VAMPIRE_COMPONENT.sync(ctx.getSource().getPlayer());
                            return 0;
                        })
                ).then(CommandManager.literal("reset").executes(ctx -> {
                    if(!ctx.getSource().isExecutedByPlayer())
                        return 1;
                    VampireComponent component = BLEntityComponents.VAMPIRE_COMPONENT.get(ctx.getSource().getPlayer());
                    for(VampireAbility a : component.getAbilties())
                        component.getAbilties().removeAbility(a);
                    BLEntityComponents.VAMPIRE_COMPONENT.sync(ctx.getSource().getPlayer());
                    return 0;
                }));
    }
}
