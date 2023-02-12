package me.cosm1x.unomod.enums;

import java.util.Arrays;
import java.util.Optional;

public enum CardValue {
    ZERO(0, '0'),
    ONE(1, '1'),
    TWO(2, '2'),
    THREE(3, '3'),
    FOUR(4, '4'),
    FIVE(5, '5'),
    SIX(6, '6'),
    SEVEN(7, '7'),
    EIGHT(8, '8'),
    NINE(9, '9'),
    BLOCK(10, 'B'),
    REVERSE(11, 'R'),
    DRAW(12, '+'),
    WILD(13, 'C'),
    REDCHANGE(14, 'R'),
    GREENCHANGE(15, 'G'),
    BLUECHANGE(16, 'B'),
    YELLOWCHANGE(17, 'Y'),
    REDDRAW(18, 'r'),
    GREENDRAW(19, 'g'),
    BLUEDRAW(20, 'b'),
    YELLOWDRAW(21, 'y');

    private int id;
    private char symbol;
    
    private CardValue(int id, char symbol) {
        this.id = id;
        this.symbol = symbol;
    }
    
    public int getId() {
        return this.id;
    }

    public char getSymbol() {
        return this.symbol;
    }

    public static Optional<CardValue> valueOf(int value) {
        return Arrays.stream(values())
            .filter(cardValue -> cardValue.id == value)
            .findFirst();
    }
}
