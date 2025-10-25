package icu.samnyan.aqua.sega.diva.model.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
public enum PreStartResult {
    SUCCESS(1),
    FAILED(0),
    CARD_TOO_NEW(-1),
    ALREADY_PLAYING(-2),
    NEW_REGISTRATION(-3),
    CARD_BANNED(-4);


    private final int value;

    PreStartResult(int i) {
        this.value = i;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
