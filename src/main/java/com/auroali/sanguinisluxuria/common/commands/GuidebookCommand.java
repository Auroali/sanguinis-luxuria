package com.auroali.sanguinisluxuria.common.commands;

import com.auroali.sanguinisluxuria.SanguinisLuxuria;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.function.Supplier;

public class GuidebookCommand {
    private static final SuggestionProvider<ServerCommandSource> ADVANCEMENT_SUGGESTIONS = (ctx,builder) -> {
        MinecraftServer server = ctx.getSource().getServer();
        for(Advancement adv : server.getAdvancementLoader().getAdvancements()){
            if (adv.getId().getNamespace().equals(SanguinisLuxuria.MODID)) {
                builder.suggest(adv.getId().toString());
            }
        }
        return builder.buildFuture();
    };

    public static LiteralArgumentBuilder<ServerCommandSource> register() {
        // not quite proud of this indenting scheme...
        return CommandManager.literal("guidebook")
            .then(CommandManager.argument("playerTarget", EntityArgumentType.player())
            .then(CommandManager.literal("grant")
                .then(
                    CommandManager.literal("all").executes(ctx -> {
                        ServerPlayerEntity target = EntityArgumentType.getPlayer(ctx,"playerTarget");
                        Collection<Advancement> all = ctx.getSource().getServer().getAdvancementLoader().getAdvancements();

                        for (Advancement adv : all) {
                            if (adv.getId().getNamespace().equals(SanguinisLuxuria.MODID)) {
                                AdvancementProgress advProg = target.getAdvancementTracker().getProgress(adv);
                                for (String criterion : advProg.getUnobtainedCriteria()){
                                    target.getAdvancementTracker().grantCriterion(adv,criterion);
                                }
                            }
                        }
                        return 0;
                    })
                )
                .then(
                    CommandManager.argument("entryName",StringArgumentType.greedyString())
                        .suggests(ADVANCEMENT_SUGGESTIONS)
                        .executes(ctx -> {
                        ServerPlayerEntity target = EntityArgumentType.getPlayer(ctx,"playerTarget");
                        Advancement adv = ctx.getSource().getServer().getAdvancementLoader().get(Identifier.tryParse(StringArgumentType.getString(ctx,"entryName")));

                        AdvancementProgress advProg = target.getAdvancementTracker().getProgress(adv);
                        for (String criterion : advProg.getUnobtainedCriteria()){
                            target.getAdvancementTracker().grantCriterion(adv,criterion);
                        }

                        return 0;
                    })
                )
            )
            .then(CommandManager.literal("revoke")
                .then(
                    CommandManager.literal("all").executes(ctx -> {
                        ServerPlayerEntity target = EntityArgumentType.getPlayer(ctx,"playerTarget");
                        Collection<Advancement> all = ctx.getSource().getServer().getAdvancementLoader().getAdvancements();

                        for (Advancement adv : all) {
                            if (adv.getId().getNamespace().equals(SanguinisLuxuria.MODID)) {
                                AdvancementProgress advProg = target.getAdvancementTracker().getProgress(adv);
                                for (String criterion : advProg.getObtainedCriteria()){
                                    target.getAdvancementTracker().revokeCriterion(adv,criterion);
                                }
                            }
                        }
                        return 0;
                    })
                )
                .then(
                  CommandManager.argument("entryName",StringArgumentType.greedyString())
                    .suggests(ADVANCEMENT_SUGGESTIONS)
                    .executes(ctx -> {
                        ServerPlayerEntity target = EntityArgumentType.getPlayer(ctx,"playerTarget");
                        Advancement adv = ctx.getSource().getServer().getAdvancementLoader().get(Identifier.tryParse(StringArgumentType.getString(ctx,"entryName")));

                        AdvancementProgress advProg = target.getAdvancementTracker().getProgress(adv);
                        for (String criterion : advProg.getObtainedCriteria()){
                            target.getAdvancementTracker().revokeCriterion(adv,criterion);
                        }

                        return 0;
                    })
                )
            ));
    }
}
