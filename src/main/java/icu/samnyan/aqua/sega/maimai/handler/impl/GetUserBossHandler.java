package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.maimai.dao.userdata.UserBossRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.userdata.UserBoss;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiGetUserBossHandler")
public class GetUserBossHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserBossHandler.class);

    private final BasicMapper mapper;

    private final UserBossRepository userBossRepository;

    public GetUserBossHandler(BasicMapper mapper, UserBossRepository userBossRepository) {
        this.mapper = mapper;
        this.userBossRepository = userBossRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        Optional<UserBoss> userBossOptional = userBossRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        if (userBossOptional.isPresent()) {
            resultMap.put("userBossData", userBossOptional.get());
        } else {
            resultMap.put("userBossData", null);
        }

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
