package aquadx.sega.diva.handler.card

import aquadx.sega.diva.DivaRepos
import aquadx.sega.diva.model.ChangeNameRequest
import aquadx.sega.diva.model.ChangeNameResponse
import aquadx.sega.diva.model.common.Result
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class ChangeNameHandler(val db: DivaRepos) {
    fun handle(request: ChangeNameRequest): Any {
        val (profile, session) = db.session(request.pd_id)

        profile.playerName = request.player_name
        db.profile.save(profile)
        db.gameSession.delete(session)

        return ChangeNameResponse(
            Result.SUCCESS,
            session.acceptId,
            profile.pdId,
            profile.playerName
        )
    }
}


