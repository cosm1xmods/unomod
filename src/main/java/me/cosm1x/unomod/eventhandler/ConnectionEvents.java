package me.cosm1x.unomod.eventhandler;

// import com.google.common.collect.BiMap;

// import me.cosm1x.unomod.UnoMod;
// import me.cosm1x.unomod.event.ServerPlayerEntitySpawnEvent;

// import net.minecraft.entity.Entity;
// import net.minecraft.network.packet.s2c.play.EntitiesDestroyS2CPacket;
// import net.minecraft.server.network.ServerPlayerEntity;
// import net.minecraft.util.ActionResult;

public class ConnectionEvents {
    public static void register() {
        // ServerPlayerEntitySpawnEvent.EVENT.register(ConnectionEvents::onPlayerSpawn);
        // ServerPlayConnectionEvents.DISCONNECT.register(ConnectionEvents::onPlayerDisconnect);
        
    }

    // private static ActionResult onPlayerSpawn(ServerPlayerEntity player) {
    //     BiMap<Entity, ServerPlayerEntity> tmpMap = UnoMod.entities.inverse();
    //     for (Entity entity : UnoMod.entities.values()) {
    //         if (!(tmpMap.get(entity).getUuid().equals(player.getUuid()))) {
    //             player.networkHandler.sendPacket(new EntitiesDestroyS2CPacket(entity.getId()));
    //         }
    //     }
        
    //     return ActionResult.SUCCESS;
    // }

// Don't delete this, maybe i'll need that one time...
//     private static void onPlayerDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
//         for (ServerPlayerEntity pl : UnoMod.entities.keySet()) {
//             if (pl == handler.getPlayer()) {
//                 Entity entity = UnoMod.entities.get(pl);
//                 entity.remove(RemovalReason.UNLOADED_WITH_PLAYER);
//                 UnoMod.entities.remove(pl, entity);
//             }
//         }
//     }
}
