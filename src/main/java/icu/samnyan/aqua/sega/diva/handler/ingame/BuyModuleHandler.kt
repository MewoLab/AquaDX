package icu.samnyan.aqua.sega.diva.handler.ingame

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.BuyModuleRequest
import icu.samnyan.aqua.sega.diva.model.BuyModuleResponse
import icu.samnyan.aqua.sega.diva.model.common.Result
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
                Result.FAILED
            )
        }
        if (session.vp < moduleOptional.get().price) {
            return BuyModuleResponse(
                Result.FAILED
            )
        }
        db.s.module.buy(profile, request.mdl_id)
        session.vp -= moduleOptional.get().price
        db.gameSession.save(session)

        return BuyModuleResponse(
            Result.SUCCESS,
            request.mdl_id,
            db.s.module.getModuleHaveString(profile),
            session.vp
        )
    }
}
