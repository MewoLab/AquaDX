package icu.samnyan.aqua.sega.diva.handler.ingame;

import icu.samnyan.aqua.sega.diva.dao.userdata.GameSessionRepository;
import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException;
import icu.samnyan.aqua.sega.diva.exception.SessionNotFoundException;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.request.ingame.StageStartRequest;
import icu.samnyan.aqua.sega.diva.model.response.BaseResponse;
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerProfile;
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@AllArgsConstructor
public class StageStartHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(StageResultHandler.class);
    private final GameSessionRepository gameSessionRepository;
    private final PlayerProfileService playerProfileService;

    public String handle(StageStartRequest request) {
        if (request.getPd_id() != -1) {
            PlayerProfile profile = playerProfileService.findByPdId(request.getPd_id()).orElseThrow(ProfileNotFoundException::new);
            GameSession session = gameSessionRepository.findByPdId(profile).orElseThrow(SessionNotFoundException::new);

            int[] stageArr = request.getStg_ply_pv_id();
            int stageIndex = 0;
            if(stageArr[0] != -1) {
                stageIndex = 0;
            }
            if(stageArr[1] != -1) {
                stageIndex = 1;
            }
            if(stageArr[2] != -1) {
                stageIndex = 2;
            }
            if(stageArr[3] != -1) {
                stageIndex = 3;
            }
            session.setStageIndex(stageIndex);
            gameSessionRepository.save(session);
        }

        BaseResponse response = new BaseResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok");

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
