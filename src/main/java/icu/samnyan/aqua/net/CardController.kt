package icu.samnyan.aqua.net

import ext.*
import icu.samnyan.aqua.net.components.JWT
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.games.GenericUserDataRepo
import icu.samnyan.aqua.net.games.IUserData
import icu.samnyan.aqua.net.utils.AquaNetProps
import icu.samnyan.aqua.net.utils.SUCCESS
import icu.samnyan.aqua.sega.chusan.model.Chu3UserDataRepo
import icu.samnyan.aqua.sega.general.dao.CardRepository
import icu.samnyan.aqua.sega.general.model.Card
import icu.samnyan.aqua.sega.general.service.CardService
import icu.samnyan.aqua.sega.maimai2.model.Mai2UserDataRepo
import icu.samnyan.aqua.sega.wacca.model.db.WcUserRepo
import jakarta.persistence.EntityManager
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.jvm.optionals.getOrNull
import kotlin.random.Random

@RestController
@API("/api/v2/card")
class CardController(
    val jwt: JWT,
    val us: AquaUserServices,
    val cardService: CardService,
    val cardGameService: CardGameService,
    val cardRepository: CardRepository,
    val props: AquaNetProps
) {
    companion object {
        val log = logger()
    }

    @API("/summary")
    @Doc("Get a summary of the card, including the user's name, rating, and last login date.", "Summary of the card")
    suspend fun summary(@RP cardId: Str, @RP token: Str): Any {
        val user = jwt.auth(token)
        // DO NOT CHANGE THIS ERROR MESSAGE - The frontend uses it to detect if the card is not found
        val card = cardService.tryLookup(cardId) ?: (404 - "Card not found")

        if (card.aquaUser != null && card.aquaUser?.auId != user.auId) (404 - "Card not found")

        // Lookup data for each game
        return mapOf(
            "card" to card,
            "summary" to cardGameService.getSummary(card),
        )
    }

    @API("/user-games")
    @Doc("Get the game summary of the user, including the user's name, rating, and last login date.", "Summary of the user")
    suspend fun userGames(@RP username: Str) = us.cardByName(username) { card -> cardGameService.getSummary(card) }

    /**
     * Bind a card to the user. This action will migrate selected data from the card to the user's ghost card.
     *
     * Non-migrated data will not be lost, but will be inaccessible from the card until the card is unbound.
     *
     * @param token JWT token
     * @param cardId Card ID
     * @param migrate Things to migrate, stored as a comma-separated list of game IDs (e.g. "maimai2,chusan")
     */
    @API("/link")
    @Doc("Bind a card to the user. This action will migrate selected data from the card to the user's ghost card.", "Success message")
    suspend fun link(@RP token: Str, @RP cardId: Str, @RP migrate: Str) = jwt.auth(token) { u ->
        // Check if the user's card limit is reached
        if (u.cards.size >= props.linkCardLimit) 400 - "Card limit reached"

        // Try to look up the card
        val card = cardService.tryLookup(cardId)

        // If no card is found, create a new card
        if (card == null) {
            // Ensure the format of the card ID is correct
            val id = cardService.sanitizeCardId(cardId)

            // Create a new card
            cardService.registerByAccessCode(id, u)

            log.info("Net /card/link : Created new card $id for user ${u.username}")

            return SUCCESS
        }

        // If card is already bound
        if (card.aquaUser != null) 400 - "Card already bound to another user"

        // Bind the card
        card.aquaUser = u
        async { cardRepository.save(card) }

        // Migrate selected data to the new user
        val games = migrate.split(',')
        cardGameService.migrate(card, games)

        log.info("Net /card/link : Linked card ${card.id} to user ${u.username} and migrated data to ${games.joinToString()}")

        SUCCESS
    }

    @API("/unlink")
    @Doc("Unbind a card from the user. No data will be migrated during this action.", "Success message")
    suspend fun unlink(@RP token: Str, @RP cardId: Str) = jwt.auth(token) { u ->
        // Try to look up the card
        val card = cardService.tryLookup(cardId) ?: (404 - "Card not found")

        // If the card is not bound to the user
        if (card.aquaUser != u) 400 - "Card not linked to user"

        // Ghost cards cannot be unlinked
        if (card.isGhost) 400 - "Account virtual cards cannot be unlinked"

        // Unbind the card
        card.aquaUser = null
        async { cardRepository.save(card) }

        log.info("Net /card/unlink : Unlinked card ${card.id} from user ${u.username}")

        SUCCESS
    }

    @API("/default-game")
    @Doc("Get the default game for the card.", "Game ID")
    suspend fun defaultGame(@RP username: Str) = us.cardByName(username) { card ->
        mapOf("game" to cardGameService.getSummary(card).filterValues { it != null }.keys.firstOrNull())
    }
}

