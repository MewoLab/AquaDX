package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GetUserRivalMusicHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserRivalMusicHandler.class);

    private final StringMapper mapper;


    public GetUserRivalMusicHandler(StringMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("rivalId", 0);
        resultMap.put("length", 0);
        resultMap.put("nextIndex", 0);
        resultMap.put("userRivalMusicList", List.of());

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}