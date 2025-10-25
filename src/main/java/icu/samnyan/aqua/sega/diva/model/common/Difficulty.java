package icu.samnyan.aqua.sega.diva.model.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
public enum Difficulty {
    UNDEFINED(-1),
    EASY(0),
    NORMAL(1),
    HARD(2),
    EXTREME(3);


    private final int value;

    Difficulty(int i) {
        this.value = i;
    }

    public static Difficulty fromValue(int i) {
        for (Difficulty difficulty :
            Difficulty.values()) {
            if (difficulty.getValue() == i) return difficulty;
        }
        return Difficulty.UNDEFINED;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
