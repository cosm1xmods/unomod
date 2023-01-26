package me.cosm1x.unomod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.cosm1x.unomod.access.EntityMixinAccess;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {

    @Shadow
    private Entity entity;

    @Inject(method = "startTracking(Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("HEAD"), cancellable = true)
    private void unomod$startTracking(ServerPlayerEntity player, CallbackInfo info) {
        if (((EntityMixinAccess)this.entity).unomod$getOwnerUuid() != null) {
            // if () {
            if (this.entity.getClass().equals(ServerPlayerEntity.class)) {
                return;
            }
            if (((EntityMixinAccess)this.entity).unomod$getOwnerUuid().equals(player.getUuid())) {
                return;
            }
            // }
            info.cancel(); 
        }
    }
}