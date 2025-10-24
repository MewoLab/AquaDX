package icu.samnyan.aqua.sega.diva.handler.ingame

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.common.Result
import icu.samnyan.aqua.sega.diva.model.request.ingame.BuyModuleRequest
import icu.samnyan.aqua.sega.diva.model.response.ingame.BuyModuleResponse
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession
import icu.samnyan.aqua.sega.diva.util.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.util.SessionNotFoundException
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class BuyModuleHandler(val db: DivaRepos) {
    fun handle(request: BuyModuleRequest): Any {
        val profile = db.profile.findByPdId(request.pd_id).orElseThrow<ProfileNotFoundException?>(
            Supplier { ProfileNotFoundException() })

        val session = db.gameSession.findByPdId(profile)
            .orElseThrow<SessionNotFoundException?>(Supplier { SessionNotFoundException() })

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
        db.gameSession.save<GameSession?>(session)

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
