package me.cosm1x.unomod.command;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.List;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import me.cosm1x.unomod.game.Table;
import me.cosm1x.unomod.game.TableManager;
import me.cosm1x.unomod.util.GenericUtils;
import me.cosm1x.unomod.util.Managers;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class UnoModCommand {
    private static final SuggestionProvider<ServerCommandSource> TABLE_SUGGESTIONS = (context, builder) -> {
        List<Table> tables = Managers.getTableManager().getTables();
        for (Table table : tables) {
            builder.suggest(table.getId());
        }
        return builder.buildFuture();
    };

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> 
            dispatcher.register(literal("unomod")
                .then(literal("reload")
                    .executes(UnoModCommand::reloadConfig))
                .then(literal("delete")
                    .then(argument("tableId", IntegerArgumentType.integer())
                        .suggests(TABLE_SUGGESTIONS)
                            .executes(context -> deleteTable(context, IntegerArgumentType.getInteger(context, "tableId")))))));
    }

    private static int deleteTable(CommandContext<ServerCommandSource> context, int id) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        TableManager tableManager = Managers.getTableManager();
        List<Table> tables = tableManager.getTables();
        if (player == null) {
            source.sendFeedback(Text.of("Only players can execute this command"), false);
            return 1;
        }
        if (tables.size() == 1) {
            tableManager.onTableBreak(tables.get(0).getCenter(), source.getWorld());
        } else {
            for (Table table : tables) {
                if (table.getId() == id) {
                    tableManager.onTableBreak(table.getCenter(), source.getWorld());
                }
            }
        }

        return 1;
    }

    private static int reloadConfig(CommandContext<ServerCommandSource> context) {
        GenericUtils.reloadConfig();
        return 1;
    }
}
