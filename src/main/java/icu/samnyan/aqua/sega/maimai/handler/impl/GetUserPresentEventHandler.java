package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.maimai.dao.userdata.UserPresentEventRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.userdata.UserPresentEvent;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiGetUserPresentEventHandler")
public class GetUserPresentEventHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserPresentEventHandler.class);

    private final BasicMapper mapper;

    private final UserPresentEventRepository userPresentEventRepository;

    public GetUserPresentEventHandler(BasicMapper mapper, UserPresentEventRepository userPresentEventRepository) {
        this.mapper = mapper;
        this.userPresentEventRepository = userPresentEventRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        List<UserPresentEvent> userPresentEventList = userPresentEventRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userPresentEventList.size());
        resultMap.put("userPresentEventList", userPresentEventList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
