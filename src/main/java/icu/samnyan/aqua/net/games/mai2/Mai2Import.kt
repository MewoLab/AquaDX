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

@RestController
@API("api/v2/game/mai2")
class Mai2Import(
    val repos: Mai2Repos,
) : ImportController<Maimai2DataExport, Mai2UserDetail>(
    "SDEZ", Maimai2DataExport::class,
    exportFields = Maimai2DataExport::class.vars().associateBy {
        it.name.replace("List", "").lowercase()
    },
    exportRepos = Maimai2DataExport::class.vars()
        .filter { f -> f.name !in setOf("gameId", "userData") }
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
            if (options.playlogSince != null) {
                repos.userPlaylog.findByUserAndUserPlayDateAfter(user, options.playlogSince)
            } else {
                repos.userPlaylog.findByUser(user)
            }
        },
        Maimai2DataExport::userFavoriteMusicList to { user: Mai2UserDetail, _: ExportOptions ->
            repos.userGeneralData.findByUserAndPropertyKey(user, "favorite_music").orElse(null)
                ?.propertyValue
                ?.takeIf { it.isNotEmpty() }
                ?.split(",")
                ?.mapIndexed { index, id -> Mai2UserFavoriteItem().apply { orderId = index; this.id = id.toInt() } }
                ?: emptyList()
        }
    ) as Map<kotlin.reflect.KMutableProperty1<Maimai2DataExport, Any>, (Mai2UserDetail, ExportOptions) -> Any?>,
    customImporters = mapOf(
        Maimai2DataExport::userFavoriteMusicList to { export: Maimai2DataExport, user: Mai2UserDetail ->
            val favoriteMusicList = export.userFavoriteMusicList
            if (favoriteMusicList.isNotEmpty()) {
                val key = "favorite_music"
                val data = repos.userGeneralData.findByUserAndPropertyKey(user, key).orElse(null)
                    ?: Mai2UserGeneralData().apply { this.user = user; propertyKey = key }
                repos.userGeneralData.save(data.apply {
                    propertyValue = favoriteMusicList.map { it.id }.joinToString(",")
                })
            }
        }
    ) as Map<kotlin.reflect.KMutableProperty1<Maimai2DataExport, Any>, (Maimai2DataExport, Mai2UserDetail) -> Unit>
) {
    override fun createEmpty() = Maimai2DataExport()
    override val userDataRepo = repos.userData
}

data class Maimai2DataExport(
    override var userData: Mai2UserDetail,
    var userExtend: Mai2UserExtend,
    var userOption: Mai2UserOption,
    var userUdemae: Mai2UserUdemae,
    var mapEncountNpcList: List<Mai2MapEncountNpc>,
    var userActList: List<Mai2UserAct>,
    var userCharacterList: List<Mai2UserCharacter>,
    var userChargeList: List<Mai2UserCharge>,
    var userCourseList: List<Mai2UserCourse>,
    var userFavoriteList: List<Mai2UserFavorite>,
    var userFriendSeasonRankingList: List<Mai2UserFriendSeasonRanking>,
    var userGeneralDataList: List<Mai2UserGeneralData>,
    var userItemList: List<Mai2UserItem>,
    var userLoginBonusList: List<Mai2UserLoginBonus>,
    var userMapList: List<Mai2UserMap>,
    var userMusicDetailList: List<Mai2UserMusicDetail>,
    var userIntimateList: List<Mai2UserIntimate>,
    var userFavoriteMusicList: List<Mai2UserFavoriteItem>,
    var userKaleidxScopeList: List<Mai2UserKaleidx>,
    var userPlaylogList: List<Mai2UserPlaylog>,
    override var gameId: String = "SDEZ",
): IExportClass<Mai2UserDetail> {
    constructor() : this(Mai2UserDetail(), Mai2UserExtend(), Mai2UserOption(), Mai2UserUdemae(),
        mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(),
        mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(),
        mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())
}
