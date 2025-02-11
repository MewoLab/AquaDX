package icu.samnyan.aqua.net

import ext.*
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.utils.SUCCESS
import icu.samnyan.aqua.sega.chusan.model.Chu3Repos
import icu.samnyan.aqua.sega.general.model.sensitiveInfo
import icu.samnyan.aqua.sega.maimai2.model.Mai2Repos
import jakarta.transaction.Transactional
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@Configuration
@ConfigurationProperties(prefix = "aqua-net.bot")
class BotProps {
    var enabled: Boolean = false
    var secret: String = ""
}

@RestController
@ConditionalOnProperty("aqua-net.bot.enabled", havingValue = "true")
@API("/api/v2/bot")
class BotController(
    val us: AquaUserServices,
    val props: BotProps,
    val chu3Db: Chu3Repos,
    val mai2Db: Mai2Repos,
) {
    fun Str.checkSecret() {
        if (this != props.secret) 403 - "Invalid Secret"
    }

    @PostMapping("/ranking-ban")
    @Doc("Ban a user from the leaderboard", "Success status")
    suspend fun rankingBan(@RP secret: Str, @RP username: Str): Any {
        secret.checkSecret()

        return us.cardByName(username) { card ->
            card.rankingBanned = true
            us.cardRepo.save(card)

            SUCCESS
        }
    }

    @Transactional
    @PostMapping("/debug-user-profile")
    @Doc("Obtain debug information for a user card", "User card details")
    fun debugUserProfile(@RP secret: Str, @RP cardId: Str): Any {
        secret.checkSecret()

        // 1. Check if the card exist
        var cards = listOfNotNull(
            us.cardRepo.findByLuid(cardId)(),
        ).mut

        cardId.toLongOrNull()?.let {
            cards += listOfNotNull(
                us.cardRepo.findById(it)(),
                us.cardRepo.findByExtId(it)(),
            )

            cards += listOfNotNull(
                us.userRepo.findByAuId(it)?.cards,
            ).flatten()
        }

        cards += listOfNotNull(
            us.userRepo.findByEmailIgnoreCase(cardId)?.cards,
            us.userRepo.findByUsernameIgnoreCase(cardId)?.cards,
        ).flatten()

        cards += cards.flatMap {
            (it.aquaUser?.cards ?: emptyList()) + listOfNotNull(it.aquaUser?.ghostCard)
        }

        cards = cards.distinctBy { it.id }.mut

        return cards.map { card ->
            // Find all games played by this card
            val chu3 = chu3Db.userData.findByCard_ExtId(card.extId)()
            val mai2 = mai2Db.userData.findByCard_ExtId(card.extId)()
            val gamesDict = listOfNotNull(chu3, mai2).map {
                // Find the keychip owner
                val keychip = it.lastClientId
                val keychipOwner = keychip?.let { us.userRepo.findByKeychip(it) }

                mapOf(
                    "userData" to it,
                    "keychip" to keychip,
                    "keychipOwner" to keychipOwner,
                    "keychipOwnerCards" to keychipOwner?.cards?.map { it.sensitiveInfo() }
                )
            }

            mapOf(
                "card" to card.sensitiveInfo(),
                "games" to gamesDict
            )
        }
    }
}
