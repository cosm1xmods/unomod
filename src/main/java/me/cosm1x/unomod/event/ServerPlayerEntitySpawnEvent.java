package me.cosm1x.unomod.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface ServerPlayerEntitySpawnEvent {
    Event<ServerPlayerEntitySpawnEvent> EVENT = EventFactory.createArrayBacked(ServerPlayerEntitySpawnEvent.class,
        (listeners) -> (player) -> {
            for (ServerPlayerEntitySpawnEvent listener : listeners) {
                ActionResult result = listener.interact(player);
 
                if(result != ActionResult.PASS) {
                    return result;
                }
            }
 
        return ActionResult.PASS;
    });

    ActionResult interact(ServerPlayerEntity player);
}