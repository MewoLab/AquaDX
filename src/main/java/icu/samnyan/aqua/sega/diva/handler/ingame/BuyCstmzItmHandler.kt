package icu.samnyan.aqua.sega.diva.handler.ingame

import icu.samnyan.aqua.sega.diva.DivaCustomizeRepository
import icu.samnyan.aqua.sega.diva.GameSessionRepository
import icu.samnyan.aqua.sega.diva.util.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.util.SessionNotFoundException
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.common.Result
import icu.samnyan.aqua.sega.diva.model.request.ingame.BuyCstmzItmRequest
import icu.samnyan.aqua.sega.diva.model.response.ingame.BuyCstmzItmResponse
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession
import icu.samnyan.aqua.sega.diva.service.PlayerCustomizeService
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService
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
        val profile = playerProfileService.findByPdId(request.pd_id).orElseThrow(
            Supplier { ProfileNotFoundException() })

        val session = gameSessionRepository.findByPdId(profile)
            .orElseThrow(Supplier { SessionNotFoundException() })

        val customizeOptional = divaCustomizeRepository.findById(request.cstmz_itm_id)

        if (customizeOptional.isEmpty) {
            return BuyCstmzItmResponse(
                request.cmd,
                request.req_id,
                "ok",
                Result.FAILED
            )
        } else {
            if (session.vp < customizeOptional.get().price) {
                return BuyCstmzItmResponse(
                    request.cmd,
                    request.req_id,
                    "ok",
                    Result.FAILED
                )
            } else {
                playerCustomizeService.buy(profile, request.cstmz_itm_id)
                session.vp = session.vp - customizeOptional.get().price
                gameSessionRepository.save<GameSession?>(session)

                return BuyCstmzItmResponse(
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
    }
}
