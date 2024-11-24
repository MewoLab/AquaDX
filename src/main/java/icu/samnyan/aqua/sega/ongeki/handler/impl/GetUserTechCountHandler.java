package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserTechCountRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserTechCount;
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
@Component("OngekiGetTechCountHandler")
public class GetUserTechCountHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserTechCountHandler.class);

    private final BasicMapper mapper;

    private final UserTechCountRepository userTechCountRepository;

    @Autowired
    public GetUserTechCountHandler(BasicMapper mapper, UserTechCountRepository userTechCountRepository) {
        this.mapper = mapper;
        this.userTechCountRepository = userTechCountRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        List<UserTechCount> userTechCountList = userTechCountRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userTechCountList.size());
        resultMap.put("userTechCountList", userTechCountList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
