package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.maimai.dao.userdata.UserDataRepository;
import icu.samnyan.aqua.sega.maimai.dao.userdata.UserWebOptionRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.response.GetUserPreviewResp;
import icu.samnyan.aqua.sega.maimai.model.userdata.UserData;
import icu.samnyan.aqua.sega.maimai.model.userdata.UserWebOption;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiGetUserPreviewHandler")
public class GetUserPreviewHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserPreviewHandler.class);

    private final BasicMapper mapper;

    private final UserDataRepository userDataRepository;
    private final UserWebOptionRepository userWebOptionRepository;

    public GetUserPreviewHandler(BasicMapper mapper, UserDataRepository userDataRepository, UserWebOptionRepository userWebOptionRepository) {
        this.mapper = mapper;
        this.userDataRepository = userDataRepository;
        this.userWebOptionRepository = userWebOptionRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        Optional<UserData> userDataOptional = userDataRepository.findByCard_ExtId(userId);

        GetUserPreviewResp resp = new GetUserPreviewResp();
        resp.setUserId(userId);
        String json;
        if (userDataOptional.isPresent() && userDataOptional.get().getUserName() != null) {
            UserData user = userDataOptional.get();
            Optional<UserWebOption> userWebOptionOptional = userWebOptionRepository.findByUser_Card_ExtId(userId);
            resp.setUserName(user.getUserName());
            resp.setLogin(true);
            resp.setLastDataVersion(user.getLastDataVersion());
            resp.setLastLoginDate(user.getLastPlayDate());
            resp.setLastPlayDate(user.getLastPlayDate());
            resp.setPlayerRating(user.getPlayerRating());
            resp.setNameplateId(user.getNameplateId());
            resp.setFrameId(user.getFrameId());
            resp.setIconId(user.getIconId());
            resp.setTrophyId(user.getTrophyId());
            if (userWebOptionOptional.isPresent()) {
                UserWebOption option = userWebOptionOptional.get();
                resp.setDispRate(option.getDispRate());
                resp.setDispRank(option.getDispRank());
                resp.setDispHomeRanker(option.getDispHomeRanker());
                resp.setDispTotalLv(option.getDispTotalLv());
            }
            resp.setTotalLv(user.getTotalLv());
            json = mapper.write(resp);
        } else {
            json = "{}";
        }

        logger.info("Response: " + json);
        return json;
    }
}
