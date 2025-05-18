package com.auroali.sanguinisluxuria.common.commands;

import com.auroali.sanguinisluxuria.common.abilities.VampireAbility;
import com.auroali.sanguinisluxuria.common.commands.arguments.VampireAbilityArgument;
import com.auroali.sanguinisluxuria.common.components.BLEntityComponents;
import com.auroali.sanguinisluxuria.common.components.VampireComponent;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;
import java.util.Collections;

public class AbilityCommand {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("ability")
          .then(CommandManager.argument("ability", VampireAbilityArgument.argument())
            .executes(ctx -> grantAbility(ctx, Collections.singleton(ctx.getSource().getPlayer())))
            .then(CommandManager.argument("targets", EntityArgumentType.players())
              .executes(ctx -> grantAbility(ctx, EntityArgumentType.getPlayers(ctx, "targets")))
            )
          ).then(CommandManager.literal("reset")
            .executes(ctx -> resetAbilities(ctx, Collections.singleton(ctx.getSource().getPlayer())))
            .then(CommandManager.argument("targets", EntityArgumentType.players())
              .executes(ctx -> resetAbilities(ctx, EntityArgumentType.getPlayers(ctx, "targets")))
            )
          );
    }

    private static int resetAbilities(CommandContext<ServerCommandSource> ctx, Collection<ServerPlayerEntity> targets) {
        for (ServerPlayerEntity player : targets) {
            VampireComponent component = BLEntityComponents.VAMPIRE_COMPONENT.get(player);
            for (VampireAbility a : component.getAbilties().abilities()) {
                a.onAbilityRemoved(player, component);
                component.getAbilties().removeAbility(a);
            }
            BLEntityComponents.VAMPIRE_COMPONENT.sync(player);
        }
        return 0;
    }

    private static int grantAbility(CommandContext<ServerCommandSource> ctx, Collection<ServerPlayerEntity> targets) {
        for (ServerPlayerEntity player : targets) {
            VampireAbility ability = VampireAbilityArgument.getAbility(ctx, "ability");
            VampireComponent component = BLEntityComponents.VAMPIRE_COMPONENT.get(player);
            if (ability.testConditions(player, component, component.getAbilties())) {
                component.unlockAbility(ability);
                BLEntityComponents.VAMPIRE_COMPONENT.sync(player);
            }
        }
        return 0;
    }
}
