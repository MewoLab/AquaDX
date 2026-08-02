package icu.samnyan.aqua.net.games.mai2

import ext.API
import ext.returns
import ext.vars
import icu.samnyan.aqua.net.games.ExportOptions
import icu.samnyan.aqua.net.games.IExportClass
import icu.samnyan.aqua.net.games.ImportClass
import icu.samnyan.aqua.net.games.ImportController
import icu.samnyan.aqua.sega.maimai2.model.Mai2Repos
import icu.samnyan.aqua.sega.maimai2.model.Mai2UserLinked
import icu.samnyan.aqua.sega.maimai2.model.request.Mai2UserFavoriteItem
import icu.samnyan.aqua.sega.maimai2.model.userdata.*
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.full.declaredMembers

@Suppress("UNCHECKED_CAST")
@RestController
@API("api/v2/game/mai2")
class Mai2Import(
    val repos: Mai2Repos,
) : ImportController<Maimai2DataExport, Mai2UserDetail>(
    "SDEZ", "mai2", Maimai2DataExport::class,
    exportFields = Maimai2DataExport::class.vars().associateBy {
        it.name.replace("List", "").lowercase()
    },
    exportRepos = Maimai2DataExport::class.vars()
        .filter { f -> f.name !in setOf(
            "gameId", "userData", "userPlaylogList", "userFavoriteMusicList", "userPrintDetailList"
        ) }
        .associateWith { field ->
            val repoName = when (field.name) {
                "userKaleidxScopeList" -> "userKaleidx"
                else -> field.name.replace("List", "")
            }
            Mai2Repos::class.declaredMembers
                .filter { f -> f returns Mai2UserLinked::class }
                .firstOrNull { f -> f.name == repoName }
                ?.call(repos) as Mai2UserLinked<*>? ?: error("No matching field found for ${field.name}")
        },
    artemisRenames = mapOf(
        "mai2_item_character" to ImportClass(Mai2UserCharacter::class),
        "mai2_item_charge" to ImportClass(Mai2UserCharge::class),
        "mai2_item_friend_season_ranking" to ImportClass(Mai2UserFriendSeasonRanking::class),
        "mai2_item_item" to ImportClass(Mai2UserItem::class, mapOf("isValid" to "valid")),
        "mai2_item_login_bonus" to ImportClass(Mai2UserLoginBonus::class),
        "mai2_item_map" to ImportClass(Mai2UserMap::class),
        "mai2_playlog" to ImportClass(Mai2UserPlaylog::class, mapOf("userId" to null)),
        "mai2_profile_activity" to ImportClass(Mai2UserAct::class, mapOf("activityId" to "id")),
        "mai2_profile_detail" to ImportClass(Mai2UserDetail::class,
            mapOf("user" to null, "version" to null, "isNetMember" to null),
            name = "userdata"),
        "mai2_profile_extend" to ImportClass(Mai2UserExtend::class, mapOf("version" to null)),
        "mai2_profile_option" to ImportClass(Mai2UserOption::class, mapOf("version" to null)),
        "mai2_score_best" to ImportClass(Mai2UserMusicDetail::class),
        "mai2_score_course" to ImportClass(Mai2UserCourse::class),
    ),
    customExporters = mapOf(
        Maimai2DataExport::userPlaylogList to { user: Mai2UserDetail, options: ExportOptions ->
            if (options.playlogAfter != null) {
                repos.userPlaylog.findByUserAndUserPlayDateAfter(user, options.playlogAfter)
            } else {
                repos.userPlaylog.findByUser(user)
            }
        },
        Maimai2DataExport::userFavoriteMusicList to { user: Mai2UserDetail, _: ExportOptions ->
            repos.userGeneralData.findByUserAndPropertyKey(user, "favorite_music")
                ?.propertyValue
                ?.takeIf { it.isNotEmpty() }
                ?.split(",")
                ?.mapIndexed { index, id -> Mai2UserFavoriteItem().apply { orderId = index; this.id = id.toInt() } }
                ?: emptyList()
        },
        Maimai2DataExport::userPrintDetailList to { user: Mai2UserDetail, _: ExportOptions ->
            repos.userPrintDetail.findByUser(user).map(::Mai2UserPrintDetailExport)
        },
    ) as Map<kotlin.reflect.KMutableProperty1<Maimai2DataExport, Any>, (Mai2UserDetail, ExportOptions) -> Any?>,
    customImporters = mapOf(
        Maimai2DataExport::userPlaylogList to { export: Maimai2DataExport, user: Mai2UserDetail ->
            repos.userPlaylog.saveAll(export.userPlaylogList.map { it.apply { it.user = user } })
        },
        Maimai2DataExport::userFavoriteMusicList to { export: Maimai2DataExport, user: Mai2UserDetail ->
            val favoriteMusicList = export.userFavoriteMusicList
            if (favoriteMusicList.isNotEmpty()) {
                val key = "favorite_music"
                // This field always imports as incremental, since the userGeneralData field (for backwards compatibility) is processed before this
                val data = repos.userGeneralData.findByUserAndPropertyKey(user, key)
                    ?: Mai2UserGeneralData().apply { this.user = user; propertyKey = key }
                repos.userGeneralData.save(data.apply {
                    propertyValue = favoriteMusicList.sortedBy { it.orderId }.map { it.id }.joinToString(",")
                })
            }
        },
        Maimai2DataExport::userPrintDetailList to { export: Maimai2DataExport, user: Mai2UserDetail ->
            val userCards = repos.userCard.findByUser(user).associateBy { it.cardId }
            repos.userPrintDetail.saveAll(export.userPrintDetailList.map { detail ->
                detail.toEntity(
                    user,
                    detail.userCardId?.let { cardId ->
                        userCards[cardId] ?: error("Missing Mai2 user card $cardId for print detail")
                    },
                )
            })
        },
    ) as Map<kotlin.reflect.KMutableProperty1<Maimai2DataExport, Any>, (Maimai2DataExport, Mai2UserDetail) -> Unit>
) {
    override fun createEmpty() = Maimai2DataExport()
    override val userDataRepo = repos.userData
}

