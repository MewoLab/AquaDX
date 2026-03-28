package icu.samnyan.aqua.net.games.ongeki

import ext.API
import ext.RP
import ext.minus
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.games.*
import icu.samnyan.aqua.net.utils.ongekiScores
import icu.samnyan.aqua.sega.ongeki.OgkUserDataRepo
import icu.samnyan.aqua.sega.ongeki.OgkUserGeneralDataRepo
import icu.samnyan.aqua.sega.ongeki.OgkUserMusicDetailRepo
import icu.samnyan.aqua.sega.ongeki.OgkUserOptionRepo
import icu.samnyan.aqua.sega.ongeki.OgkUserPlaylogRepo
import icu.samnyan.aqua.sega.ongeki.model.UserData
import icu.samnyan.aqua.sega.ongeki.model.UserOption
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties

@RestController
@API("api/v2/game/ongeki")
class Ongeki(
    override val us: AquaUserServices,
    override val playlogRepo: OgkUserPlaylogRepo,
    override val userDataRepo: OgkUserDataRepo,
    override val userMusicRepo: OgkUserMusicDetailRepo,
    val userGeneralDataRepository: OgkUserGeneralDataRepo,
    val userOptionRepo: OgkUserOptionRepo
): GameApiController<UserData>("ongeki", UserData::class) {
    override suspend fun trend(username: String) = us.cardByName(username) { card ->
        findTrend(playlogRepo.findByUser_Card_ExtId(card.extId)
            .map { TrendLog(it.playDate, it.playerRating) })
    }

    override val shownRanks = ongekiScores.filter { it.first >= 950000 }
    override val settableFields: Map<String, (UserData, String) -> Unit> by lazy { mapOf(
        "userName" to usernameCheck(SEGA_USERNAME_CHARS),

        "lastRomVersion" to { u, v -> u.lastRomVersion = v },
        "lastDataVersion" to { u, v -> u.lastDataVersion = v },
    ) }

    override suspend fun userSummary(username: String, token: String?) = us.cardByName(username) { card ->
        val extra = userGeneralDataRepository.findByUser_Card_ExtId(card.extId)
            .associate { it.propertyKey to it.propertyValue }

        val ratingComposition = mapOf(
            "best30" to (extra["rating_base_best"] ?: ""),
            "best15" to (extra["rating_base_new_best"] ?: ""),
            "recent10" to (extra["rating_base_hot_best"] ?: "")
        )

        genericUserSummary(card, ratingComposition)
    }

    @API("user-option")
    override suspend fun userOption(@RP token: String) = us.jwt.auth(token) { u ->
        userOptionRepo.findByUser_Card_ExtId(u.ghostCard.extId).getOrNull(0)
    }
    @API("user-option-set")
    override suspend fun userOptionSet(@RP token: String, @RP field: String, @RP value: Int): Any = us.jwt.auth(token) { u ->
        val gameOptions = userOptionRepo.findSingleByUser_Card_ExtId(u.ghostCard.extId)
        val property = UserOption::class.memberProperties.filterIsInstance<KMutableProperty1<Any, Any?>>().find{ it.name == field }

        if (property != null && gameOptions != null) {
            property.setter.call(gameOptions, value)
            userOptionRepo.save(gameOptions)
            200 - "Success"
        } else
            400 - "Invalid parameters"
    }
}
