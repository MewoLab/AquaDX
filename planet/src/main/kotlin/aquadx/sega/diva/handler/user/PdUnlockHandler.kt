package aquadx.sega.diva.handler.user

import aquadx.sega.diva.DIVA_OK
import aquadx.sega.diva.DivaRepos
import aquadx.sega.diva.model.PdUnlockRequest
import org.springframework.stereotype.Component

@Component
class PdUnlockHandler(val db: DivaRepos) {
    fun handle(request: PdUnlockRequest): Any {
        val (_, session) = db.session(request.pd_id)

        db.gameSession.delete(session)

        return DIVA_OK
    }
}
