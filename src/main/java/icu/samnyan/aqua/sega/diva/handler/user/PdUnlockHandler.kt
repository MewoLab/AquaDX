package icu.samnyan.aqua.sega.diva.handler.user

import icu.samnyan.aqua.sega.diva.GameSessionRepository
import icu.samnyan.aqua.sega.diva.util.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.util.SessionNotFoundException
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.user.PdUnlockRequest
import icu.samnyan.aqua.sega.diva.model.response.BaseResponse
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService
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

        return BaseResponse(
            request.cmd,
            request.req_id,
            "ok"
        )
    }
}
