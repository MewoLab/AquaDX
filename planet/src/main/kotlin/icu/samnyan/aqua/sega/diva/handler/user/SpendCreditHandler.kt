package icu.samnyan.aqua.sega.diva.handler.user

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.SpendCreditRequest
import icu.samnyan.aqua.sega.diva.model.SpendCreditResponse
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class SpendCreditHandler(val db: DivaRepos) {
    fun handle(request: SpendCreditRequest): Any {
        val profile = db.profile(request.pd_id)

        return SpendCreditResponse(
            "-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x",
            0,
            profile.vocaloidPoints,
            profile.levelTitle,
            profile.plateEffectId,
            profile.plateId
        )
    }
}
