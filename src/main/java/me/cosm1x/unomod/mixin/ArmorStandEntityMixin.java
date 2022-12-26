package me.cosm1x.unomod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.cosm1x.unomod.access.EntityMixinAccess;
import me.cosm1x.unomod.util.GenericUtils;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

@Mixin(ArmorStandEntity.class)
public class ArmorStandEntityMixin {
    @Inject(method = "interactAt(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", at = @At(value = "HEAD"), cancellable = true)
    private void unomod$onRightClick(PlayerEntity player, Vec3d hitPos, Hand hand, CallbackInfoReturnable<ActionResult> info) {
        if (((EntityMixinAccess)((ArmorStandEntity)(Object)this)).unomod$getOwnerUuid() != null) {
            GenericUtils.onRightClick(((ArmorStandEntity)(Object)this), (ServerPlayerEntity)player);
            info.setReturnValue(ActionResult.SUCCESS);
        }
    }
}