package me.cosm1x.unomod.access;

import me.cosm1x.unomod.game.Table;

public interface ServerPlayerEntityMixinAccess {
    void unomod$setTable(Table table);
    Table unomod$getTable();
    boolean unomod$hasAssignedTable();
    boolean unomod$isUnoPressed();
    boolean unomod$ingame();
    void unomod$toggleUno();
    void unomod$toggleIngame();
}
