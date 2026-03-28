package icu.samnyan.aqua.sega.diva.handler.ingame

import ext.invoke
import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.BuyCstmzItmRequest
import icu.samnyan.aqua.sega.diva.model.BuyCstmzItmResponse
import icu.samnyan.aqua.sega.diva.model.common.Result
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class BuyCstmzItmHandler(val db: DivaRepos) {
    fun handle(request: BuyCstmzItmRequest): Any {
        val (profile, session) = db.session(request.pd_id)

        val customize = db.g.customize.findById(request.cstmz_itm_id)() ?: return BuyCstmzItmResponse(
            Result.FAILED
        )

        if (session.vp < customize.price) {
            return BuyCstmzItmResponse(
                Result.FAILED
            )
        }
        db.s.customize.buy(profile, request.cstmz_itm_id)
        session.vp -= customize.price
        db.gameSession.save(session)

        return BuyCstmzItmResponse(
            Result.SUCCESS,
            request.cstmz_itm_id,
            db.s.customize.getModuleHaveString(profile),
            session.vp
        )
    }
}
