package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiGetUserCourseHandler")
public class GetUserCourseHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserCourseHandler.class);

    private final BasicMapper mapper;

    public GetUserCourseHandler(BasicMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Response Format:
     * nextIndex
     * userCourseList
     * courseId
     * rate
     *
     */
    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", 0);
        resultMap.put("userCourseList", null);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
