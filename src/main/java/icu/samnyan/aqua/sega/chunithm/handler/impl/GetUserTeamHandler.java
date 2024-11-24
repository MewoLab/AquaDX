package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GetUserTeamHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserTeamHandler.class);

    private final StringMapper mapper;

    private final String teamName;


    public GetUserTeamHandler(StringMapper mapper, @Value("${game.chunithm.team-name:#{null}}") String teamName) {
        this.mapper = mapper;
        this.teamName = teamName;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");
        String playDate = (String) request.get("playDate");

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);

        if (teamName != null && !teamName.isEmpty()) {
            resultMap.put("teamId", 1);
            resultMap.put("teamRank", 1);
            resultMap.put("teamName", teamName);

            Map<String, Object> userTeamMap = new LinkedHashMap<>();
            userTeamMap.put("userId", userId);
            userTeamMap.put("teamId", 1);
            userTeamMap.put("orderId", 1);
            userTeamMap.put("teamPoint", 1);
            userTeamMap.put("aggrDate", playDate);

            resultMap.put("userTeamPoint", userTeamMap);
        } else {
            resultMap.put("teamId", 0);
        }

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}