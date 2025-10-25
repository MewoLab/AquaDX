package icu.samnyan.aqua.sega.diva.model.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
public enum Edition {
    ORIGINAL(0),
    EXTRA(1);


    private final int value;

    Edition(int i) {
        this.value = i;
    }

    public static Edition fromValue(int i) {
        for (Edition edition :
            Edition.values()) {
            if (edition.getValue() == i) return edition;
        }
        return Edition.ORIGINAL;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
