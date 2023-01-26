package me.cosm1x.unomod.enums;

import net.minecraft.util.math.Direction;

public enum TurnDirection {
    NORMAL(new Direction[]{Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST}),
    REVERSE(new Direction[]{Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH});

    private Direction[] order;
    
    private TurnDirection(Direction[] order) {
        this.order = order;
    }

    public Direction[] getOrder() {
        return this.order;
    }
}
