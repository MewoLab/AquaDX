package aquadx.sega.diva.handler.card

import aquadx.sega.diva.DivaRepos
import aquadx.sega.diva.model.ChangePasswdRequest
import aquadx.sega.diva.model.ChangePasswdResponse
import aquadx.sega.diva.model.common.PassStat
import aquadx.sega.diva.model.common.Result
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
