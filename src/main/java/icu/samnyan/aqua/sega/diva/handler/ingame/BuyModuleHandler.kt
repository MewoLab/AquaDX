package icu.samnyan.aqua.sega.diva.handler.ingame

import icu.samnyan.aqua.sega.diva.DivaModuleRepository
import icu.samnyan.aqua.sega.diva.GameSessionRepository
import icu.samnyan.aqua.sega.diva.util.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.util.SessionNotFoundException
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.common.Result
import icu.samnyan.aqua.sega.diva.model.request.ingame.BuyModuleRequest
import icu.samnyan.aqua.sega.diva.model.response.ingame.BuyModuleResponse
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession
import icu.samnyan.aqua.sega.diva.service.PlayerModuleService
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class BuyModuleHandler(
    private val divaModuleRepository: DivaModuleRepository,
    private val playerProfileService: PlayerProfileService,
    private val playerModuleService: PlayerModuleService,
    private val gameSessionRepository: GameSessionRepository
) {
    fun handle(request: BuyModuleRequest): Any {
        val profile = playerProfileService.findByPdId(request.pd_id).orElseThrow<ProfileNotFoundException?>(
            Supplier { ProfileNotFoundException() })

        val session = gameSessionRepository.findByPdId(profile)
            .orElseThrow<SessionNotFoundException?>(Supplier { SessionNotFoundException() })

        val moduleOptional = divaModuleRepository.findById(request.mdl_id)

        val response: BuyModuleResponse?
        if (moduleOptional.isEmpty) {
            return BuyModuleResponse(
                request.cmd,
                request.req_id,
                "ok",
                Result.FAILED
            )
        } else {
            if (session.vp < moduleOptional.get().price) {
                return BuyModuleResponse(
                    request.cmd,
                    request.req_id,
                    "ok",
                    Result.FAILED
                )
            } else {
                playerModuleService.buy(profile, request.mdl_id)
                session.vp = session.vp - moduleOptional.get().price
                gameSessionRepository.save<GameSession?>(session)

                return BuyModuleResponse(
                    request.cmd,
                    request.req_id,
                    "ok",
                    Result.SUCCESS,
                    request.mdl_id,
                    playerModuleService.getModuleHaveString(profile),
                    session.vp
                )
            }
        }
    }
}
