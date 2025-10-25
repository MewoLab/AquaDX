package icu.samnyan.aqua.sega.diva.handler.card

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.CardProcedureRequest
import icu.samnyan.aqua.sega.diva.model.CardProcedureResponse
import icu.samnyan.aqua.sega.diva.model.common.Result
import icu.samnyan.aqua.sega.diva.model.common.StartMode
import icu.samnyan.aqua.sega.diva.model.db.userdata.GameSession
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class CardProcedureHandler(val db: DivaRepos) {
    fun handle(request: CardProcedureRequest): Any {
        val profileOptional = db.profile.findByPdId(request.aime_id)
        if (profileOptional.isEmpty) {
            return CardProcedureResponse(
                Result.FAILED
            )
        } else {
            val profile = profileOptional.get()

            val sessionOptional = db.gameSession.findByPdId(profile)
            if (sessionOptional.isPresent) {
                val session = sessionOptional.get()
                if (session.lastUpdateTime.isBefore(LocalDateTime.now().minusMinutes(5))) {
                    db.gameSession.delete(session)
                }
                return CardProcedureResponse(
                    Result.FAILED
                )
            } else {
                val session = GameSession(
                    ThreadLocalRandom.current().nextInt(100, 99999),
                    profile,
                    StartMode.CARD_PROCEDURE,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    -1,
                    -1,
                    -1,
                    profile.level,
                    profile.levelExp,
                    profile.level,
                    profile.levelExp,
                    profile.vocaloidPoints
                )

                db.gameSession.save(session)
                return CardProcedureResponse(
                    Result.SUCCESS,
                    100,
                    session.acceptId,
                    profile.pdId,
                    profile.playerName,
                    profile.level,
                    profile.levelExp,
                    profile.levelTitle,
                    profile.plateEffectId,
                    profile.plateId,
                    profile.vocaloidPoints,
                    profile.passwordStatus
                )
            }
        }
    }
}
