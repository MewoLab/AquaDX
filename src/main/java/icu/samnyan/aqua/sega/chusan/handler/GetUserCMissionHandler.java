package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.chusan.dao.userdata.UserCMissionProgressRepository;
import icu.samnyan.aqua.sega.chusan.dao.userdata.UserCMissionRepository;
import icu.samnyan.aqua.sega.chusan.model.response.data.UserCMissionProgressResp;
import icu.samnyan.aqua.sega.chusan.model.response.data.UserCMissionResp;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserCMission;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component("ChusanGetUserCMissionHandler")
public class GetUserCMissionHandler implements BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserCMissionHandler.class);

    private final StringMapper mapper;

    private final UserCMissionProgressRepository userCMissionProgressRepository;
    private final UserCMissionRepository userCMissionRepository;

    @Autowired
    public GetUserCMissionHandler(StringMapper mapper, UserCMissionProgressRepository userCMissionProgressRepository, UserCMissionRepository userCMissionRepository) {
        this.mapper = mapper;
        this.userCMissionProgressRepository = userCMissionProgressRepository;
        this.userCMissionRepository = userCMissionRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = Long.parseLong((String) request.get("userId"));
        int missionId = Integer.parseInt((String) request.get("missionId"));

        UserCMissionResp userCMissionResp = new UserCMissionResp();
        userCMissionResp.setUserId(userId);
        userCMissionResp.setMissionId(missionId);

        Optional<UserCMission> userCMissionOptional = userCMissionRepository.findByUser_Card_ExtIdAndMissionId(userId, missionId);
        if (userCMissionOptional.isPresent()) {
            userCMissionResp.setPoint(userCMissionOptional.get().getPoint());
            List<UserCMissionProgressResp> userCMissionProgressRespList = userCMissionProgressRepository.findByUser_Card_ExtIdAndMissionId(userId, missionId).stream()
                    .map(userCMissionProgress -> {
                        UserCMissionProgressResp userCMissionProgressResp = new UserCMissionProgressResp();
                        userCMissionProgressResp.setOrder(userCMissionProgress.getOrder());
                        userCMissionProgressResp.setProgress(userCMissionProgress.getProgress());
                        userCMissionProgressResp.setStage(userCMissionProgress.getStage());
                        return userCMissionProgressResp;
                    }).toList();
            userCMissionResp.setUserCMissionProgressList(userCMissionProgressRespList);
        }

        String json = mapper.write(userCMissionResp);
        logger.info("Response: " + json);
        return json;
    }
}
