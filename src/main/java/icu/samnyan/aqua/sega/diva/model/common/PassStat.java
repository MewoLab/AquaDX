package icu.samnyan.aqua.sega.diva.model.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
public enum PassStat {
    MISS(0),
    SET(1),
    RESET(2),
    REISSUE(3);


    private final int value;

    PassStat(int i) {
        this.value = i;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
