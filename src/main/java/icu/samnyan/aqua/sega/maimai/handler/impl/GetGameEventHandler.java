package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.maimai.dao.gamedata.GameEventRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.gamedata.GameEvent;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiGetGameEventHandler")
public class GetGameEventHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameEventHandler.class);

    private final BasicMapper mapper;

    private final GameEventRepository gameEventRepository;

    public GetGameEventHandler(BasicMapper mapper, GameEventRepository gameEventRepository) {
        this.mapper = mapper;
        this.gameEventRepository = gameEventRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        int type = ((Number) request.get("type")).intValue();
        Boolean isAllEvent = (Boolean) request.get("isAllEvent");

        List<GameEvent> gameEventList = gameEventRepository.findByType(type);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("type", type);
        resultMap.put("length", gameEventList.size());
        resultMap.put("gameEventList", gameEventList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
