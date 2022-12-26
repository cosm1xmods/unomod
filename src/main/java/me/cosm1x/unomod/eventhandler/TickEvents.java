package me.cosm1x.unomod.eventhandler;

import me.cosm1x.unomod.util.GenericUtils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class TickEvents {
    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(world -> GenericUtils.getGameManager().tick(world));
    }
}
