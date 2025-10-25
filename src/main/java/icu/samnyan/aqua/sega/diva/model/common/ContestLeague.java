package icu.samnyan.aqua.sega.diva.model.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
public enum ContestLeague {
    BEGINNER(0),
    INTERMEDIATE(1),
    ADVANCED(2),
    PROFESSIONAL(3);


    private final int value;

    ContestLeague(int i) {
        this.value = i;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
