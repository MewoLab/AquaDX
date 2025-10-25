package icu.samnyan.aqua.sega.diva.model.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
public enum Result {
    FAILED(0),
    SUCCESS(1);


    private final int value;

    Result(int i) {
        this.value = i;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
