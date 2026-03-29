package icu.samnyan.aqua.sega.maimai2.model

class UserRivalMusic(
    var musicId: Int,
    var userRivalMusicDetailList: MutableList<UserRivalMusicDetail> = mutableListOf()
)

class UserRivalMusicDetail(
    var level: Int,
    var achievement: Int,
    var deluxscoreMax: Int
)