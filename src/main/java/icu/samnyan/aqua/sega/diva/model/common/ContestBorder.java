package icu.samnyan.aqua.sega.diva.model.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
public enum ContestBorder {
    NONE(-1),
    BRONZE(0),
    SILVER(1),
    GOLD(2);


    private final int value;

    ContestBorder(int i) {
        this.value = i;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
