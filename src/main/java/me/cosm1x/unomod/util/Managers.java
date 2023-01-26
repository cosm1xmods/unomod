package me.cosm1x.unomod.util;

import me.cosm1x.unomod.card.CardManager;
import me.cosm1x.unomod.game.GameManager;
import me.cosm1x.unomod.game.TableManager;

public class Managers {
    private static TableManager uTableManager = new TableManager();
    private static CardManager uCardManager = new CardManager();
    private static GameManager uGameManager = new GameManager(uTableManager, uCardManager);

    public static void register() {};

    public static GameManager getGameManager() {
        return uGameManager;
    }

    public static TableManager getTableManager() {
        return uTableManager;
    }

    public static CardManager getCardManager() {
        return uCardManager;
    }
}
