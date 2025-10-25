package icu.samnyan.aqua.sega.diva.handler.card

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.common.Result
import icu.samnyan.aqua.sega.diva.model.request.card.ChangeNameRequest
import icu.samnyan.aqua.sega.diva.model.response.card.ChangeNameResponse
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
            request.cmd,
            request.req_id,
            "ok",
            Result.SUCCESS,
            session.acceptId,
            profile.pdId,
            profile.playerName
        )
    }
}


