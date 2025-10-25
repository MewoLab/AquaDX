package icu.samnyan.aqua.sega.diva.model.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
public enum SortMode {
    COMPLEXITY(0),
    NAME(1),
    RELEASE_DATE(2),
    MY_LIST_A(3),
    MY_LIST_B(4),
    MY_LIST_C(5);


    private final int value;

    SortMode(int i) {
        this.value = i;
    }

    public static SortMode fromValue(int i) {
        for (SortMode sortMode :
            SortMode.values()) {
            if (sortMode.getValue() == i) return sortMode;
        }
        return SortMode.RELEASE_DATE;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
