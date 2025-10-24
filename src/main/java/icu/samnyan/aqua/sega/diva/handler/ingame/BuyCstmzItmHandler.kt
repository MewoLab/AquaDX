package icu.samnyan.aqua.sega.diva.handler.ingame

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.common.Result
import icu.samnyan.aqua.sega.diva.model.request.ingame.BuyCstmzItmRequest
import icu.samnyan.aqua.sega.diva.model.response.ingame.BuyCstmzItmResponse
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession
import icu.samnyan.aqua.sega.diva.util.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.util.SessionNotFoundException
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class BuyCstmzItmHandler(val db: DivaRepos) {
    fun handle(request: BuyCstmzItmRequest): Any {
        val profile = db.profile.findByPdId(request.pd_id).orElseThrow(
            Supplier { ProfileNotFoundException() })

        val session = db.gameSession.findByPdId(profile)
            .orElseThrow(Supplier { SessionNotFoundException() })

        val customizeOptional = db.g.customize.findById(request.cstmz_itm_id)

        if (customizeOptional.isEmpty) {
            return BuyCstmzItmResponse(
                request.cmd,
                request.req_id,
                "ok",
                Result.FAILED
            )
        }
        if (session.vp < customizeOptional.get().price) {
            return BuyCstmzItmResponse(
                request.cmd,
                request.req_id,
                "ok",
                Result.FAILED
            )
        }
        db.s.customize.buy(profile, request.cstmz_itm_id)
        session.vp -= customizeOptional.get().price
        db.gameSession.save<GameSession?>(session)

        return BuyCstmzItmResponse(
            request.cmd,
            request.req_id,
            "ok",
            Result.SUCCESS,
            request.cstmz_itm_id,
            db.s.customize.getModuleHaveString(profile),
            session.vp
        )
    }
}
