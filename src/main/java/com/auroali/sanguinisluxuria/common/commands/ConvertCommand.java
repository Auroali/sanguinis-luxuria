package com.auroali.sanguinisluxuria.common.commands;

import com.auroali.sanguinisluxuria.common.commands.arguments.ConversionArgument;
import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.registry.SLConversions;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

public class ConvertCommand {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("convert")
          .then(CommandManager.argument("targets", EntityArgumentType.entities())
            .then(CommandManager.argument("conversion", ConversionArgument.conversion())
              .executes(ctx -> convert(
                EntityArgumentType.getEntities(ctx, "targets"),
                ConversionArgument.getConversion(ctx, "conversion")
              ))
            )
          );
    }

    public static int convert(Collection<? extends Entity> entities, ConversionContext.Conversion conversion) {
        for (Entity entity : entities) {
            if (!SLConversions.convertEntity(ConversionContext.from(entity, conversion)))
                throw new CommandException(Text.translatable("commands.sanguinisluxuria.convert.invalid_conversion", entity.getName()));
        }
        return 0;
    }
}
