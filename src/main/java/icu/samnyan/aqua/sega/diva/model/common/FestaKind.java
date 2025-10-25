package icu.samnyan.aqua.sega.diva.model.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
public enum FestaKind {
    PINK_FESTA(0),
    GREEN_FESTA(1);


    private final int value;

    FestaKind(int i) {
        this.value = i;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
