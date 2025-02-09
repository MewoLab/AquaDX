package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.chunithm.dao.gamedata.GameChargeRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.gamedata.GameCharge;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
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
@Component
public class GetGameChargeHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameChargeHandler.class);
    private final GameChargeRepository gameChargeRepository;
    private final StringMapper mapper;

    @Autowired
    public GetGameChargeHandler(GameChargeRepository gameChargeRepository, StringMapper mapper) {
        this.gameChargeRepository = gameChargeRepository;
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {

        List<GameCharge> gameChargeList = gameChargeRepository.findAll();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("length", gameChargeList.size());
        resultMap.put("gameChargeList", gameChargeList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
