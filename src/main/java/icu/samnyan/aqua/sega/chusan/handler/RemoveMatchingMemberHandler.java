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

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("ChusanRemoveMatchingMemberHandler")
public class RemoveMatchingMemberHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(RemoveMatchingMemberHandler.class);

    private final StringMapper mapper;

    @Autowired
    public RemoveMatchingMemberHandler(StringMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("returnCode", 1);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }

}
