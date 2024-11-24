package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserScenarioRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserScenario;
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
@Component("OngekiGetUserScenarioHandler")
public class GetUserScenarioHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserScenarioHandler.class);

    private final BasicMapper mapper;

    private final UserScenarioRepository userScenarioRepository;

    @Autowired
    public GetUserScenarioHandler(BasicMapper mapper, UserScenarioRepository userScenarioRepository) {
        this.mapper = mapper;
        this.userScenarioRepository = userScenarioRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        List<UserScenario> userScenarioList = userScenarioRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userScenarioList.size());
        resultMap.put("userScenarioList", userScenarioList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
