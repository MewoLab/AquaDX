package icu.samnyan.aqua.sega.diva.handler.user

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.request.user.PdUnlockRequest
import icu.samnyan.aqua.sega.diva.model.response.BaseResponse
import org.springframework.stereotype.Component

@Component
class PdUnlockHandler(val db: DivaRepos) {
    fun handle(request: PdUnlockRequest): Any {
        val (_, session) = db.session(request.pd_id)

        db.gameSession.delete(session)

        return BaseResponse(
            request.cmd,
            request.req_id,
            "ok"
        )
    }
}
