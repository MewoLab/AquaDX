package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component("ChusanGetUserNetBattleRankingInfoHandler")
public class GetUserNetBattleRankingInfoHandler implements BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserNetBattleRankingInfoHandler.class);

    private final StringMapper mapper;

    @Autowired
    public GetUserNetBattleRankingInfoHandler(StringMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        logger.info("UserNetBattleRankingInfo Dummy Handler");

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", request.get("userId"));
        resultMap.put("length", 0);
        resultMap.put("userNetBattleRankingInfoList", new LinkedHashMap<>());

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
