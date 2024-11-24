package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handle GetUserFavoriteItem request
 * @author yueou (yueou.xu@gmail.com)
 */

@Component
public class GetUserFavoriteItemHandler  implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserFavoriteItemHandler.class);

    private final StringMapper mapper;

    @Autowired
    public GetUserFavoriteItemHandler(StringMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");
        String kind = (String) request.get("kind");

        // TODO:


        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", 0);
        resultMap.put("kind", kind);
        resultMap.put("nextIndex", -1);
        resultMap.put("userFavoriteItemList", List.of());

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