/**
 * Migrate data from the card to the user's ghost card
 *
 * Assumption: The card is already linked to the user.
 */
suspend fun <T : IUserData> migrateCard(repo: GenericUserDataRepo<T>, cardRepo: CardRepository, card: Card): Bool {
    val ghost = card.aquaUser!!.ghostCard

    // Check if data already exists in the user's ghost card
    async { repo.findByCard(ghost) }?.let {
        // Create a new dummy card for deleted data
        it.card = async {
            cardRepo.save(Card().apply {
                luid = "Migrated data of ghost card ${ghost.id} for user ${card.aquaUser!!.auId} on ${LocalDateTime.now(ZoneOffset.UTC).isoDateTime()}"
                // Randomize an extId outside the normal range
                extId = Random.nextLong(0x7FFFFFF7L shl 32, 0x7FFFFFFFL shl 32)
                registerTime = LocalDateTime.now()
                accessTime = registerTime
            })
        }
        async { repo.save(it) }
    }

    // Migrate data from the card to the user's ghost card
    // An easy migration is to change the UserData card field to the user's ghost card
    val data = async { repo.findByCard(card) } ?: return false
    data.card = card.aquaUser!!.ghostCard
    async { repo.save(data) }
    return true
}

suspend fun getSummaryFor(repo: GenericUserDataRepo<*>, card: Card): Map<Str, Any>? {
    val data = async { repo.findByCard(card) } ?: return null
    return mapOf(
        "name" to data.userName,
        "rating" to data.playerRating,
        "lastLogin" to data.lastPlayDate,
    )
}

@Service
class CardGameService(
    val maimai2: Mai2UserDataRepo,
    val chusan: Chu3UserDataRepo,
    val wacca: WcUserRepo,
    val ongeki: icu.samnyan.aqua.sega.ongeki.dao.userdata.UserDataRepository,
    val diva: icu.samnyan.aqua.sega.diva.dao.userdata.PlayerProfileRepository,
    val safety: AquaNetSafetyService,
    val cardRepo: CardRepository,
    val em: EntityManager
) {
    companion object {
        val log = logger()
    }

    suspend fun migrate(crd: Card, games: List<String>) = async {
        // Migrate data from the card to the user's ghost card
        // An easy migration is to change the UserData card field to the user's ghost card
        games.forEach { game ->
            when (game) {
                "mai2" -> migrateCard(maimai2, cardRepo, crd)
                "chu3" -> migrateCard(chusan, cardRepo, crd)
                "ongeki" -> migrateCard(ongeki, cardRepo, crd)
                "wacca" -> migrateCard(wacca, cardRepo, crd)
                // TODO: diva
//                "diva" -> diva.findByPdId(card.extId.toInt()).getOrNull()?.let {
//                    it.pdId = card.aquaUser!!.ghostCard
//                }
            }
        }
    }

    suspend fun getSummary(card: Card) = async {
        mapOf(
            "mai2" to getSummaryFor(maimai2, card),
            "chu3" to getSummaryFor(chusan, card),
            "ongeki" to getSummaryFor(ongeki, card),
            "wacca" to getSummaryFor(wacca, card),
            "diva" to diva.findByPdId(card.extId).getOrNull()?.let {
                mapOf(
                    "name" to it.playerName,
                    "rating" to it.level,
                )
            },
        )
    }

    // Every hour
    @Scheduled(fixedDelay = 3600000)
    suspend fun autoBan() {
        log.info("Running auto-ban")
        val time = millis()

        // Ban any players with unacceptable names
        for (repo in listOf(maimai2, chusan, wacca, ongeki)) {
            val all = async { repo.findAllNonBanned() }
            val isSafe = safety.isSafeBatch(all.map { it.userName })
            val toSave = all.filterIndexed { i, _ -> !isSafe[i] }.mapNotNull { it.card }
            if (toSave.isNotEmpty()) {
                log.info("Banning users ${toSave.joinToString(", ")}")
                toSave.forEach { it.rankingBanned = true }
                async { cardRepo.saveAll(toSave) }
            }
        }

        log.info("Auto-ban completed in ${millis() - time}ms")
    }
}
