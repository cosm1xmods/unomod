package me.cosm1x.unomod.mixin;

import me.cosm1x.unomod.event.ServerPlayerEntitySpawnEvent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
	@Inject(method = "onSpawn()V", at = @At("TAIL"))
	private void unomod$onSpawn(CallbackInfo info) {
		ActionResult result = ServerPlayerEntitySpawnEvent.EVENT.invoker().interact((ServerPlayerEntity)(Object)this);
		if (result == ActionResult.FAIL) {
			info.cancel();
		}
	}

	// @Inject(method = "changeGameMode(Lnet/minecraft/world/GameMode;)Z", at = @At("HEAD"))
	// private void unomod$changeGameMode(GameMode gamemode, CallbackInfoReturnable<Boolean> info) {
	// 	if (Utils.getConfig().getSpectatorsBoolean()) {
	// 		System.out.println("configIf");
	// 		if (((ServerPlayerEntity)(Object)this).isSpectator()) {
	// 			System.out.println("isSpectator");
	// 			// for (Entity entity : Utils.getMarkedEntities(((ServerPlayerEntity)(Object)this).getWorld())) {
	// 				// ((ServerPlayerEntity)(Object)this).getWorld().getChunkManager().unloadEntity(entity);
	// 			// }
	// 		} else if (gamemode == GameMode.SPECTATOR) {
	// 			System.out.println("isNotSpectator");
	// 			for (Entity entity : Utils.getMarkedEntities(((ServerPlayerEntity)(Object)this).getWorld())) {
	// 				System.out.println(entity);
	// 				try {
	// 					// ((ServerPlayerEntity)(Object)this)
	// 					((ServerPlayerEntity)(Object)this).getWorld().getChunkManager().loadEntity(entity);
	// 					// ((ServerPlayerEntity)(Object)this).getWorld().getChunkManager().threadedAnvilChunkStorage
	// 				} catch (Exception e) {
	// 					e.printStackTrace();
	// 				}
	// 			}
	// 		}
	// 	}
	// }
}
