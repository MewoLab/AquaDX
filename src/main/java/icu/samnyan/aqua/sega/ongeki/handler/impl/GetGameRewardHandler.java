package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.gamedata.GameRewardRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.gamedata.GameReward;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetGameRewardHandler")
public class GetGameRewardHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameRewardHandler.class);

    private final BasicMapper mapper;

    private final GameRewardRepository gameRewardRepository;

    @Autowired
    public GetGameRewardHandler(BasicMapper mapper, GameRewardRepository gameRewardRepository) {
        this.mapper = mapper;
        this.gameRewardRepository = gameRewardRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        Boolean isAllGP = (Boolean) request.get("isAllReward");

        List<GameReward> rewardList = gameRewardRepository.findAll();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("length", rewardList.size());
        resultMap.put("gameRewardList", rewardList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
