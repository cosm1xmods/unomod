package me.cosm1x.unomod.mixin;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.cosm1x.unomod.util.GenericUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.ServerPropertiesHandler;
import net.minecraft.text.Text;

@Mixin(ServerPropertiesHandler.class)
public class ResourcePackMixin {
    
    @Inject(method = "getServerResourcePackProperties", at = @At("HEAD"), cancellable = true)
    private static void getServerResourcePackProperties(String url, String sha1, @Nullable String hash, boolean required, String prompt, CallbackInfoReturnable<Optional<MinecraftServer.ServerResourcePackProperties>> cir) {
        if (GenericUtils.getConfig().replaceResourcePack()) {
            cir.setReturnValue(Optional.of(new MinecraftServer.ServerResourcePackProperties(
                "https://www.dropbox.com/s/pqmtutubludvie8/unomodrp.zip?dl=1",
                "9da49180c55edae879c92941c7848c6f959ae8d6", 
                true, 
                Text.of("This resource pack is required for UnoMod"))));
            cir.cancel();
        }

    }

}
