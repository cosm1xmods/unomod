package me.cosm1x.unomod.enums;

import net.minecraft.util.math.ColorHelper;

public enum CardColorEnum {
    ABSTRACT(000000), GREEN(1), RED(2), BLUE(3), YELLOW(4);
    private int type;
    CardColorEnum(int type) {
        this.type = type;
    }
    public int getType() {
        return this.type;
    }
}
