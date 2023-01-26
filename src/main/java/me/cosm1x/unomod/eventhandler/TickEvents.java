package me.cosm1x.unomod.eventhandler;

import me.cosm1x.unomod.util.Managers;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class TickEvents {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> Managers.getGameManager().tick(server));
        ServerTickEvents.END_SERVER_TICK.register(server -> Managers.getTableManager().tick(server));
    }
}
