package icu.samnyan.aqua.sega.diva.model.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
public enum ContestNormaType {
    SCORE(0),
    PERCENTAGE(1),
    COOL_PERCENTAGE(2);


    private final int value;

    ContestNormaType(int i) {
        this.value = i;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
