package icu.samnyan.aqua.sega.chusan.handler;

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

@Component("ChusanGetUserNetBattleDataHandler")
public class GetUserNetBattleDataHandler  implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserNetBattleDataHandler.class);

    private final StringMapper mapper;

    @Autowired
    public GetUserNetBattleDataHandler(StringMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);

        Map<String, Object> userNetBattleData = new LinkedHashMap<>();
        userNetBattleData.put("recentNBSelectMusicList", List.of());
        userNetBattleData.put("recentNBMusicList", List.of());

        resultMap.put("userNetBattleData", userNetBattleData);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}