package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.maimai.dao.userdata.UserWebOptionRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.userdata.UserWebOption;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiGetUserWebOptionHandler")
public class GetUserWebOptionHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserWebOptionHandler.class);

    private final BasicMapper mapper;

    private final UserWebOptionRepository userWebOptionRepository;

    public GetUserWebOptionHandler(BasicMapper mapper, UserWebOptionRepository userWebOptionRepository) {
        this.mapper = mapper;
        this.userWebOptionRepository = userWebOptionRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        UserWebOption userWebOption = userWebOptionRepository.findByUser_Card_ExtId(userId)
                .orElse(new UserWebOption(-1, null, true, 1, 1, 1, 1, 1));

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("userWebOption", userWebOption);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
