package aquadx.sega.diva.handler.ingame

import aquadx.sega.diva.DivaRepos
import aquadx.sega.diva.model.BuyModuleRequest
import aquadx.sega.diva.model.BuyModuleResponse
import aquadx.sega.diva.model.common.Result
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
