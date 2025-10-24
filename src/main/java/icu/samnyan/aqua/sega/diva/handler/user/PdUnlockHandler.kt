package icu.samnyan.aqua.sega.diva.handler.user

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.request.user.PdUnlockRequest
import icu.samnyan.aqua.sega.diva.model.response.BaseResponse
import icu.samnyan.aqua.sega.diva.util.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.util.SessionNotFoundException
import org.springframework.stereotype.Component
import java.util.function.Supplier

@Component
class PdUnlockHandler(val db: DivaRepos) {
    fun handle(request: PdUnlockRequest): Any {
        val profile = db.profile.findByPdId(request.pd_id).orElseThrow<ProfileNotFoundException?>(
            Supplier { ProfileNotFoundException() })
        val session = db.gameSession.findByPdId(profile)
            .orElseThrow<SessionNotFoundException?>(Supplier { SessionNotFoundException() })

        db.gameSession.delete(session)

        return BaseResponse(
            request.cmd,
            request.req_id,
            "ok"
        )
    }
}
