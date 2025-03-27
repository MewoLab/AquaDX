package icu.samnyan.aqua.sega.maimai2.handler

import ext.invoke
import icu.samnyan.aqua.sega.general.BaseHandler
import icu.samnyan.aqua.sega.maimai2.model.Mai2Repos
import icu.samnyan.aqua.sega.maimai2.model.UserRating
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserRate
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserUdemae
import icu.samnyan.aqua.sega.util.jackson.BasicMapper
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("Maimai2GetUserRatingHandler")
class GetUserRatingHandler(
    val mapper: BasicMapper,
    val repos: Mai2Repos
) : BaseHandler {
    override fun handle(request: Map<String, Any>): Any {
        val userId = (request["userId"] as Number?)!!.toLong()
        val empty: List<Mai2UserRate> = ArrayList()

        val ur = UserRating()

        repos.userData.findByCardExtId(userId)()?.let {
            ur.rating = it.playerRating
        }

        // Old charts (standard) = 25
        ur.ratingList = repos.userGeneralData.findByUser_Card_ExtIdAndPropertyKey(userId, "recent_rating")()
            ?.let { loadRateData(it.propertyValue) } ?: empty

        // New charts (DX) = 15
        ur.newRatingList = repos.userGeneralData.findByUser_Card_ExtIdAndPropertyKey(userId, "recent_rating_new")()
            ?.let { loadRateData(it.propertyValue) } ?: empty

        ur.nextRatingList = repos.userGeneralData.findByUser_Card_ExtIdAndPropertyKey(userId, "recent_rating_next")()
            ?.let { loadRateData(it.propertyValue) } ?: empty

        ur.nextNewRatingList = repos.userGeneralData.findByUser_Card_ExtIdAndPropertyKey(userId, "recent_rating_next_new")()
            ?.let { loadRateData(it.propertyValue) } ?: empty

        ur.udemae = repos.userUdemae.findSingleByUser_Card_ExtId(userId)() ?: Mai2UserUdemae()

        return mapOf(
            "userId" to userId,
            "userRating" to ur
        )
    }

    fun loadRateData(value: String) = value.split(",").filter { it.isNotBlank() }.map {
        val (musicId, level, beforeRating, afterRating) = it.split(":")
        Mai2UserRate(musicId.toInt(), level.toInt(), beforeRating.toInt(), afterRating.toInt())
    }
}
