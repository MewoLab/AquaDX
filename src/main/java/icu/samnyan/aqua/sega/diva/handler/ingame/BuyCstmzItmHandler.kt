package icu.samnyan.aqua.sega.diva.handler.ingame

import icu.samnyan.aqua.sega.diva.dao.gamedata.DivaCustomizeRepository
import icu.samnyan.aqua.sega.diva.dao.userdata.GameSessionRepository
import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.exception.SessionNotFoundException
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.common.Result
import icu.samnyan.aqua.sega.diva.model.request.ingame.BuyCstmzItmRequest
import icu.samnyan.aqua.sega.diva.model.response.ingame.BuyCstmzItmResponse
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession
import icu.samnyan.aqua.sega.diva.service.PlayerCustomizeService
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class BuyCstmzItmHandler(
    private val divaCustomizeRepository: DivaCustomizeRepository,
    private val playerProfileService: PlayerProfileService,
    private val playerCustomizeService: PlayerCustomizeService,
    private val gameSessionRepository: GameSessionRepository
) : BaseHandler() {
    fun handle(request: BuyCstmzItmRequest): Any {
        val profile = playerProfileService.findByPdId(request.pd_id).orElseThrow<ProfileNotFoundException?>(
            Supplier { ProfileNotFoundException() })

        val session = gameSessionRepository.findByPdId(profile)
            .orElseThrow<SessionNotFoundException?>(Supplier { SessionNotFoundException() })

        val customizeOptional = divaCustomizeRepository.findById(request.cstmz_itm_id)

        val response: BuyCstmzItmResponse?
        if (customizeOptional.isEmpty) {
            response = BuyCstmzItmResponse(
                request.cmd,
                request.req_id,
                "ok",
                Result.FAILED
            )
        } else {
            if (session.vp < customizeOptional.get().price) {
                response = BuyCstmzItmResponse(
                    request.cmd,
                    request.req_id,
                    "ok",
                    Result.FAILED
                )
            } else {
                playerCustomizeService.buy(profile, request.cstmz_itm_id)
                session.vp = session.vp - customizeOptional.get().price
                gameSessionRepository.save<GameSession?>(session)

                response = BuyCstmzItmResponse(
                    request.cmd,
                    request.req_id,
                    "ok",
                    Result.SUCCESS,
                    request.cstmz_itm_id,
                    playerCustomizeService.getModuleHaveString(profile),
                    session.vp
                )
            }
        }

        val resp = this.build(mapper.toMap(response))
        logger.info("Response: {}", resp)

        return resp
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(BuyCstmzItmHandler::class.java)
    }
}
