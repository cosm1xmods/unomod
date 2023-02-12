package me.cosm1x.unomod.eventhandler;

import net.fabricmc.fabric.api.event.player.UseItemCallback;

public class UseItemEvent {
    public static void register() {
        UseItemCallback.EVENT.register(EventHandlers::onItemCarduse);
        // UseItemCallback.EVENT.register(EventHandlers::onUnoButtonPressItem);
    }
}
