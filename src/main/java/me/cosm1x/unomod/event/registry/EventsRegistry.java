package me.cosm1x.unomod.event.registry;

import me.cosm1x.unomod.event.handlers.EventHandlers;
import me.cosm1x.unomod.util.Managers;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;

public class EventsRegistry {
    public static void register() {
        
        // TickEvents
        ServerTickEvents.END_SERVER_TICK.register(server -> Managers.getGameManager().tick(server));
        ServerTickEvents.END_SERVER_TICK.register(server -> Managers.getTableManager().tick(server));

        // BlockBreakEvent
        PlayerBlockBreakEvents.BEFORE.register(EventHandlers::beforeButtonBreak);
        PlayerBlockBreakEvents.BEFORE.register(EventHandlers::beforeCenterBlockBreak);
        PlayerBlockBreakEvents.AFTER.register(EventHandlers::afrerCenterBlockBreak);
        
        // UseBlockEvent
        UseBlockCallback.EVENT.register(EventHandlers::onUseBlock);
        UseBlockCallback.EVENT.register(EventHandlers::takeCard);
        UseBlockCallback.EVENT.register(EventHandlers::onUseItem);
        
        // UseItemEvent
        UseItemCallback.EVENT.register(EventHandlers::onItemCardUse);
    }
}
