package aquadx.sega.diva.handler.card

import aquadx.sega.diva.PlayerProfileService
import aquadx.sega.diva.model.RegistrationRequest
import aquadx.sega.diva.model.RegistrationResponse
import aquadx.sega.diva.model.common.Result
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
