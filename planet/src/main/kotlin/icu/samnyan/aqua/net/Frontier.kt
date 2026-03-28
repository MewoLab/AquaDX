package icu.samnyan.aqua.net

import ext.*
import icu.samnyan.aqua.sega.general.service.CardService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController

@Configuration
@ConfigurationProperties(prefix = "aqua-net.frontier")
class FrontierProps {
    var enabled: Boolean = false
    var ftk: String = ""
}

@RestController
@ConditionalOnProperty("aqua-net.frontier.enabled", havingValue = "true")
@API("/api/v2/frontier")
class Frontier(
    val cardService: CardService,
    val props: FrontierProps,
    val fedy: Fedy
) {
    fun Str.checkFtk() {
        if (this != props.ftk) 403 - "Invalid FTK"
    }

    @API("/register-card")
    @Doc("Register a new card by access code", "Card information")
    suspend fun registerCard(@RP ftk: Str, @RP accessCode: Str): Any {
        ftk.checkFtk()

        if (accessCode.length != 20) 400 - "Invalid access code"
//        if (!accessCode.startsWith("9900")) 400 - "Frontier access code must start with 9900"
        if (async { cardService.cardRepo.findByLuid(accessCode) } != null) 400 - "Card already registered"

        val card = async { cardService.registerByAccessCode(accessCode) }
        
        fedy.onCardCreated(accessCode, card.extId)
        
        return mapOf(
            "card" to card,
            "id" to card.extId  // Expose hidden ID
        )
    }

    @API("/lookup-card")
    @Doc("Lookup a card by access code", "Card information")
    suspend fun lookupCard(@RP ftk: Str, @RP accessCode: Str): Any {
        ftk.checkFtk()

        val card = cardService.tryLookup(accessCode) ?: (404 - "Card not found")
        return mapOf(
            "card" to card,
            "id" to card.extId  // Expose hidden ID
        )
    }
}
