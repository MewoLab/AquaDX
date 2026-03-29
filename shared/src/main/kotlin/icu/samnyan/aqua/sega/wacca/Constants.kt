package icu.samnyan.aqua.sega.wacca

import icu.samnyan.aqua.sega.wacca.model.db.WaccaUser
import icu.samnyan.aqua.sega.wacca.model.db.WcUserItem

enum class WaccaGrades(val value: Int) {
    D(1),
    C(2),
    B(3),
    A(4),
    AA(5),
    AAA(6),
    S(7),
    S_PLUS(11),
    SS(8),
    SS_PLUS(12),
    SSS(9),
    SSS_PLUS(13),
    MASTER(10);

    companion object {
        val valueMap = entries.associateBy { it.value }
    }
}

enum class WaccaDifficulty(val value: Int) {
    NORMAL(1),
    HARD(2),
    EXPERT(3),
    INFERNO(4);

    operator fun invoke() = value
}

enum class WaccaItemType(val type: Int) {
    XP(1),
    WP(2),
    MUSIC_UNLOCK(3),
    MUSIC_DIFFICULTY_UNLOCK(4),
    TITLE(5),
    ICON(6),
    TROPHY(7),
    SKILL(8),
    TICKET(9),
    NOTE_COLOR(10),
    NOTE_SOUND(11),
    BAHT_DO_NOT_SEND(12),
    BOOST_BADGE(13),
    GATE_POINT(14),
    NAVIGATOR(15),
    USER_PLATE(16),
    TOUCH_EFFECT(17);

    operator fun invoke() = type
    operator fun invoke(u: WaccaUser, id: Int, p1: Long = 0, p2: Long = 0, p3: Long = 0) =
        WcUserItem(type = this(), itemId = id, p1 = p1, p2 = p2, p3 = p3).apply { user = u }
}

enum class WaccaOptionType(val id: Int, val default: Int) {
    NOTE_SPEED(1, 5), // 1.0 - 6.0
    FIELD_MASK(2, 0), // 0-4
    NOTE_SOUND(3, 105001), // ID
    NOTE_COLOR(4, 203001), // ID
    BGM_VOLUME(5, 10), // 0-100 increments of 10
    BG_VIDEO(7, 0), // ask, on, or off
    MIRROR(101, 0), // none or left+right swap
    JUDGE_DISPLAY_POS(102, 0), // center, under, over, top or off
    JUDGE_DETAIL_DISPLAY(103, 0), // on or off
    MEASURE_GUIDELINES(105, 1), // on or off
    GUIDELINE_MASK(106, 1), // 0 - 5
    JUDGE_LINE_TIMING_ADJUST(108, 10), // -10 - 10
    NOTE_DESIGN(110, 3), // 1 - 5
    BONUS_EFFECT(114, 1), // on or off
    CHARA_VOICE(115, 1), // "usually" or none
    SCORE_DISPLAY_METHOD(116, 0), // add or subtract
    GIVE_UP(117, 0), // off, no touch, can't achieve s, ss, sss, pb
    GUIDELINE_SPACING(118, 1), // none, or a-g type
    CENTER_DISPLAY(119, 1), // none, combo, score add, score sub, s ss sss pb boarder
    RANKING_DISPLAY(120, 1), // on or off
    STAGE_UP_ICON_DISPLAY(121, 1), // on or off
    RATING_DISPLAY(122, 1), // on or off
    PLAYER_LEVEL_DISPLAY(123, 1), // on or off
    TOUCH_EFFECT(124, 1), // on or off
    GUIDE_SOUND_VOL(125, 30), // 0-100 increments of 10
    TOUCH_NOTE_VOL(126, 80), // 0-100 increments of 10
    HOLD_NOTE_VOL(127, 80), // 0
    SLIDE_NOTE_VOL(128, 80), // 0
    SNAP_NOTE_VOL(129, 80), // 0
    CHAIN_NOTE_VOL(130, 80), // 0
    BONUS_NOTE_VOL(131, 80), // 0
    GATE_SKIP(132, 0), // on
    KEY_BEAM_DISPLAY(133, 1), // on
    LEFT_SLIDE_NOTE_COLOR(201, 4), // red
    RIGHT_SLIDE_NOTE_COLOR(202, 3), // red
    FORWARD_SLIDE_NOTE_COLOR(203, 1), // red
    BACK_SLIDE_NOTE_COLOR(204, 2), // red
    MASTER_VOL(1001, 3), // 0
    SET_TITLE_ID(1002, 104001), // ID
    SET_ICON_ID(1003, 102001), // ID
    SET_NAV_ID(1004, 310001), // ID
    SET_PLATE_ID(1005, 211001), // ID
}

val waccaRatingMult = linkedMapOf(
    990_000 to 4.0,
    980_000 to 3.75,
    970_000 to 3.5,
    960_000 to 3.25,
    950_000 to 3.0,
    940_000 to 2.75,
    920_000 to 2.5,
    900_000 to 2.0,
    850_000 to 1.5,
    800_000 to 1.0,
    700_000 to 0.8,
    600_000 to 0.7,
    500_000 to 0.6,
    400_000 to 0.5,
    300_000 to 0.4,
    200_000 to 0.3,
    100_000 to 0.2,
    1 to 0.1,
    0 to 0.0
)

fun waccaRating(score: Int, level: Double): Int {
    val mult = waccaRatingMult.entries.firstOrNull { score >= it.key }?.value ?: 0.0
    return (level * mult * 10).toInt()
}