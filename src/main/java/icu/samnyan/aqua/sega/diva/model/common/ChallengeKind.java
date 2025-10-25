package icu.samnyan.aqua.sega.diva.model.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
public enum ChallengeKind {
    UNDEFINED(-1),
    CLEAR(0),
    GREAT(1),
    EXCELLENT(2),
    PERFECT(3),
    COMPLETED(4);


    private final int value;

    ChallengeKind(int i) {
        this.value = i;
    }

    public static ChallengeKind fromValue(int i) {
        for (ChallengeKind challengeKind :
            ChallengeKind.values()) {
            if (challengeKind.getValue() == i) return challengeKind;
        }
        return ChallengeKind.UNDEFINED;
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
