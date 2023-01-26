package me.cosm1x.unomod.enums;

import java.util.Arrays;
import java.util.Optional;

public enum CardColor {
    RED(1, "red"),
    GREEN(2, "green"),
    YELLOW(3    , "yellow"),
    BLUE(4, "blue"),
    WILD(5, "wild"),
    UNO(6, "uno");

    private int id;
    private String name;

    private CardColor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getid() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public static Optional<CardColor> valueOf(int value) {
        return Arrays.stream(values())
            .filter(cardColor -> cardColor.id == value)
            .findFirst();
    }
}
