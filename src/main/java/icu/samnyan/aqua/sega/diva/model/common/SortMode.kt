package icu.samnyan.aqua.sega.diva.model.common

import com.fasterxml.jackson.annotation.JsonValue

enum class SortMode(@get:JsonValue val value: Int) {
    COMPLEXITY(0),
    NAME(1),
    RELEASE_DATE(2),
    MY_LIST_A(3),
    MY_LIST_B(4),
    MY_LIST_C(5);

    companion object {
        fun fromValue(i: Int): SortMode {
            for (sortMode in entries) {
                if (sortMode.value == i) return sortMode
            }
            return RELEASE_DATE
        }
    }
}

enum class StartMode {
    PRE_START,
    START,
    CARD_PROCEDURE
}

enum class Result(@get:JsonValue val value: Int) {
    FAILED(0),
    SUCCESS(1)
}

enum class PreStartResult(@get:JsonValue val value: Int) {
    SUCCESS(1),
    FAILED(0),
    CARD_TOO_NEW(-1),
    ALREADY_PLAYING(-2),
    NEW_REGISTRATION(-3),
    CARD_BANNED(-4)
}

enum class PassStat(@get:JsonValue val value: Int) {
    MISS(0),
    SET(1),
    RESET(2),
    REISSUE(3)
}

enum class FestaKind(@get:JsonValue val value: Int) {
    PINK_FESTA(0),
    GREEN_FESTA(1)
}

enum class Edition(@get:JsonValue val value: Int) {
    ORIGINAL(0),
    EXTRA(1);

    companion object {
        fun fromValue(i: Int): Edition {
            for (edition in entries) {
                if (edition.value == i) return edition
            }
            return ORIGINAL
        }
    }
}

enum class Difficulty(@get:JsonValue val value: Int) {
    UNDEFINED(-1),
    EASY(0),
    NORMAL(1),
    HARD(2),
    EXTREME(3);

    companion object {
        fun fromValue(i: Int): Difficulty {
            for (difficulty in entries) {
                if (difficulty.value == i) return difficulty
            }
            return UNDEFINED
        }
    }
}

enum class ContestStageLimit(@get:JsonValue val value: Int) {
    UNLIMITED(0),
    LIMITED(1)
}

enum class ContestNormaType(@get:JsonValue val value: Int) {
    SCORE(0),
    PERCENTAGE(1),
    COOL_PERCENTAGE(2)
}

enum class ContestLeague(@get:JsonValue val value: Int) {
    BEGINNER(0),
    INTERMEDIATE(1),
    ADVANCED(2),
    PROFESSIONAL(3)
}

enum class ContestBorder(@get:JsonValue val value: Int) {
    NONE(-1),
    BRONZE(0),
    SILVER(1),
    GOLD(2)
}

enum class ClearResult(@get:JsonValue val value: Int) {
    NO_CLEAR(-1),
    MISS_TAKE(0),
    CHEAP(1),
    STANDARD(2),
    GREAT(3),
    EXCELLENT(4),
    PERFECT(5);

    companion object {
        fun fromValue(i: Int): ClearResult {
            for (clearResult in entries) {
                if (clearResult.value == i) return clearResult
            }
            return NO_CLEAR
        }
    }
}

enum class ChallengeKind(@get:JsonValue val value: Int) {
    UNDEFINED(-1),
    CLEAR(0),
    GREAT(1),
    EXCELLENT(2),
    PERFECT(3),
    COMPLETED(4);

    companion object {
        fun fromValue(i: Int): ChallengeKind {
            for (challengeKind in entries) {
                if (challengeKind.value == i) return challengeKind
            }
            return UNDEFINED
        }
    }
}