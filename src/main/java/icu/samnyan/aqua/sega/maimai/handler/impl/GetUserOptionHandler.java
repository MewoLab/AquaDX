package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.maimai.dao.userdata.UserOptionRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.userdata.UserOption;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiGetUserOptionHandler")
public class GetUserOptionHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserOptionHandler.class);

    private final BasicMapper mapper;

    private final UserOptionRepository userOptionRepository;

    public GetUserOptionHandler(BasicMapper mapper, UserOptionRepository userOptionRepository) {
        this.mapper = mapper;
        this.userOptionRepository = userOptionRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        UserOption userOption = userOptionRepository.findByUser_Card_ExtId(userId).orElseThrow();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("userOption", userOption);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
