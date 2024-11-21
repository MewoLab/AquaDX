package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.maimai.dao.userdata.UserCharacterRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.userdata.UserCharacter;
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
@Component("MaimaiGetUserCharacterHandler")
public class GetUserCharacterHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserCharacterHandler.class);

    private final BasicMapper mapper;

    private final UserCharacterRepository userCharacterRepository;

    public GetUserCharacterHandler(BasicMapper mapper, UserCharacterRepository userCharacterRepository) {
        this.mapper = mapper;
        this.userCharacterRepository = userCharacterRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        List<UserCharacter> userCharacterList = userCharacterRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userCharacterList.size());
        resultMap.put("userCharacterList", userCharacterList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
