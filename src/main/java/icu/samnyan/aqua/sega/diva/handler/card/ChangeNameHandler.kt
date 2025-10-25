package icu.samnyan.aqua.sega.diva.handler.card

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.ChangeNameRequest
import icu.samnyan.aqua.sega.diva.model.ChangeNameResponse
import icu.samnyan.aqua.sega.diva.model.common.Result
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


