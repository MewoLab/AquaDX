package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserCharacter;
import icu.samnyan.aqua.sega.chunithm.service.UserCharacterService;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handle getUserCharacter request
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class GetUserCharacterHandler implements BaseHandler {


    private static final Logger logger = LoggerFactory.getLogger(GetUserCharacterHandler.class);

    private final StringMapper mapper;

    private final UserCharacterService userCharacterService;


    @Autowired
    public GetUserCharacterHandler(StringMapper mapper, UserCharacterService userCharacterService) {
        this.mapper = mapper;
        this.userCharacterService = userCharacterService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");
        int nextIndex = Integer.parseInt((String) request.get("nextIndex"));
        int maxCount = Integer.parseInt((String) request.get("maxCount"));

        int pageNum = nextIndex / maxCount;

        Page<UserCharacter> dbPage = userCharacterService.getByUserId(userId, pageNum, maxCount);

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
