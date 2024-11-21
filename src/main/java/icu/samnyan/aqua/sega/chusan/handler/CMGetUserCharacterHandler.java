package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserCharacter;
import icu.samnyan.aqua.sega.chusan.service.UserCharacterService;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handle getUserCharacter request
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("ChusanCMGetUserCharacterHandler")
public class CMGetUserCharacterHandler implements BaseHandler {


    private static final Logger logger = LoggerFactory.getLogger(CMGetUserCharacterHandler.class);

    private final BasicMapper mapper;

    private final UserCharacterService userCharacterService;


    @Autowired
    public CMGetUserCharacterHandler(BasicMapper mapper, UserCharacterService userCharacterService) {
        this.mapper = mapper;
        this.userCharacterService = userCharacterService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = String.valueOf(request.get("userId"));
        int nextIndex = ((Number) request.get("nextIndex")).intValue();
        int maxCount = ((Number) request.get("maxCount")).intValue();

        int pageNum = nextIndex / maxCount;

        Page<UserCharacter> dbPage = userCharacterService.getByUserId(userId, pageNum, maxCount);

        long currentIndex = maxCount * pageNum + dbPage.getNumberOfElements();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", dbPage.getNumberOfElements());
        resultMap.put("nextIndex", dbPage.getNumberOfElements() < maxCount ? -1 : currentIndex);
        List<Integer> userCharacterIdList = new ArrayList<>();

        dbPage.getContent().forEach(userCharacter -> {
            userCharacterIdList.add(userCharacter.getCharacterId());
        });

        resultMap.put("userCharacterList", userCharacterIdList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
