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

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("ChusanEndMatchingHandler")
public class EndMatchingHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(EndMatchingHandler.class);

    private final StringMapper mapper;

    @Autowired
    public EndMatchingHandler(StringMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        //roomId, userId

        Map<String, Object> matchingResult = new LinkedHashMap<>();
        matchingResult.put("matchingMemberInfoList", List.of()); // MatchingMemberInfo
        matchingResult.put("matchingMemberRoleList", List.of()); // Role?
        matchingResult.put("reflectorUri", "");

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("matchingResult", matchingResult);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }

}
