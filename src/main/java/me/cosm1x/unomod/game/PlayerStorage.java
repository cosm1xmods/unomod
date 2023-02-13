package me.cosm1x.unomod.game;

import java.util.ArrayList;
import java.util.List;

import me.cosm1x.unomod.access.ServerPlayerEntityMixinAccess;
import me.cosm1x.unomod.card.Card;
import me.cosm1x.unomod.enums.CardColor;
import me.cosm1x.unomod.enums.CardValue;
import me.cosm1x.unomod.enums.GameState;
import me.cosm1x.unomod.enums.TurnDirection;
import me.cosm1x.unomod.util.GenericUtils;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.inventory.Inventories;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class PlayerStorage {
    private ServerPlayerEntity southPlayer;
    private ServerPlayerEntity eastPlayer;
    private ServerPlayerEntity northPlayer;
    private ServerPlayerEntity westPlayer;
    private ServerPlayerEntity currentPlayer;
    private List<ServerPlayerEntity> players = new ArrayList<>();
    private TurnDirection turnDirection;
    private boolean skipPlayer = false;

    public PlayerStorage () {
        this.turnDirection = TurnDirection.NORMAL;
    };

    protected PlayerStorage (ServerPlayerEntity southPlayer, ServerPlayerEntity eastPlayer, ServerPlayerEntity northPlayer, ServerPlayerEntity westPlayer) {
        this.southPlayer = southPlayer;
        this.eastPlayer = eastPlayer;
        this.northPlayer = northPlayer;
        this.westPlayer = westPlayer;
        this.turnDirection = TurnDirection.NORMAL;
    }

    public void setSouthPlayer(ServerPlayerEntity southPlayer) {
        this.southPlayer = southPlayer;
    }

    public void setEastPlayer(ServerPlayerEntity eastPlayer) {
        this.eastPlayer = eastPlayer;
    }

    public void setNorthPlayer(ServerPlayerEntity northPlayer) {
        this.northPlayer = northPlayer;
    }

    public void setWestPlayer(ServerPlayerEntity westPlayer) {
        this.westPlayer = westPlayer;
    }

    public void setCurrentPlayer(ServerPlayerEntity currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ServerPlayerEntity getSouthPlayer() {
        return this.southPlayer;
    }

    public ServerPlayerEntity getEastPlayer() {
        return this.eastPlayer;
    }

    public ServerPlayerEntity getNorthPlayer() {
        return this.northPlayer;
    }

    public ServerPlayerEntity getWestPlayer() {
        return this.westPlayer;
    }

    public ServerPlayerEntity getCurrentPlayer() {
        return this.currentPlayer;
    }

    public TurnDirection getTurnDirection() {
        return this.turnDirection;
    }

    public void skipPlayer() {
        this.skipPlayer = true;
    }

    public void turn() {
        ServerWorld world = this.currentPlayer.getWorld();
        Table table = GenericUtils.getTableByPlayer(this.currentPlayer);
        Game game = table.getGame();
        ScoreboardPlayerScore oldScore = world.getScoreboard().getPlayerScore(this.currentPlayer.getEntityName(), world.getScoreboard().getObjective("table"+GenericUtils.getTableByPlayer(this.currentPlayer).getId()));
        if (oldScore.getScore() == 0) {
            game.setGameState(GameState.ENDGAME);
            this.currentPlayer.removeStatusEffect(StatusEffects.GLOWING);
            return;
        }
        this.currentPlayer.removeStatusEffect(StatusEffects.GLOWING);
        if (oldScore.getScore() == 1 && (!((ServerPlayerEntityMixinAccess)this.currentPlayer).unomod$isUnoPressed())) {
            Inventories.remove(this.currentPlayer.getInventory(), GenericUtils.UNO_BUTTON, 64, false);
            for (ServerPlayerEntity player : this.players) {
                if (player.equals(this.currentPlayer)) continue;
                player.giveItemStack(new Card(CardColor.UNO, CardValue.ZERO).toItemStack());
            }
        } else {
            boolean clear = false;
            for (ServerPlayerEntity player : this.players) {
                if (player.equals(this.currentPlayer)) continue;
                ScoreboardPlayerScore playerScore = world.getScoreboard().getPlayerScore(player.getEntityName(), world.getScoreboard().getObjective("table"+GenericUtils.getTableByPlayer(player).getId()));
                if (playerScore.getScore() == 1)
                    if (player.equals(this.players.get(GenericUtils.getPreviousIndex(this.players, this.players.indexOf(this.currentPlayer), 1))))
                        clear = true;
            }
            if (clear) {
                Card.clearUnoButton(this.players);
            }
        }
        if ((!skipPlayer)) {
            this.currentPlayer = this.turnDirection.equals(TurnDirection.NORMAL)
            ? this.players.get(GenericUtils.getNextIndex(this.players, this.players.indexOf(currentPlayer)))
            : this.players.get(GenericUtils.getPreviousIndex(this.players, this.players.indexOf(currentPlayer)));
        } else {
            this.currentPlayer = this.turnDirection.equals(TurnDirection.NORMAL)
            ? this.players.get(GenericUtils.getNextIndex(this.players, this.players.indexOf(currentPlayer), 1))
            : this.players.get(GenericUtils.getPreviousIndex(this.players, this.players.indexOf(currentPlayer), 1)); 
            this.skipPlayer = false;
        }
        ScoreboardPlayerScore newScore = world.getScoreboard().getPlayerScore(this.currentPlayer.getEntityName(), world.getScoreboard().getObjective("table"+GenericUtils.getTableByPlayer(this.currentPlayer).getId()));
        
        if (newScore.getScore() == 2) {
            ((ServerPlayerEntityMixinAccess)this.currentPlayer).unomod$unToggleUno();
            this.currentPlayer.giveItemStack(new Card(CardColor.UNO, CardValue.ZERO).toItemStack());
        }

    }

    public ServerPlayerEntity getNextPlayer() {
        return this.turnDirection.equals(TurnDirection.NORMAL)
        ? this.players.get(GenericUtils.getNextIndex(this.players, this.players.indexOf(currentPlayer)))
        : this.players.get(GenericUtils.getPreviousIndex(this.players, this.players.indexOf(currentPlayer)));
    }

    public void flipDirection() {
        this.turnDirection = this.turnDirection.equals(TurnDirection.NORMAL) ? TurnDirection.REVERSE : TurnDirection.NORMAL;
    }

    public List<ServerPlayerEntity> getPlayers() {
        return this.players;
    }

    public void updatePlayers() {
        this.players.clear();
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    this.players.add(southPlayer);
                    break;
                case 1:
                    this.players.add(westPlayer);
                    break;
                case 2:
                    this.players.add(northPlayer);
                    break;
                case 3:
                    this.players.add(eastPlayer);
                    break;
            }
        }
    }
}
