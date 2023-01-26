package me.cosm1x.unomod.eventhandler;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class UseBlockEvent {
    public static void register() {
        UseBlockCallback.EVENT.register(EventHandlers::onUseBlock);
        UseBlockCallback.EVENT.register(EventHandlers::takeCard);
        UseBlockCallback.EVENT.register(EventHandlers::onUseItem);
        UseBlockCallback.EVENT.register(EventHandlers::onUnoButtonPressBlock);
    }
}
