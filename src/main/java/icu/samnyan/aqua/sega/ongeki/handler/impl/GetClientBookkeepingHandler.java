package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;

import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserKopRepository;
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
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetClientBookkeepingHandler")
public class GetClientBookkeepingHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetClientBookkeepingHandler.class);

    private final BasicMapper mapper;

    @Autowired
    public GetClientBookkeepingHandler(BasicMapper mapper, UserKopRepository userKopRepository) {
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long placeId = ((Number) request.get("placeId")).longValue();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("placeId", placeId);
        resultMap.put("length", 0);
        resultMap.put("clientBookkeepingList", List.of());

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
