package icu.samnyan.aqua.sega.diva.model.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
public enum ContestStageLimit {
    UNLIMITED(0),
    LIMITED(1);


    private final int value;

    ContestStageLimit(int i) {
        this.value = i;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
