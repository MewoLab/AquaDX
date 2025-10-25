package icu.samnyan.aqua.sega.diva.model.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
public enum ClearResult implements ValueEnum {
    NO_CLEAR(-1),
    MISS_TAKE(0),
    CHEAP(1),
    STANDARD(2),
    GREAT(3),
    EXCELLENT(4),
    PERFECT(5);


    private final int value;

    ClearResult(int i) {
        this.value = i;
    }

    public static ClearResult fromValue(int i) {
        for (ClearResult clearResult :
            ClearResult.values()) {
            if (clearResult.getValue() == i) return clearResult;
        }
        return ClearResult.NO_CLEAR;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
