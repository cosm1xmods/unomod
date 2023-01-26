package me.cosm1x.unomod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.cosm1x.unomod.util.GenericUtils;
import me.cosm1x.unomod.util.Managers;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;

@Mixin(AbstractDecorationEntity.class)
public class AbstractDecorationEntityMixin {
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void unomod$onTick(CallbackInfo info) {
        // Check if ticked entity is ItemFrameEntity
        if (!(((AbstractDecorationEntity)(Object)this).getClass().equals(ItemFrameEntity.class))) return;

        ItemFrameEntity entity = (ItemFrameEntity)(((AbstractDecorationEntity)(Object)this));

        // Check if Item Frame direction = up and if it attached to Smooth Stone.
        if (!(GenericUtils.itemFrameTest(entity))) return;

        if (GenericUtils.existingTableTest(entity)) return;

        // Check if table is big(full version)
        if (GenericUtils.bigTableTest(entity)) {
            Managers.getTableManager().onBigTableForm(entity, entity.getWorld());
        }
    }
}
