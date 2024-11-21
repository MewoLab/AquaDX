package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserDataEx;
import icu.samnyan.aqua.sega.chunithm.service.UserDataExService;
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
public class GetUserDataExHandler implements BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserDataExHandler.class);

    private final StringMapper mapper;

    private final UserDataExService userDataExService;

    @Autowired
    public GetUserDataExHandler(StringMapper mapper, UserDataExService userDataExService) {
        this.mapper = mapper;
        this.userDataExService = userDataExService;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");
        Optional<UserDataEx> userDataExOptional = userDataExService.getByExtId(userId);

        if (userDataExOptional.isPresent()) {
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put("userId", userId);
            resultMap.put("userDataEx", userDataExOptional.get());

            String json = mapper.write(resultMap);
            logger.info("Response: " + json);
            return json;
        }

        return null;
    }

}
