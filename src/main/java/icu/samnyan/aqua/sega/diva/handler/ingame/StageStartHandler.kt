package icu.samnyan.aqua.sega.diva.handler.ingame

import ext.emptyMap
import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.StageStartRequest
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class StageStartHandler(val db: DivaRepos) {
    fun handle(request: StageStartRequest): Any {
        if (request.pd_id != -1L) {
            val (_, session) = db.session(request.pd_id)

            val stageArr = request.stg_ply_pv_id
            var stageIndex = 0
            if (stageArr[0] != -1) {
                stageIndex = 0
            }
            if (stageArr[1] != -1) {
                stageIndex = 1
            }
            if (stageArr[2] != -1) {
                stageIndex = 2
            }
            if (stageArr[3] != -1) {
                stageIndex = 3
            }
            session.stageIndex = stageIndex
            db.gameSession.save(session)
        }

        return emptyMap
    }
}
