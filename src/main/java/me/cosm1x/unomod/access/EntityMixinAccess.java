package me.cosm1x.unomod.access;

import java.util.UUID;

public interface EntityMixinAccess {
    UUID unomod$getOwnerUuid();
    void unomod$setOwnerUuid(UUID uuid);
    boolean unomod$hasOwnerUuid();
}
