package aquadx.sega.diva.handler.user

import ext.invoke
import ext.logger
import aquadx.sega.diva.DivaRepos
import aquadx.sega.diva.model.PreStartRequest
import aquadx.sega.diva.model.PreStartResponse
import aquadx.sega.diva.model.common.PreStartResult
import aquadx.sega.diva.model.common.StartMode
import aquadx.sega.diva.model.db.userdata.GameSession
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class PreStartHandler(val db: DivaRepos) {
    var logger = logger()
    fun handle(request: PreStartRequest): Any {
        val profile = db.profile.findByPdId(request.aime_id)() ?: return PreStartResponse(PreStartResult.NEW_REGISTRATION)

        db.gameSession.findByPdId(profile)()?.let { session ->
            if (!session.lastUpdateTime.isBefore(LocalDateTime.now().minusMinutes(5)) && session.startMode == StartMode.START) {
                return PreStartResponse(PreStartResult.ALREADY_PLAYING)
            } else {
                db.gameSession.delete(session)
            }
        }

        val session = GameSession(
            ThreadLocalRandom.current().nextInt(100, 99999),
            profile,
            StartMode.PRE_START,
            LocalDateTime.now(),
            LocalDateTime.now(),
            -1,
            -1,
            -1,
            profile.level,
            profile.levelExp,
            profile.level,
            profile.levelExp,
            profile.vocaloidPoints
        )

        db.gameSession.save(session)

        return PreStartResponse(
            PreStartResult.SUCCESS,
            session.acceptId,
            profile.pdId,
            profile.playerName,
            profile.sortMode,
            profile.level,
            profile.levelExp,
            profile.levelTitle,
            profile.plateEffectId,
            profile.plateId,
            profile.commonModule,
            profile.commonModuleSetTime,
            profile.commonSkin,
            profile.buttonSe,
            profile.slideSe,
            profile.chainSlideSe,
            profile.sliderTouchSe,
            profile.vocaloidPoints,
            profile.passwordStatus
        )
    }
}
