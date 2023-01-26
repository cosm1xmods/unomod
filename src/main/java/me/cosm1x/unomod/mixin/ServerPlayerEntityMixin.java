package me.cosm1x.unomod.mixin;

import me.cosm1x.unomod.access.ServerPlayerEntityMixinAccess;
import me.cosm1x.unomod.event.ServerPlayerEntitySpawnEvent;
import me.cosm1x.unomod.game.Table;
import me.cosm1x.unomod.util.GenericUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityMixinAccess {

	private Table unomod$table;
	private boolean unomod$ingame = false;
	private boolean unomod$isUnoPressed = false;

	public boolean unomod$hasAssignedTable() {
		return this.unomod$table != null;
	}

	public boolean unomod$isUnoPressed() {
		return this.unomod$isUnoPressed;
	}

	public boolean unomod$ingame() {
		return this.unomod$ingame;
	}

	public Table unomod$getTable() {
		return this.unomod$table;
	}

	public void unomod$setTable(Table table) {
		this.unomod$table = table;
	}

	public void unomod$toggleIngame() {
		this.unomod$ingame = !this.unomod$ingame;
	}

	public void unomod$toggleUno() {
		this.unomod$isUnoPressed = !this.unomod$isUnoPressed;
	}

	@Inject(method = "onSpawn()V", at = @At("TAIL"))
	private void unomod$onSpawn(CallbackInfo info) {
		ActionResult result = ServerPlayerEntitySpawnEvent.EVENT.invoker().interact((ServerPlayerEntity)(Object)this);
		if (result == ActionResult.FAIL) {
			info.cancel();
		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void unomod$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
		int id;
		boolean ingame;
		if (nbt.get("unomod") != null) {
			if ((id = nbt.getInt("Table")) != 0) {
				this.unomod$table = GenericUtils.findTableById(id);
			}
			ingame = nbt.getBoolean("ingame");
			if (ingame || !ingame) {
				this.unomod$ingame = ingame;
			}
		}
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void unomod$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
		NbtCompound unomod = new NbtCompound();
		if (this.unomod$table != null) {
			unomod.putInt("Table", this.unomod$table.getId());
		}
		unomod.putBoolean("ingame", this.unomod$ingame);
		nbt.put("unomod", unomod);
 	}
}
