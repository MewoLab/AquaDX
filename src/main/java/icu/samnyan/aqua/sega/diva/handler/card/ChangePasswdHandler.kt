package icu.samnyan.aqua.sega.diva.handler.card

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.ChangePasswdRequest
import icu.samnyan.aqua.sega.diva.model.ChangePasswdResponse
import icu.samnyan.aqua.sega.diva.model.common.PassStat
import icu.samnyan.aqua.sega.diva.model.common.Result
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class ChangePasswdHandler(val db: DivaRepos) {
    fun handle(request: ChangePasswdRequest): Any {
        val (profile, session) = db.session(request.pd_id)

        profile.passwordStatus = PassStat.SET
        profile.password = request.new_passwd
        db.profile.save(profile)
        db.gameSession.delete(session)

        return ChangePasswdResponse(
            Result.SUCCESS,
            session.acceptId,
            profile.pdId
        )
    }
}
