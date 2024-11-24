package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The game doesn't send this to save
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetUserRegionHandler")
public class GetUserRegionHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserRegionHandler.class);

    private final BasicMapper mapper;

    @Autowired
    public GetUserRegionHandler(BasicMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", 0);
        resultMap.put("userRegionList", new List[]{});

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
