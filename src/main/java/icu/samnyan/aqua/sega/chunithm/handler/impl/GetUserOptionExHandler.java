package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserGameOptionEx;
import icu.samnyan.aqua.sega.chunithm.service.UserGameOptionExService;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class GetUserOptionExHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserOptionExHandler.class);

    private final StringMapper mapper;

    private final UserGameOptionExService userGameOptionExService;

    @Autowired
    public GetUserOptionExHandler(StringMapper mapper, UserGameOptionExService userGameOptionExService) {
        this.mapper = mapper;
        this.userGameOptionExService = userGameOptionExService;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");
        Optional<UserGameOptionEx> userGameOptionEx = userGameOptionExService.getByUserId(userId);

        if (userGameOptionEx.isPresent()) {
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put("userId", userId);
            resultMap.put("userGameOptionEx", userGameOptionEx.get());

            String json = mapper.write(resultMap);
            logger.info("Response: " + json);
            return json;
        }

        return null;
    }
}
