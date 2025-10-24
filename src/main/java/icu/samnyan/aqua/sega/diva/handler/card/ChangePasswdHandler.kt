package icu.samnyan.aqua.sega.diva.handler.card

import icu.samnyan.aqua.sega.diva.dao.DivaRepos
import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.exception.SessionNotFoundException
import icu.samnyan.aqua.sega.diva.model.common.PassStat
import icu.samnyan.aqua.sega.diva.model.common.Result
import icu.samnyan.aqua.sega.diva.model.request.card.ChangePasswdRequest
import icu.samnyan.aqua.sega.diva.model.response.card.ChangePasswdResponse
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class ChangePasswdHandler(val db: DivaRepos) {
    fun handle(request: ChangePasswdRequest): Any {
        val profile = db.profile.findByPdId(request.pd_id)
            .orElseThrow(Supplier { ProfileNotFoundException() })
        val session = db.gameSession.findByPdId(profile)
            .orElseThrow(Supplier { SessionNotFoundException() })

        profile.passwordStatus = PassStat.SET
        profile.password = request.new_passwd
        db.profile.save(profile)
        db.gameSession.delete(session)

        return ChangePasswdResponse(
            request.cmd,
            request.req_id,
            "ok",
            Result.SUCCESS,
            session.acceptId,
            profile.pdId
        )
    }
}
