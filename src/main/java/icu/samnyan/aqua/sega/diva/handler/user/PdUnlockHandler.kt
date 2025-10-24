package icu.samnyan.aqua.sega.diva.handler.user

import icu.samnyan.aqua.sega.diva.dao.userdata.GameSessionRepository
import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.exception.SessionNotFoundException
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.user.PdUnlockRequest
import icu.samnyan.aqua.sega.diva.model.response.BaseResponse
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class PdUnlockHandler(
    private val playerProfileService: PlayerProfileService,
    private val gameSessionRepository: GameSessionRepository
) : BaseHandler() {
    fun handle(request: PdUnlockRequest): Any {
        val profile = playerProfileService.findByPdId(request.pd_id).orElseThrow<ProfileNotFoundException?>(
            Supplier { ProfileNotFoundException() })
        val session = gameSessionRepository.findByPdId(profile)
            .orElseThrow<SessionNotFoundException?>(Supplier { SessionNotFoundException() })

        gameSessionRepository.delete(session)

        val response = BaseResponse(
            request.cmd,
            request.req_id,
            "ok"
        )

        val resp = this.build(mapper.toMap(response))
        logger.info("Response: {}", resp)

        return resp
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(PdUnlockHandler::class.java)
    }
}
