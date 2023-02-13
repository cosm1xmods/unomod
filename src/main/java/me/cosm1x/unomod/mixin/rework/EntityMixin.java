package me.cosm1x.unomod.mixin.rework;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.cosm1x.unomod.access.EntityMixinAccess;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

@Mixin(Entity.class)
public class EntityMixin implements EntityMixinAccess{
    
    private UUID unomod$ownerUuid;

    public UUID unomod$getOwnerUuid() {
        return this.unomod$ownerUuid;
    }

    public void unomod$setOwnerUuid(UUID uuid) {
        this.unomod$ownerUuid = uuid;
    }

    @Inject(method = "writeNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
    private void unomod$writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info) {
        if (this.unomod$ownerUuid != null) {
            nbt.putUuid("UnoMod$OwnerUUID", this.unomod$ownerUuid);
        }
    }

    @Inject(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
    private void unomod$readNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.containsUuid("OwnerUUID")) {
            this.unomod$ownerUuid = nbt.getUuid("UnoMod$OwnerUUID");
        }
    }

    public boolean unomod$hasOwnerUuid() {
        return (this.unomod$ownerUuid == null) ? false : true;
    }
}
