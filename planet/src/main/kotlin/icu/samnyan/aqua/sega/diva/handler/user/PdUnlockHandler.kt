package icu.samnyan.aqua.sega.diva.handler.user

import icu.samnyan.aqua.sega.diva.DIVA_OK
import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.PdUnlockRequest
import org.springframework.stereotype.Component

@Component
class PdUnlockHandler(val db: DivaRepos) {
    fun handle(request: PdUnlockRequest): Any {
        val (_, session) = db.session(request.pd_id)

        db.gameSession.delete(session)

        return DIVA_OK
    }
}
