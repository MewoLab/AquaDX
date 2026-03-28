package icu.samnyan.aqua.sega.diva.handler.card

import icu.samnyan.aqua.sega.diva.PlayerProfileService
import icu.samnyan.aqua.sega.diva.model.RegistrationRequest
import icu.samnyan.aqua.sega.diva.model.RegistrationResponse
import icu.samnyan.aqua.sega.diva.model.common.Result
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class RegistrationHandler(val service: PlayerProfileService) {
    fun handle(request: RegistrationRequest) =
        if (service.findByPdId(request.aime_id).isPresent) {
            RegistrationResponse(
                Result.FAILED,
                -1
            )
        } else {
            val profile = service.register(request)
            RegistrationResponse(
                Result.SUCCESS,
                profile.pdId
            )
        }
}
