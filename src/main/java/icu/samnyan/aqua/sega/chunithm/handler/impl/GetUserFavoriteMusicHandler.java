package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserDuel;
import icu.samnyan.aqua.sega.chunithm.service.UserDuelService;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handle GetUserDuel request
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class GetUserFavoriteMusicHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserFavoriteMusicHandler.class);

    private final StringMapper mapper;

    @Autowired
    public GetUserFavoriteMusicHandler(StringMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");

        // TODO:


        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", 0);
        resultMap.put("userFavoriteMusicList", List.of());

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
