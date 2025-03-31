package icu.samnyan.aqua.sega.ongeki.model

import com.fasterxml.jackson.annotation.JsonValue

enum class OgkGpProductID(@get:JsonValue val value: Int) {
    A_Credit1(0),
    A_Credit2(1),
    A_Credit3(2),
    B_Credit1(3),
    B_Credit2(4),
    B_Credit3(5),
    End(6)
}

enum class OgkIdListType {
    Invalid,
    NgMusic,
    Recommend;

    @JsonValue
    fun toValue() = ordinal
}

enum class OgkItemType {
    None,
    Card,
    NamePlate,
    Trophy,
    LimitBreakItem,
    AlmightyJewel,
    Money,
    Music,
    ProfileVoice,
    Present,
    ChapterJewel,
    GachaTicket,
    KaikaItem,
    ExpUpItem,
    IntimateUpItem,
    BookItem,
    SystemVoice,
    Costume,
    Medal,
    Attachment,
    UnlockItem,
    Max;

    @JsonValue
    fun toValue() = ordinal
}