package icu.samnyan.aqua.sega.diva.handler.ingame

import icu.samnyan.aqua.sega.diva.dao.userdata.GameSessionRepository
import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.exception.SessionNotFoundException
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.ingame.StageStartRequest
import icu.samnyan.aqua.sega.diva.model.response.BaseResponse
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class StageStartHandler(
    private val gameSessionRepository: GameSessionRepository,
    private val playerProfileService: PlayerProfileService
) : BaseHandler() {
    fun handle(request: StageStartRequest): Any {
        if (request.getPd_id() != -1L) {
            val profile = playerProfileService.findByPdId(request.getPd_id()).orElseThrow<ProfileNotFoundException?>(
                Supplier { ProfileNotFoundException() })
            val session = gameSessionRepository.findByPdId(profile)
                .orElseThrow<SessionNotFoundException?>(Supplier { SessionNotFoundException() })

            val stageArr = request.getStg_ply_pv_id()
            var stageIndex = 0
            if (stageArr[0] != -1) {
                stageIndex = 0
            }
            if (stageArr[1] != -1) {
                stageIndex = 1
            }
            if (stageArr[2] != -1) {
                stageIndex = 2
            }
            if (stageArr[3] != -1) {
                stageIndex = 3
            }
            session.stageIndex = stageIndex
            gameSessionRepository.save<GameSession?>(session)
        }

        return BaseResponse(
            request.cmd,
            request.req_id,
            "ok"
        )
    }
}
