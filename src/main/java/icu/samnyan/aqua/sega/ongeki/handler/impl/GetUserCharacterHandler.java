package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserCharacterRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserCharacter;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetUserCharacterHandler")
public class GetUserCharacterHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserCharacterHandler.class);

    private final BasicMapper mapper;

    private final UserCharacterRepository userCharacterRepository;

    @Autowired
    public GetUserCharacterHandler(BasicMapper mapper, UserCharacterRepository userCharacterRepository) {
        this.mapper = mapper;
        this.userCharacterRepository = userCharacterRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();
        Integer maxCount = (Integer) request.get("maxCount");
        Integer nextIndex = (Integer) request.get("nextIndex");

        int pageNum = nextIndex / maxCount;

        Page<UserCharacter> dbPage = userCharacterRepository.findByUser_Card_ExtId(userId, PageRequest.of(pageNum, maxCount));

        long currentIndex = maxCount * pageNum + dbPage.getNumberOfElements();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", dbPage.getNumberOfElements());
        resultMap.put("nextIndex", dbPage.getNumberOfElements() < maxCount ? -1 : currentIndex);
        resultMap.put("userCharacterList", dbPage.getContent());

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
