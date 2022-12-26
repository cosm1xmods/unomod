package me.cosm1x.unomod.game;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.server.network.ServerPlayerEntity;

public class Game {
    private List<ServerPlayerEntity> players;
    private Map<ServerPlayerEntity, Seat> seats = Collections.emptyMap();
    
    private GameState gameState;
    private int id;
    private boolean justStarted;
    
    public List<ServerPlayerEntity> getPlayers() {
        return players;
    }
    
    public GameState getGameState() {
        return gameState;
    }
    
    public int getId() {
        return id;
    }

    protected Map<ServerPlayerEntity, Seat> getSeats() {
        return seats;
    }

    public void addPlayer(ServerPlayerEntity player) {
        this.players.add(player);
    }

    protected boolean getStartState() {
        return this.justStarted;
    }

    protected void setStartState(boolean state) {
        this.justStarted = state;
    }

    protected void setGameState(GameState state) {
        this.gameState = state;
    }

    public Game(List<ServerPlayerEntity> players, GameState gameState, int id) {
        this.players = players;
        this.gameState = gameState;
        this.id = id;
    }


}
