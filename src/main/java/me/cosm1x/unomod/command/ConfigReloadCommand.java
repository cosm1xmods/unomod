package me.cosm1x.unomod.command;

import static net.minecraft.server.command.CommandManager.*;

import com.mojang.brigadier.context.CommandContext;

import me.cosm1x.unomod.util.GenericUtils;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.server.command.ServerCommandSource;

public class ConfigReloadCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> 
            dispatcher.register(literal("unomod")
                .then(literal("reload"))
                    .executes(ConfigReloadCommand::reloadConfig)));
    }

    private static int reloadConfig(CommandContext<ServerCommandSource> context) {
        GenericUtils.reloadConfig();
        // Utils.onReloadCommand(context);
        return 1;
    }
}
