package me.cosm1x.unomod.game;

import me.cosm1x.unomod.card.Card;
import me.cosm1x.unomod.card.CardManager;
import me.cosm1x.unomod.enums.GameState;
import me.cosm1x.unomod.util.GenericUtils;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.inventory.Inventories;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionTypes;

public class GameManager {
    
    private TableManager tableManager;
    private CardManager cardManager;
    
    public GameManager(TableManager tableManager, CardManager cardManager) {
        this.tableManager = tableManager;
        this.cardManager = cardManager;
    }

    public void tick(MinecraftServer server) {
        ServerWorld world = server.getOverworld();
        if (world.getScoreboard().getTeam("unomod_winners") == null) this.setupWinnersTeam(world);
        if (!(world.getDimensionKey().equals(DimensionTypes.OVERWORLD))) return;
        for (Table table : this.tableManager.getTables()) {
            if (table.isDirty()) continue;
            Game game = table.getGame();
            if (game.getGameState() == GameState.PREGAME) {
                this.setupGame(world, game, table);
            }

            if (game.getGameState() == GameState.INGAME) {
                this.tickInGame(world, game, table);
            }

            if (game.getGameState() == GameState.ENDGAME) {
                this.tickEndGame(world, game, table);
            }
        }
    }
    
    private void setupWinnersTeam(ServerWorld world) {
        Scoreboard scoreboard = world.getScoreboard();
        Team team = scoreboard.addTeam("unomod_winners");
        team.setColor(Formatting.GREEN);
    }

    private void setupGame(ServerWorld world, Game game, Table table) {
        PlayerStorage playerStorage = table.getPlayerStorage();
        for (ServerPlayerEntity player : playerStorage.getPlayers()) {
            this.cardManager.giveCard(player, 6, true);
        }
        this.cardManager.setupCardCounters(world, table, playerStorage.getPlayers());
        this.cardManager.setupTopCardEntity(world, table);
        game.setGameState(GameState.INGAME);
    }

    private void tickInGame(ServerWorld world, Game game, Table table) {
        Card nextTopCard = game.getNextTopCard();
        Card topCard = game.getTopCard();
        if (nextTopCard == null) return;
        if (nextTopCard.equals(topCard)) return;
        AreaEffectCloudEntity cardEntity = game.getCardEntity();
        cardEntity.setCustomName(Text.literal(" " + nextTopCard.getValue().getSymbol()).setStyle(Style.EMPTY.withFont(new Identifier("unomod", nextTopCard.getColor().getName()))));
        game.setTopCard(nextTopCard);

        PlayerStorage playerStorage = table.getPlayerStorage();
        ServerPlayerEntity currentPlayer = playerStorage.getCurrentPlayer();
        StatusEffectInstance effect = new StatusEffectInstance(StatusEffects.GLOWING, 2147483647, 1, false, false, true);
        if (!(currentPlayer.hasStatusEffect(StatusEffects.GLOWING))) {
            currentPlayer.setStatusEffect(effect, null);
        }
    }
    
    private void tickEndGame(ServerWorld world, Game game, Table table) {
        ServerPlayerEntity winner = table.getPlayerStorage().getCurrentPlayer();
        if (winner.hasStatusEffect(StatusEffects.GLOWING)) return;
        if (world.getScoreboard().addPlayerToTeam(winner.getEntityName(), world.getScoreboard().getTeam("unomod_winners")) && !(world.getScoreboard().getTeam("unomod_winners").getPlayerList().contains(winner.getEntityName()))) {
            StatusEffectInstance effect = new StatusEffectInstance(StatusEffects.GLOWING, 5, 1, false, false, true);
            winner.setStatusEffect(effect, null);
        } else {
            world.getScoreboard().removePlayerFromTeam(winner.getEntityName(), world.getScoreboard().getTeam("unomod_winners"));
            for (ServerPlayerEntity player : table.getPlayerStorage().getPlayers()) {
                Inventories.remove(player.getInventory(), GenericUtils.IS_A_CARD, 64, false);
            }
            table.markDirty();
        }
    }

}