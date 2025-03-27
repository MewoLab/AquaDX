package icu.samnyan.aqua.api.model.resp.sega.chuni.v2.external

import icu.samnyan.aqua.net.games.IExportClass
import icu.samnyan.aqua.sega.chusan.model.userdata.*

data class Chu3DataExport(
    override var gameId: String = "SDHD",
    override var userData: Chu3UserData,
    var userGameOption: UserGameOption,
    var userActivityList: List<Chu3UserActivity>,
    var userCharacterList: List<UserCharacter>,
    var userChargeList: List<UserCharge>,
    var userCourseList: List<UserCourse>,
    var userDuelList: List<UserDuel>,
    var userItemList: List<Chu3UserItem>,
    var userMapList: List<UserMap>,
    var userMusicDetailList: List<UserMusicDetail>,
    var userPlaylogList: List<UserPlaylog>,
): IExportClass<Chu3UserData> {
    constructor() : this("SDHD",
        Chu3UserData(), UserGameOption(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList())
}

