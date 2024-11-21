package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.userdata.UserGameOption;
import icu.samnyan.aqua.sega.chusan.service.UserGameOptionService;
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
@Component("ChusanGetUserOptionHandler")
public class GetUserOptionHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserOptionHandler.class);

    private final StringMapper mapper;

    private final UserGameOptionService userGameOptionService;

    @Autowired
    public GetUserOptionHandler(StringMapper mapper, UserGameOptionService userGameOptionService) {
        this.mapper = mapper;
        this.userGameOptionService = userGameOptionService;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");
        Optional<UserGameOption> userGameOption = userGameOptionService.getByUserId(userId);

        if (userGameOption.isPresent()) {
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put("userId", userId);
            resultMap.put("userGameOption", userGameOption.get());
            String json = mapper.write(resultMap);
            logger.info("Response: " + json);
            return json;
        }

        return null;
    }
}
