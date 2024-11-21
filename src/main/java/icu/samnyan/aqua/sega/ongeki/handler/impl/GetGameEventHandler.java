package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.gamedata.GameEventRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.gamedata.GameEvent;
import icu.samnyan.aqua.sega.ongeki.model.response.data.GameEventItem;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetGameEventHandler")
public class GetGameEventHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameEventHandler.class);

    private final BasicMapper mapper;

    private final GameEventRepository gameEventRepository;

    @Autowired
    public GetGameEventHandler(BasicMapper mapper, GameEventRepository gameEventRepository) {
        this.mapper = mapper;
        this.gameEventRepository = gameEventRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        Integer type = (Integer) request.get("type");

        List<GameEvent> eventIdList = gameEventRepository.findAll();
        List<GameEventItem> eventList = new ArrayList<>();
        eventIdList.forEach(x -> eventList.add(new GameEventItem(
                x.getId(),
                type,
                "2005-01-01 00:00:00.0",
                "2099-01-01 05:00:00.0"
        )));

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("type", type);
        resultMap.put("length", eventList.size());
        resultMap.put("gameEventList", eventList);

        String json = mapper.write(resultMap);
        logger.info("Response: {} events", eventList.size());
        return json;
    }
}
