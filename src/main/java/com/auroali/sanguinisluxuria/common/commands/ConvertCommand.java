package com.auroali.sanguinisluxuria.common.commands;

import com.auroali.sanguinisluxuria.common.commands.arguments.ConversionArgument;
import com.auroali.sanguinisluxuria.common.conversions.ConversionContext;
import com.auroali.sanguinisluxuria.common.data.EntityConversionLoader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ConvertCommand {
    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        return CommandManager.literal("convert")
          .then(CommandManager.argument("target", EntityArgumentType.entity())
            .then(CommandManager.argument("conversion", ConversionArgument.conversion())
              .executes(ctx -> convert(
                EntityArgumentType.getEntity(ctx, "target"),
                ConversionArgument.getConversion(ctx, "conversion")
              ))
            )
          );
    }

    public static int convert(Entity entity, ConversionContext.Conversion conversion) {
        if (!EntityConversionLoader.convertEntity(ConversionContext.builder(entity).withConversion(conversion).build()))
            throw new CommandException(Text.translatable("commands.sanguinisluxuria.convert.failed", entity.getName()));
        return 0;
    }
}
