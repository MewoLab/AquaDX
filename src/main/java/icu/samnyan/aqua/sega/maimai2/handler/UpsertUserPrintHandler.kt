package icu.samnyan.aqua.sega.maimai2.handler

import ext.invoke
import ext.logger
import ext.long
import ext.parsing
import icu.samnyan.aqua.sega.general.BaseHandler
import icu.samnyan.aqua.sega.general.service.CardService
import icu.samnyan.aqua.sega.maimai2.model.Mai2Repos
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserPrintDetail
import icu.samnyan.aqua.sega.util.BasicMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ThreadLocalRandom

@Component("Maimai2UpsertUserPrintHandler")
class UpsertUserPrintHandler(
    val mapper: BasicMapper,
    val db: Mai2Repos,
    val cardService: CardService,
    @param:Value("\${game.cardmaker.card.expiration:15}") val expirationTime: Long,
) : BaseHandler {
    val log = logger()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")

    override fun handle(request: Map<String, Any>): Any? {
        val userId = parsing { request["userId"]!!.long }
        val userData = db.userData.findByCardExtId(userId) ?: return null

        val userPrint = parsing { mapper.convert(request["userPrintDetail"]!!, Mai2UserPrintDetail::class.java) }
        val newCard = userPrint.userCard ?: return null

        newCard.user = userData
        newCard.startDate = LocalDateTime.now().format(formatter)
        newCard.endDate = LocalDateTime.now().plusDays(expirationTime).format(formatter)
        newCard.id = db.userCard.findByUserAndCardId(newCard.user, newCard.cardId)?.id ?: 0
        db.userCard.save(newCard)

        userPrint.user = userData
        userPrint.serialId = buildString {
            append(String.format("%010d", ThreadLocalRandom.current().nextLong(0L, 9999999999L)))
            append(String.format("%010d", ThreadLocalRandom.current().nextLong(0L, 9999999999L)))
        }
        db.userPrintDetail.save(userPrint)

        userData.card?.let { cardService.updateCardTimestamp(it, "mai2") }

        return mapOf(
            "returnCode" to 1,
            "orderId" to 0,
            "serialId" to userPrint.serialId,
            "startDate" to newCard.startDate,
            "endDate" to newCard.endDate
        )
    }
}
