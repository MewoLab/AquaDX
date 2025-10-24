package icu.samnyan.aqua.sega.diva.handler.ingame

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.request.ingame.StageStartRequest
import icu.samnyan.aqua.sega.diva.model.response.BaseResponse
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession
import icu.samnyan.aqua.sega.diva.util.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.util.SessionNotFoundException
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class StageStartHandler(val db: DivaRepos) {
    fun handle(request: StageStartRequest): Any {
        if (request.getPd_id() != -1L) {
            val profile = db.profile.findByPdId(request.getPd_id()).orElseThrow<ProfileNotFoundException?>(
                Supplier { ProfileNotFoundException() })
            val session = db.gameSession.findByPdId(profile)
                .orElseThrow<SessionNotFoundException?>(Supplier { SessionNotFoundException() })

            val stageArr = request.getStg_ply_pv_id()
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
            db.gameSession.save<GameSession?>(session)
        }

        return BaseResponse(
            request.cmd,
            request.req_id,
            "ok"
        )
    }
}
