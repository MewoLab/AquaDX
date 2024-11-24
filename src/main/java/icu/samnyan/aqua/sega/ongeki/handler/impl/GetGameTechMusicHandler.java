package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
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
@Component("OngekiGetGameTechMusicHandler")
public class GetGameTechMusicHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameTechMusicHandler.class);

    private final BasicMapper mapper;

    @Autowired
    public GetGameTechMusicHandler(BasicMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        List<Object> techMusicList = new ArrayList<>();

        // This endpoint seems related to "techchallengeevent" in game data, TBD

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("length", 0);
        resultMap.put("gameTechMusicList", techMusicList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
