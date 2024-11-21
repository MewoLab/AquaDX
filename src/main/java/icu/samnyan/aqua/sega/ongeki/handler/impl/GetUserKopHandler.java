package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;

import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserKopRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserKop;
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
@Component("OngekiGetUserKopHandler")
public class GetUserKopHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserKopHandler.class);

    private final BasicMapper mapper;

    private final UserKopRepository userKopRepository;

    @Autowired
    public GetUserKopHandler(BasicMapper mapper, UserKopRepository userKopRepository) {
        this.mapper = mapper;
        this.userKopRepository = userKopRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();
        List<UserKop> kopList = userKopRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", kopList.size());
        resultMap.put("userKopList", kopList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
