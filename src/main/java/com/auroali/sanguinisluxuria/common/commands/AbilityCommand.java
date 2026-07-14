package com.auroali.sanguinisluxuria.common.commands;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.commands.arguments.VampireAbilityArgument;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.auroali.sanguinisluxuria.common.registry.SLRegistries;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class AbilityCommand {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("ability")
          .then(CommandManager.argument("ability", VampireAbilityArgument.argument())
            .executes(ctx -> grantAbility(
              VampireAbilityArgument.getAbility(ctx, "ability"),
              ctx.getSource().getPlayer()
            ))
            .then(CommandManager.argument("target", EntityArgumentType.player())
              .executes(ctx -> grantAbility(
                VampireAbilityArgument.getAbility(ctx, "ability"),
                EntityArgumentType.getPlayer(ctx, "targets")
              ))
            )
          ).then(CommandManager.literal("reset")
            .executes(ctx -> resetAbilities(ctx.getSource().getPlayer()))
            .then(CommandManager.argument("target", EntityArgumentType.player())
              .executes(ctx -> resetAbilities(EntityArgumentType.getPlayer(ctx, "targets")))
            )
          );
    }

    private static int resetAbilities(ServerPlayerEntity player) {
        VampireComponent component = VampireComponent.KEY.get(player);
        for (VampireAbility a : component.getAbilityContainer().abilities()) {
            a.onAbilityRemoved(player, component);
            component.getAbilityContainer().removeAbility(a);
        }
        VampireComponent.KEY.sync(player);
        return 0;
    }

    private static int grantAbility(VampireAbility ability, ServerPlayerEntity target) {
        VampireComponent component = VampireComponent.KEY.get(target);
        if (!ability.testConditions(target, component, component.getAbilityContainer())) {
            throw new CommandException(
              Text.translatable(
                "commands.sanguinisluxuria.ability.failed_conditions",
                SLRegistries.VAMPIRE_ABILITIES.getId(ability),
                target.getName()
              )
            );
        }
        component.getAbilityContainer().addAbility(ability);
        VampireComponent.KEY.sync(target);
        return 0;
    }
}
