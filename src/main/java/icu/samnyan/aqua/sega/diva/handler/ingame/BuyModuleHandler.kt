package icu.samnyan.aqua.sega.diva.handler.ingame

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.common.Result
import icu.samnyan.aqua.sega.diva.model.request.ingame.BuyModuleRequest
import icu.samnyan.aqua.sega.diva.model.response.ingame.BuyModuleResponse
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class BuyModuleHandler(val db: DivaRepos) {
    fun handle(request: BuyModuleRequest): Any {
        val (profile, session) = db.session(request.pd_id)
        val moduleOptional = db.g.module.findById(request.mdl_id)

        if (moduleOptional.isEmpty) {
            return BuyModuleResponse(
                request.cmd,
                request.req_id,
                "ok",
                Result.FAILED
            )
        }
        if (session.vp < moduleOptional.get().price) {
            return BuyModuleResponse(
                request.cmd,
                request.req_id,
                "ok",
                Result.FAILED
            )
        }
        db.s.module.buy(profile, request.mdl_id)
        session.vp -= moduleOptional.get().price
        db.gameSession.save(session)

        return BuyModuleResponse(
            request.cmd,
            request.req_id,
            "ok",
            Result.SUCCESS,
            request.mdl_id,
            db.s.module.getModuleHaveString(profile),
            session.vp
        )
    }
}
