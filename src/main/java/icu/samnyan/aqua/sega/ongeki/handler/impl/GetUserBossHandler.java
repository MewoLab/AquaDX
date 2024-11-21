package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserBossRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserBoss;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
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
@Component("OngekiGetUserBossHandler")
public class GetUserBossHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserBossHandler.class);

    private final BasicMapper mapper;

    private final UserBossRepository userBossRepository;

    @Autowired
    public GetUserBossHandler(BasicMapper mapper, UserBossRepository userBossRepository) {
        this.mapper = mapper;
        this.userBossRepository = userBossRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        List<UserBoss> userBossList = userBossRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userBossList.size());
        resultMap.put("userBossList", userBossList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
