package icu.samnyan.aqua.sega.chusan.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chusan.model.response.data.UserEmoney;
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3UserData;
import icu.samnyan.aqua.sega.chusan.service.UserDataService;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
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
@Component("ChusanCMGetUserDataHandler")
public class CMGetUserDataHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(CMGetUserDataHandler.class);

    private final BasicMapper mapper;

    private final UserDataService userDataService;

    @Autowired
    public CMGetUserDataHandler(BasicMapper mapper, UserDataService userDataService) {
        this.mapper = mapper;
        this.userDataService = userDataService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = String.valueOf(request.get("userId"));
        Optional<Chu3UserData> userDataOptional = userDataService.getUserByExtId(userId);

        if (userDataOptional.isPresent()) {
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put("userId", userId);
            Chu3UserData user = userDataOptional.get();
            UserEmoney userEmoney = new UserEmoney();
            user.setUserEmoney(userEmoney);

            resultMap.put("userData", user);
            resultMap.put("userEmoney", userEmoney);

            String json = mapper.write(resultMap);

            logger.info("Response: " + json);
            return json;
        }

        return null;
    }
}
