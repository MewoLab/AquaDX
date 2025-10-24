package icu.samnyan.aqua.sega.diva.handler.user

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.util.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.model.request.user.SpendCreditRequest
import icu.samnyan.aqua.sega.diva.model.response.user.SpendCreditResponse
import icu.samnyan.aqua.sega.diva.PlayerProfileService
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class SpendCreditHandler(val db: DivaRepos) {
    fun handle(request: SpendCreditRequest): Any {
        val profile = db.profile.findByPdId(request.pd_id).orElseThrow<ProfileNotFoundException?>(
            Supplier { ProfileNotFoundException() })

        return SpendCreditResponse(
            request.cmd,
            request.req_id,
            "ok",
            "-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x",
            0,
            profile.vocaloidPoints,
            profile.levelTitle,
            profile.plateEffectId,
            profile.plateId
        )
    }
}
