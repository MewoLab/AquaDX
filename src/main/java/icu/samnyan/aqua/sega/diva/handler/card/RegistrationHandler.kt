package icu.samnyan.aqua.sega.diva.handler.card

import icu.samnyan.aqua.sega.diva.PlayerProfileService
import icu.samnyan.aqua.sega.diva.model.common.Result
import icu.samnyan.aqua.sega.diva.model.request.card.RegistrationRequest
import icu.samnyan.aqua.sega.diva.model.response.card.RegistrationResponse
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class RegistrationHandler(val service: PlayerProfileService) {
    fun handle(request: RegistrationRequest) =
        if (service.findByPdId(request.aime_id).isPresent) {
            RegistrationResponse(
                request.cmd,
                request.req_id,
                "ok",
                Result.FAILED,
                -1
            )
        } else {
            val profile = service.register(request)
            RegistrationResponse(
                request.cmd,
                request.req_id,
                "ok",
                Result.SUCCESS,
                profile.pdId
            )
        }
}
