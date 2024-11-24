package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.chunithm.dao.gamedata.GameMessageRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.gamedata.GameMessage;
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
public class GetGameMessageHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameMessageHandler.class);

    private final GameMessageRepository gameMessageRepository;

    private final StringMapper mapper;

    @Autowired
    public GetGameMessageHandler(GameMessageRepository gameMessageRepository, StringMapper mapper) {
        this.gameMessageRepository = gameMessageRepository;
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String type = (String) request.get("type");

        List<GameMessage> gameMessageList = gameMessageRepository.findAll();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("type", type);
        resultMap.put("length", gameMessageList.size());
        resultMap.put("gameMessageList", gameMessageList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
