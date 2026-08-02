package icu.samnyan.aqua.net.games.chu3

import ext.API
import ext.returns
import ext.vars
import icu.samnyan.aqua.net.games.ExportOptions
import icu.samnyan.aqua.net.games.IExportClass
import icu.samnyan.aqua.net.games.ImportClass
import icu.samnyan.aqua.net.games.ImportController
import icu.samnyan.aqua.sega.chusan.model.Chu3Repos
import icu.samnyan.aqua.sega.chusan.model.Chu3UserLinked
import icu.samnyan.aqua.sega.chusan.model.userdata.*
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.full.declaredMembers

@Suppress("UNCHECKED_CAST")
@RestController
@API("api/v2/game/chu3")
class Chu3Import(
    val repos: Chu3Repos,
) : ImportController<Chu3DataExport, Chu3UserData>(
    "SDHD", "chu3", Chu3DataExport::class,
    exportFields = Chu3DataExport::class.vars().associateBy {
        it.name.replace("List", "").lowercase()
    },
    exportRepos = Chu3DataExport::class.vars()
        .filter { f -> f.name !in setOf("gameId", "userData", "userLoginBonusList") }
        .associateWith { Chu3Repos::class.declaredMembers
            .filter { f -> f returns Chu3UserLinked::class }
            .firstOrNull { f -> f.name == it.name || f.name == it.name.replace("List", "") }
            ?.call(repos) as Chu3UserLinked<*>? ?: error("No matching field found for ${it.name}")
        },
    artemisRenames = mapOf(
        "chuni_item_character" to ImportClass(UserCharacter::class),
        "chuni_item_duel" to ImportClass(UserDuel::class),
        "chuni_item_item" to ImportClass(Chu3UserItem::class, mapOf("isValid" to "valid")),
//        "chuni_item_login_bonus" to ImportClass(UserLoginBonus::class, mapOf("isWatched" to "watched")),
        "chuni_item_map_area" to ImportClass(UserMap::class),
        "chuni_profile_activity" to ImportClass(Chu3UserActivity::class, mapOf("activityId" to "id")),
        "chuni_profile_charge" to ImportClass(UserCharge::class),
        "chuni_profile_data" to ImportClass(Chu3UserData::class, mapOf("user" to null, "version" to null, "isNetMember" to null)),
        "chuni_profile_option" to ImportClass(UserGameOption::class, mapOf("version" to null)),
        "chuni_score_best" to ImportClass(UserMusicDetail::class),
        "chuni_score_playlog" to ImportClass(UserPlaylog::class),
//        "chuni_item_favorite" to ImportClass(UserFavorite::class),
//        "chuni_profile_emoney" to ImportClass(UserEmoney::class),
//        "chuni_profile_overpower" to ImportClass(UserOverpower::class),
//        "chuni_profile_recent_rating" to ImportClass(UserRecentRating::class),
    ),
    customExporters = mapOf(
        Chu3DataExport::userLoginBonusList to { user: Chu3UserData, _: ExportOptions ->
            repos.userLoginBonus.findByUser(user.card!!.extId.toInt())
        },
    ) as Map<kotlin.reflect.KMutableProperty1<Chu3DataExport, Any>, (Chu3UserData, ExportOptions) -> Any?>,
    customImporters = mapOf(
        Chu3DataExport::userLoginBonusList to { export: Chu3DataExport, user: Chu3UserData ->
            val userId = user.card!!.extId.toInt()
            repos.userLoginBonus.deleteAll(repos.userLoginBonus.findByUser(userId))
            repos.userLoginBonus.saveAll(export.userLoginBonusList.map { it.apply {
                id = 0
                this.user = userId
            } })
        },
    ) as Map<kotlin.reflect.KMutableProperty1<Chu3DataExport, Any>, (Chu3DataExport, Chu3UserData) -> Unit>,
) {
    override fun createEmpty() = Chu3DataExport()
    override val userDataRepo = repos.userData
}


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
    var userMateList: List<UserMate> = emptyList(),
    var userGeneralDataList: List<UserGeneralData> = emptyList(),
    var userMiscList: Chu3UserMisc = Chu3UserMisc(),
    var userCardPrintStateList: List<UserCardPrintState> = emptyList(),
    var userGachaList: List<UserGacha> = emptyList(),
    var userRegionsList: List<UserRegions> = emptyList(),
    var userCMissionList: List<UserCMission> = emptyList(),
    var userCMissionProgressList: List<UserCMissionProgress> = emptyList(),
    var netBattleLogList: List<Chu3NetBattleLog> = emptyList(),
    var userChallengeList: List<Chu3UserChallenge> = emptyList(),
    var userLinkedVerseList: List<Chu3UserLinkedVerse> = emptyList(),
    var userVoteList: List<UserVote> = emptyList(),
    var userLoginBonusList: List<UserLoginBonus> = emptyList(),
): IExportClass<Chu3UserData> {
    constructor() : this("SDHD",
        Chu3UserData(), UserGameOption(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), ArrayList(), Chu3UserMisc())
}