data class Maimai2DataExport(
    override var userData: Mai2UserDetail = Mai2UserDetail(),
    var userExtend: Mai2UserExtend = Mai2UserExtend(),
    var userOption: Mai2UserOption = Mai2UserOption(),
    var userUdemae: Mai2UserUdemae = Mai2UserUdemae(),
    var mapEncountNpcList: List<Mai2MapEncountNpc> = mutableListOf(),
    var userActList: List<Mai2UserAct> = mutableListOf(),
    var userCharacterList: List<Mai2UserCharacter> = mutableListOf(),
    var userChargeList: List<Mai2UserCharge> = mutableListOf(),
    var userCourseList: List<Mai2UserCourse> = mutableListOf(),
    var userFavoriteList: List<Mai2UserFavorite> = mutableListOf(),
    var userFriendSeasonRankingList: List<Mai2UserFriendSeasonRanking> = mutableListOf(),
    var userGeneralDataList: List<Mai2UserGeneralData> = mutableListOf(),
    var userItemList: List<Mai2UserItem> = mutableListOf(),
    var userLoginBonusList: List<Mai2UserLoginBonus> = mutableListOf(),
    var userMapList: List<Mai2UserMap> = mutableListOf(),
    var userMusicDetailList: List<Mai2UserMusicDetail> = mutableListOf(),
    var userIntimateList: List<Mai2UserIntimate> = mutableListOf(),
    var userFavoriteMusicList: List<Mai2UserFavoriteItem> = mutableListOf(),
    var userKaleidxScopeList: List<Mai2UserKaleidx> = mutableListOf(),
    var userPlaylogList: List<Mai2UserPlaylog> = mutableListOf(),
    var userCardList: List<Mai2UserCard> = mutableListOf(),
    var userPrintDetailList: List<Mai2UserPrintDetailExport> = mutableListOf(),
    var userRegionsList: List<UserRegions> = mutableListOf(),
    // Not supported yet:
    // var userWeeklyData
    // var userMissionDataList
    // var userShopStockList
    // var userTradeItemList
    override var gameId: String = "SDEZ",
): IExportClass<Mai2UserDetail>

data class Mai2UserPrintDetailExport(
    var orderId: Long = 0,
    var printNumber: Int = 0,
    var printDate: String = "",
    var serialId: String = "",
    var placeId: Int = 0,
    var clientId: String = "",
    var printerSerialId: String = "",
    var userCardId: Int? = null,
    var cardRomVersion: Int = 0,
    var isHolograph: Boolean = false,
    var printOption1: Boolean = false,
    var printOption2: Boolean = false,
    var printOption3: Boolean = false,
    var printOption4: Boolean = false,
    var printOption5: Boolean = false,
    var printOption6: Boolean = false,
    var printOption7: Boolean = false,
    var printOption8: Boolean = false,
    var printOption9: Boolean = false,
    var printOption10: Boolean = false,
    var created: String = "",
) {
    constructor(detail: Mai2UserPrintDetail) : this(
        orderId = detail.orderId,
        printNumber = detail.printNumber,
        printDate = detail.printDate,
        serialId = detail.serialId,
        placeId = detail.placeId,
        clientId = detail.clientId,
        printerSerialId = detail.printerSerialId,
        userCardId = detail.userCard?.cardId,
        cardRomVersion = detail.cardRomVersion,
        isHolograph = detail.isHolograph,
        printOption1 = detail.printOption1,
        printOption2 = detail.printOption2,
        printOption3 = detail.printOption3,
        printOption4 = detail.printOption4,
        printOption5 = detail.printOption5,
        printOption6 = detail.printOption6,
        printOption7 = detail.printOption7,
        printOption8 = detail.printOption8,
        printOption9 = detail.printOption9,
        printOption10 = detail.printOption10,
        created = detail.created,
    )

    fun toEntity(user: Mai2UserDetail, userCard: Mai2UserCard?) = Mai2UserPrintDetail().also { detail ->
        detail.user = user
        detail.userCard = userCard
        detail.orderId = orderId
        detail.printNumber = printNumber
        detail.printDate = printDate
        detail.serialId = serialId
        detail.placeId = placeId
        detail.clientId = clientId
        detail.printerSerialId = printerSerialId
        detail.cardRomVersion = cardRomVersion
        detail.isHolograph = isHolograph
        detail.printOption1 = printOption1
        detail.printOption2 = printOption2
        detail.printOption3 = printOption3
        detail.printOption4 = printOption4
        detail.printOption5 = printOption5
        detail.printOption6 = printOption6
        detail.printOption7 = printOption7
        detail.printOption8 = printOption8
        detail.printOption9 = printOption9
        detail.printOption10 = printOption10
        detail.created = created
    }
}
