package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.response.data.GameSale;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
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
@Component
public class GetGameSaleHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetGameSaleHandler.class);

    private final StringMapper mapper;

    @Autowired
    public GetGameSaleHandler(StringMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String type = (String) request.get("type");

        List<GameSale> gameSaleList = new ArrayList<>();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("type", type);
        resultMap.put("length", 0);
        resultMap.put("gameSaleList", gameSaleList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
