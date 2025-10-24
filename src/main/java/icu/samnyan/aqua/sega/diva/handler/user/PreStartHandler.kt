package icu.samnyan.aqua.sega.diva.handler.user

import icu.samnyan.aqua.sega.diva.dao.userdata.GameSessionRepository
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.common.PreStartResult
import icu.samnyan.aqua.sega.diva.model.common.StartMode
import icu.samnyan.aqua.sega.diva.model.request.user.PreStartRequest
import icu.samnyan.aqua.sega.diva.model.response.user.PreStartResponse
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.ThreadLocalRandom

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class PreStartHandler(
    private val playerProfileService: PlayerProfileService,
    private val gameSessionRepository: GameSessionRepository
) : BaseHandler() {
    fun handle(request: PreStartRequest): Any {
        val profileOptional = playerProfileService.findByPdId(request.aime_id)
        val response: PreStartResponse?
        if (profileOptional.isEmpty) {
            response = PreStartResponse(
                request.cmd,
                request.req_id,
                "ok",
                PreStartResult.NEW_REGISTRATION
            )

            val resp = this.build(mapper.toMap(response))
            logger.info("Response: {}", resp)

            return resp
        } else {
            val profile = profileOptional.get()

            val sessionOptional = gameSessionRepository.findByPdId(profile)
            if (sessionOptional.isPresent) {
                val session = sessionOptional.get()
                if (!session.lastUpdateTime
                        .isBefore(LocalDateTime.now().minusMinutes(5)) && session.startMode == StartMode.START
                ) {
                    response = PreStartResponse(
                        request.cmd,
                        request.req_id,
                        "ok",
                        PreStartResult.ALREADY_PLAYING
                    )

                    val resp = this.build(mapper.toMap(response))
                    logger.info("Response: {}", resp)

                    return resp
                } else {
                    gameSessionRepository.delete(session)
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

            gameSessionRepository.save<GameSession?>(session)

            response = PreStartResponse(
                request.cmd,
                request.req_id,
                "ok",
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

            val resp = this.build(mapper.toMap(response))
            logger.info("Response: {}", resp)

            return resp
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(PreStartHandler::class.java)
    }
}
