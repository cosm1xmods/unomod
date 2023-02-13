package me.cosm1x.unomod.game;

import me.cosm1x.unomod.card.Card;
import me.cosm1x.unomod.enums.GameState;
import net.minecraft.entity.AreaEffectCloudEntity;

public class Game {
    private GameState gameState;
    private boolean justStarted;
    private Card nextTopCard;    
    private Card topCard;
    private AreaEffectCloudEntity cardEntity;
    private boolean endgame = false;
    
    public AreaEffectCloudEntity getCardEntity() {
        return cardEntity;
    }

    public void setCardEntity(AreaEffectCloudEntity cardEntity) {
        this.cardEntity = cardEntity;
    }

    public Card getNextTopCard() {
        return this.nextTopCard;
    }

    public void setNextTopCard(Card nextTopCard) {
        this.nextTopCard = nextTopCard;
    }
    
    public Card getTopCard() {
        return topCard;
    }

    public void setTopCard(Card topCard) {
        this.topCard = topCard;
    }

    public GameState getGameState() {
        return gameState;
    }

    protected boolean getStartState() {
        return this.justStarted;
    }

    protected void setStartState(boolean state) {
        this.justStarted = state;
    }

    public void setGameState(GameState state) {
        this.gameState = state;
    }

    public Game(GameState gameState) {
        this.gameState = gameState;
    }

    public void toEndgame() {
        this.endgame = true;
    }

    public boolean isEndgame() {
        return this.endgame;
    }
}
