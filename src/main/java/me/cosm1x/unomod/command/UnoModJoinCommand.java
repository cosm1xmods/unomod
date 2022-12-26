package me.cosm1x.unomod.command;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mojang.brigadier.context.CommandContext;

import me.cosm1x.unomod.access.EntityMixinAccess;
import me.cosm1x.unomod.game.Game;
import me.cosm1x.unomod.game.GameManager;
import me.cosm1x.unomod.game.GameState;
import me.cosm1x.unomod.util.GenericUtils;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class UnoModJoinCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> 
            dispatcher.register(literal("unomod")
                .then(literal("join")
                        .executes(UnoModJoinCommand::joinGame))));
    }

    private static int joinGame(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendFeedback(Text.of("Only players can execute this command"), false);
            return 1;
        }

        ServerWorld serverWorld = player.getWorld();
            
        // GameManager gameManager = GenericUtils.getGameManager();
        // List<Game> games = gameManager.getGames();
        // if (games.isEmpty()) {
        //     List<ServerPlayerEntity> players = new ArrayList<ServerPlayerEntity>();
        //     players.add(player);
        //     gameManager.addGame(new Game(players, GameState.PREGAME, GenericUtils.getMaxGameId(gameManager)));
        // } else {
        //     Game qgame;
        //     for (Game game : games) {
        //         if (game.getGameState() == GameState.ENDGAME) continue;
        //         if (game.getGameState() == GameState.PLAY) continue;
        //         if (game.getGameState() == GameState.PREGAME) {
        //             if (game.getPlayers().size() == 4) continue;
        //             game.addPlayer(player);
        //         }
        //         if ((qgame = GenericUtils.getFirstQueuedGame(games)) != null) {
        //             if (qgame == game) {
        //                 game.addPlayer(player);
        //             }
        //         }
        //     }
        // }
        // player.teleport(serverWorld, 0, 0, 0, 0, 0);

        Entity lentity = EntityType.ARMOR_STAND.create(serverWorld, null, null, player.getBlockPos(), SpawnReason.COMMAND, true, false);
        
        NbtCompound nbt = NbtPredicate.entityToNbt(lentity);

        nbt.putBoolean("Small", true);
        nbt.putBoolean("NoGravity", true);
        nbt.putBoolean("NoBasePlate", true);
        nbt.putBoolean("Invisible", true);

        UUID uUID = lentity.getUuid();
        lentity.readNbt(nbt);
        lentity.setUuid(uUID);

        lentity.refreshPositionAndAngles(player.getX(), player.getY()+1, player.getZ(), player.getYaw(), player.getPitch());

        // ((EntityMixinAccess)lentity).unomod$setOwnerUuid(player.getUuid());

        // System.out.println(Utils.getMarkedEntities(serverWorld));

        // serverWorld.getBlockState(null).getBlock();

        serverWorld.spawnEntity(lentity);

        // player.networkHandler.sendPacket(lentity.createSpawnPacket());

        return 1;
    }
}
